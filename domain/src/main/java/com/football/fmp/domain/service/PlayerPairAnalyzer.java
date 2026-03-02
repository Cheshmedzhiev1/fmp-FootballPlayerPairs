package com.football.fmp.domain.service;

import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerPairAnalyzer {

    public PlayerPairResult findLongestPlayingPair(
            List<Record> allRecords,
            Map<Long, Long> playerToTeamMap  // playerId -> teamId
    ) {

        Map<Long, List<Record>> recordsByMatch = groupByMatch(allRecords);

        Map<String, List<PlayerPairResult.MatchDuration>> pairDurations = new HashMap<>();

        for (Map.Entry<Long, List<Record>> entry : recordsByMatch.entrySet()) {
            Long matchId = entry.getKey();
            List<Record> matchRecords = entry.getValue();

            Map<Long, List<Record>> recordsByTeam = groupByTeam(matchRecords, playerToTeamMap);

            for (List<Record> teamRecords : recordsByTeam.values()) {
                calculatePairOverlaps(teamRecords, matchId, pairDurations);
            }
        }

        return findMaxPair(pairDurations);
    }

    private Map<Long, List<Record>> groupByMatch(List<Record> records) {
        Map<Long, List<Record>> grouped = new HashMap<>();
        for (Record record : records) {
            grouped.computeIfAbsent(record.matchId(), k -> new ArrayList<>()).add(record);
        }
        return grouped;
    }

    private Map<Long, List<Record>> groupByTeam(List<Record> records, Map<Long, Long> playerToTeamMap) {
        Map<Long, List<Record>> grouped = new HashMap<>();
        for (Record record : records) {
            Long teamId = playerToTeamMap.get(record.playerId());
            if (teamId != null) {
                grouped.computeIfAbsent(teamId, k -> new ArrayList<>()).add(record);
            }
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

        int from1 = r1.fromMinutes();
        int to1 = r1.toMinutes() != null ? r1.toMinutes() : 90;

        int from2 = r2.fromMinutes();
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

    private PlayerPairResult findMaxPair(Map<String, List<PlayerPairResult.MatchDuration>> pairDurations) {
        String maxPairKey = null;
        int maxTotalMinutes = 0;
        List<PlayerPairResult.MatchDuration> maxDurations = null;

        for (Map.Entry<String, List<PlayerPairResult.MatchDuration>> entry : pairDurations.entrySet()) {
            int totalMinutes = entry.getValue().stream()
                    .mapToInt(PlayerPairResult.MatchDuration::minutes)
                    .sum();

            if (totalMinutes > maxTotalMinutes) {
                maxTotalMinutes = totalMinutes;
                maxPairKey = entry.getKey();
                maxDurations = entry.getValue();
            }
        }

        if (maxPairKey == null) {
            return null;
        }

        String[] playerIds = maxPairKey.split("-");
        Long player1Id = Long.parseLong(playerIds[0]);
        Long player2Id = Long.parseLong(playerIds[1]);

        return new PlayerPairResult(player1Id, player2Id, maxTotalMinutes, maxDurations);
    }
}