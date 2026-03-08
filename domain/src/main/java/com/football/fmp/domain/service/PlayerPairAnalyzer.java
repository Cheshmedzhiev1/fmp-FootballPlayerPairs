package com.football.fmp.domain.service;

import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;

import java.util.*;

public class PlayerPairAnalyzer {

    public List<PlayerPairResult> findLongestPlayingPair(List<Record> allRecords) {

        Map<Long, List<Record>> recordsByMatch = groupByMatch(allRecords);

        Map<String, List<PlayerPairResult.MatchDuration>> pairDurations = new HashMap<>();

        for (Map.Entry<Long, List<Record>> entry : recordsByMatch.entrySet()) {
            Long matchId = entry.getKey();
            List<Record> matchRecords = entry.getValue();
            calculatePairOverlaps(matchRecords, matchId, pairDurations);
        }

        return findMaxPairs(pairDurations);
    }

    private Map<Long, List<Record>> groupByMatch(List<Record> records) {
        Map<Long, List<Record>> grouped = new HashMap<>();
        for (Record record : records) {
            grouped.computeIfAbsent(record.matchId(), k -> new ArrayList<>()).add(record);
        }
        return grouped;
    }

    private void calculatePairOverlaps(
            List<Record> teamRecords,
            Long matchId,
            Map<String, List<PlayerPairResult.MatchDuration>> pairDurations
    ) {
        for (int i = 0; i < teamRecords.size(); i++) {
            for (int j = i + 1; j < teamRecords.size(); j++) {
                Record r1 = teamRecords.get(i);
                Record r2 = teamRecords.get(j);

                int overlap = calculateOverlap(r1, r2);

                if (overlap > 0) {
                    String pairKey = createPairKey(r1.playerId(), r2.playerId());

                    pairDurations.computeIfAbsent(pairKey, k -> new ArrayList<>())
                            .add(new PlayerPairResult.MatchDuration(matchId, overlap));
                }
            }
        }
    }

    private int calculateOverlap(Record r1, Record r2) {

        int from1 = r1.fromMinutes() != null ? r1.fromMinutes() : 0;
        int to1 = r1.toMinutes() != null ? r1.toMinutes() : 90;

        int from2 = r2.fromMinutes() != null ? r2.fromMinutes() : 0;
        int to2 = r2.toMinutes() != null ? r2.toMinutes() : 90;

        int overlapStart = Math.max(from1, from2);
        int overlapEnd = Math.min(to1, to2);

        return Math.max(0, overlapEnd - overlapStart);
    }

    private String createPairKey(Long playerId1, Long playerId2) {
        if (playerId1 < playerId2) {
            return playerId1 + "-" + playerId2;
        } else {
            return playerId2 + "-" + playerId1;
        }
    }

    private List<PlayerPairResult> findMaxPairs(Map<String, List<PlayerPairResult.MatchDuration>> pairDurations) {
        int maxTotalMinutes = 0;
        List<PlayerPairResult> bestPairs = new ArrayList<>();

        for (Map.Entry<String, List<PlayerPairResult.MatchDuration>> entry : pairDurations.entrySet()) {
            int totalMinutes = entry.getValue().stream()
                    .mapToInt(PlayerPairResult.MatchDuration::minutes)
                    .sum();

            if (totalMinutes > maxTotalMinutes) {
                maxTotalMinutes = totalMinutes;
                bestPairs.clear();
                bestPairs.add(toPlayerPairResult(entry.getKey(), totalMinutes, entry.getValue()));
            } else if (totalMinutes == maxTotalMinutes && totalMinutes > 0) {
                bestPairs.add(toPlayerPairResult(entry.getKey(), totalMinutes, entry.getValue()));
            }
        }

        bestPairs.sort(
                Comparator.comparing(PlayerPairResult::player1Id)
                        .thenComparing(PlayerPairResult::player2Id)
        );

        return bestPairs;
    }

    private PlayerPairResult toPlayerPairResult(
            String pairKey,
            int totalMinutes,
            List<PlayerPairResult.MatchDuration> durations
    ) {
        String[] playerIds = pairKey.split("-");
        Long player1Id = Long.parseLong(playerIds[0]);
        Long player2Id = Long.parseLong(playerIds[1]);
        List<PlayerPairResult.MatchDuration> sortedDurations = new ArrayList<>(durations);
        sortedDurations.sort(Comparator.comparing(PlayerPairResult.MatchDuration::matchId));

        return new PlayerPairResult(player1Id, player2Id, totalMinutes, sortedDurations);
    }
}