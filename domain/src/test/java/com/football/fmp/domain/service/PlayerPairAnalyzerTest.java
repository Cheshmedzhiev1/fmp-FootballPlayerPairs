package com.football.fmp.domain.service;

import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerPairAnalyzerTest {

    private PlayerPairAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new PlayerPairAnalyzer();
    }

    @Test
    @DisplayName("Two players who played the full match together return 90 minutes overlap")
    void twoPlayersFullMatch_returns90Minutes() {
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90)
        );

        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(records);

        assertFalse(results.isEmpty());
        assertEquals(90, results.get(0).totalMinutes());
    }

    @Test
    @DisplayName("Pair with most combined minutes across multiple matches wins")
    void pairWithMostTotalMinutesAcrossMatches_isReturned() {

        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90),
                new Record(3L, 10L, 2L, 0, 45),
                new Record(4L, 20L, 2L, 0, 45)
        );

        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(records);

        assertFalse(results.isEmpty());
        assertEquals(135, results.get(0).totalMinutes());
        assertTrue(
                (results.get(0).player1Id() == 10L && results.get(0).player2Id() == 20L) ||
                        (results.get(0).player1Id() == 20L && results.get(0).player2Id() == 10L)
        );
    }

    @Test
    @DisplayName("Partial overlap is calculated correctly")
    void partialOverlap_calculatedCorrectly() {
        // Player 10: 0-60, Player 20: 30-90 → overlap = 30 min
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 60),
                new Record(2L, 20L, 1L, 30, 90)
        );

        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(records);

        assertFalse(results.isEmpty());
        assertEquals(30, results.get(0).totalMinutes());
    }

    @Test
    @DisplayName("No overlap between players returns empty list")
    void noOverlap_returnsEmptyList() {
        // Player 10: 0-45, Player 20: 45-90 → overlap = 0
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 45),
                new Record(2L, 20L, 1L, 45, 90)
        );

        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(records);

        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Null toMinutes is treated as 90")
    void nullToMinutes_treatedAs90() {
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, null),
                new Record(2L, 20L, 1L, 0, null)
        );

        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(records);

        assertFalse(results.isEmpty());
        assertEquals(90, results.get(0).totalMinutes());
    }

    @Test
    @DisplayName("Empty records list returns empty list")
    void emptyRecords_returnsEmptyList() {
        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(List.of());
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Single player record returns empty list — no pair possible")
    void singleRecord_returnsEmptyList() {
        List<Record> records = List.of(new Record(1L, 10L, 1L, 0, 90));

        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(records);

        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Three players — best pair is selected")
    void threePlayersOnSameTeam_bestPairSelected() {
        // Pair 10-20: 90 min, Pair 10-30: 45 min, Pair 20-30: 45 min
        List<Record> records = List.of(
                new Record(1L, 10L, 1L, 0, 90),
                new Record(2L, 20L, 1L, 0, 90),
                new Record(3L, 30L, 1L, 0, 45)
        );

        List<PlayerPairResult> results = analyzer.findLongestPlayingPair(records);

        assertFalse(results.isEmpty());
        assertEquals(90, results.get(0).totalMinutes());
        assertTrue(
                (results.get(0).player1Id() == 10L && results.get(0).player2Id() == 20L) ||
                        (results.get(0).player1Id() == 20L && results.get(0).player2Id() == 10L)
        );
    }
}