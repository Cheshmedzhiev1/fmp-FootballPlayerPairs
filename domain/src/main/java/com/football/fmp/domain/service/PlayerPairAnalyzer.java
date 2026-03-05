package com.football.fmp.domain.service;

import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;

import java.util.*;

public class PlayerPairAnalyzer {     // main logic of the domain
// takes all the records and returns the winning pair
    public PlayerPairResult findLongestPlayingPair(List<Record> allRecords) {
// groups all records by matchId
        Map<Long, List<Record>> recordsByMatch = groupByMatch(allRecords);
// accumulates result across all matches, list of MatchDuration- one entry per match played together
        Map<String, List<PlayerPairResult.MatchDuration>> pairDurations = new HashMap<>();
// process each match independently
        for (Map.Entry<Long, List<Record>> entry : recordsByMatch.entrySet()) {
            Long matchId = entry.getKey();
            List<Record> matchRecords = entry.getValue();
            // for each match, calculate overlaps for every pair and add results into pairDurations
            calculatePairOverlaps(matchRecords, matchId, pairDurations);
        }
// finds and returns pair with the highest total minutes
        return findMaxPair(pairDurations);
    }
//groups a flat list of records into a map keyed by matchID
    private Map<Long, List<Record>> groupByMatch(List<Record> records) {
        Map<Long, List<Record>> grouped = new HashMap<>();
        for (Record record : records) {
            // if this key doesn't exist,then create a new empty list then add the record to it <- avoids null check
            grouped.computeIfAbsent(record.matchId(), k -> new ArrayList<>()).add(record);
        }
        return grouped;
    }
//for a single match, calculates the overlap in mutes for every possible pair of players and accumulates the result
    // into pairDurations
    private void calculatePairOverlaps(
            List<Record> matchRecords, // all records for this specific match
            Long matchId,
            Map<String, List<PlayerPairResult.MatchDuration>> pairDurations // accumulated results map shared across the matches
    ) {
        // Track total overlap per pair for this match ONLY
        Map<String, Integer> matchPairMinutes = new HashMap<>();
// check every unique combination of two players in this match
        for (int i = 0; i < matchRecords.size(); i++) {
            for (int j = i + 1; j < matchRecords.size(); j++) {
                Record r1 = matchRecords.get(i);
                Record r2 = matchRecords.get(j);

                int overlap = calculateOverlap(r1, r2);

                if (overlap > 0) {
                    String pairKey = createPairKey(r1.playerId(), r2.playerId());
                    // merge adds 'overlap' to existing value , or sets it if key is new
                    matchPairMinutes.merge(pairKey, overlap, Integer::sum);
                }
            }
        }

        // Now add ONE MatchDuration entry per pair per match
        for (Map.Entry<String, Integer> entry : matchPairMinutes.entrySet()) {
            pairDurations.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                    .add(new PlayerPairResult.MatchDuration(matchId, entry.getValue()));
        }
    }
// calculates how many minutes two players were both on the pitch at the same time
    private int calculateOverlap(Record r1, Record r2) {

        int from1 = r1.fromMinutes() != null ? r1.fromMinutes() : 0;
        int to1 = r1.toMinutes() != null ? r1.toMinutes() : 90;

        int from2 = r2.fromMinutes() != null ? r2.fromMinutes() : 0;
        int to2 = r2.toMinutes() != null ? r2.toMinutes() : 90;

        int overlapStart = Math.max(from1, from2);  // latest entry
        int overlapEnd = Math.min(to1, to2);  // earliest exit
// negative result here means the time ranges don't overlap at all
        return Math.max(0, overlapEnd - overlapStart);
    }
// key always puts small id first, example (107,112) and (112,107) would be stored as separate pairs if not this
    private String createPairKey(Long playerId1, Long playerId2) {
        if (playerId1 < playerId2) {
            return playerId1 + "-" + playerId2;
        } else {
            return playerId2 + "-" + playerId1;
        }
    }
// scans all the accumulated pair data and returns the pair with the highest total minutes played together
    private PlayerPairResult findMaxPair(Map<String, List<PlayerPairResult.MatchDuration>> pairDurations) {
        String maxPairKey = null;
        // sum all minutes for this pair across every match they played together
        int maxTotalMinutes = 0;
        List<PlayerPairResult.MatchDuration> maxDurations = null;

        for (Map.Entry<String, List<PlayerPairResult.MatchDuration>> entry : pairDurations.entrySet()) {
            int totalMinutes = entry.getValue().stream()
                    .mapToInt(PlayerPairResult.MatchDuration::minutes)
                    .sum();
// keep track of the highest total found so far
            if (totalMinutes > maxTotalMinutes) {
                maxTotalMinutes = totalMinutes;
                maxPairKey = entry.getKey();
                maxDurations = entry.getValue();
            }
        }
// no valid pairs found, like empty input or no overlaps at all
        if (maxPairKey == null) {
            return null;
        }
// splits the final pair into two long ID's
        String[] playerIds = maxPairKey.split("-");
        Long player1Id = Long.parseLong(playerIds[0]);
        Long player2Id = Long.parseLong(playerIds[1]);

        return new PlayerPairResult(player1Id, player2Id, maxTotalMinutes, maxDurations);
    }
}