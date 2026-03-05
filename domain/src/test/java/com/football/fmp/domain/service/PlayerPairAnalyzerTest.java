package com.football.fmp.domain.service;

import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerPairAnalyzerTest {

    private PlayerPairAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new PlayerPairAnalyzer();
    }

    // -------------------------------------------------------------------------
    // 1. BASIC HAPPY PATH
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Two players who played the full match together return 90 minutes overlap")
    void twoPlayersFullMatch_returns90Minutes() {
        // Both players are on team 1, match 1, minutes 0-90
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(90, result.totalMinutes());
        assertEquals(1, result.matchDurations().size());
        assertEquals(1L, result.matchDurations().get(0).matchId());
        assertEquals(90, result.matchDurations().get(0).minutes());
    }

    @Test
    @DisplayName("Pair with most combined minutes across multiple matches wins")
    void pairWithMostTotalMinutesAcrossMatches_isReturned() {
        // Players 10 & 20 play together in match 1 (90 min) and match 2 (45 min) = 135 total
        // Players 30 & 40 play together in match 1 (90 min) only = 90 total
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90),
                new Record(3L, 30L, 1L, 0, 90),  // different team
                new Record(4L, 40L, 1L, 0, 90),  // different team
                new Record(5L, 10L, 2L, 0, 45),
                new Record(6L, 20L, 2L, 0, 45)
        );
        Map<Long, Long> playerToTeam = Map.of(
                10L, 1L,
                20L, 1L,
                30L, 2L,
                40L, 2L
        );

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(135, result.totalMinutes());
        // The winning pair must be 10 & 20 (order may vary, so check both)
        assertTrue(
                (result.player1Id() == 10L && result.player2Id() == 20L) ||
                        (result.player1Id() == 20L && result.player2Id() == 10L)
        );
    }

    // -------------------------------------------------------------------------
    // 2. OVERLAP CALCULATION
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Partial overlap is calculated correctly")
    void partialOverlap_calculatedCorrectly() {
        // Player 10: minutes 0-60, Player 20: minutes 30-90 → overlap = 30-60 = 30 min
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 60),
                new Record(2L, 20L, 1L, 30, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(30, result.totalMinutes());
    }

    @Test
    @DisplayName("No overlap between players returns null")
    void noOverlap_returnsNull() {
        // Player 10: minutes 0-45, Player 20: minutes 45-90 → overlap = 0
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 45),
                new Record(2L, 20L, 1L, 45, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNull(result);
    }

    @Test
    @DisplayName("Null toMinutes is treated as 90")
    void nullToMinutes_treatedAs90() {
        // Player 10: 0 to null (=90), Player 20: 0 to null (=90) → overlap = 90
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, null),
                new Record(2L, 20L, 1L, 0, null)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(90, result.totalMinutes());
    }

    @Test
    @DisplayName("One player with null toMinutes overlaps correctly with explicit toMinutes")
    void oneNullToMinutes_overlapWithExplicit() {
        // Player 10: 0 to null (=90), Player 20: 45 to 90 → overlap = 45
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, null),
                new Record(2L, 20L, 1L, 45, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(45, result.totalMinutes());
    }

    // -------------------------------------------------------------------------
    // 3. TEAM ISOLATION
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Players on different teams in the same match are NOT paired together")
    void playersOnDifferentTeams_areNotPaired() {
        // Player 10 (team 1) and Player 20 (team 2) — should NOT overlap
        // Only pairing within same team is valid
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 2L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        // No valid pair exists (different teams), so result should be null
        assertNull(result);
    }

    @Test
    @DisplayName("Players from same team are paired, cross-team players are ignored")
    void mixedTeams_onlySameTeamPaired() {
        // Team 1: players 10, 20 — Team 2: players 30, 40
        // Both pairs play full match but are on separate teams
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90),
                new Record(3L, 30L, 1L, 0, 90),
                new Record(4L, 40L, 1L, 0, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(
                10L, 1L, 20L, 1L,
                30L, 2L, 40L, 2L
        );

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(90, result.totalMinutes());
        // Result must be one of the two same-team pairs
        boolean isTeam1Pair = (result.player1Id() == 10L || result.player1Id() == 20L)
                && (result.player2Id() == 10L || result.player2Id() == 20L);
        boolean isTeam2Pair = (result.player1Id() == 30L || result.player1Id() == 40L)
                && (result.player2Id() == 30L || result.player2Id() == 40L);
        assertTrue(isTeam1Pair || isTeam2Pair);
    }

    // -------------------------------------------------------------------------
    // 4. EDGE CASES
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Empty records list returns null")
    void emptyRecords_returnsNull() {
        PlayerPairResult result = analyzer.findLongestPlayingPair(List.of(), Map.of());
        assertNull(result);
    }

    @Test
    @DisplayName("Single player record returns null — no pair possible")
    void singleRecord_returnsNull() {
        List<Record> records = List.of(new Record(1L, 10L, 1L, 0, 90));
        Map<Long, Long> playerToTeam = Map.of(10L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNull(result);
    }

    @Test
    @DisplayName("Player with no team mapping is skipped")
    void playerWithNoTeamMapping_isSkipped() {
        // Player 20 has no entry in playerToTeamMap — should be ignored
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L); // 20L is missing

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNull(result);
    }

    @Test
    @DisplayName("Match durations list contains correct matchId and minutes")
    void matchDurations_containCorrectDetails() {
        List<Record> records = List.of(
                new Record(1L, 10L, 5L, 0, 90),
                new Record(2L, 20L, 5L, 0, 90)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(1, result.matchDurations().size());

        PlayerPairResult.MatchDuration duration = result.matchDurations().get(0);
        assertEquals(5L, duration.matchId());
        assertEquals(90, duration.minutes());
    }

    @Test
    @DisplayName("Three players on same team — best pair is selected")
    void threePlayersOnSameTeam_bestPairSelected() {
        // Player 10: 0-90, Player 20: 0-90, Player 30: 0-45
        // Pair 10-20: 90 min, Pair 10-30: 45 min, Pair 20-30: 45 min
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90),
                new Record(3L, 30L, 1L, 0, 45)
        );
        Map<Long, Long> playerToTeam = Map.of(10L, 1L, 20L, 1L, 30L, 1L);

        PlayerPairResult result = analyzer.findLongestPlayingPair(records, playerToTeam);

        assertNotNull(result);
        assertEquals(90, result.totalMinutes());
        assertTrue(
                (result.player1Id() == 10L && result.player2Id() == 20L) ||
                        (result.player1Id() == 20L && result.player2Id() == 10L)
        );
    }
}