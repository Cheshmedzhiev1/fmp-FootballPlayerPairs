package com.football.fmp.domain.model;

import java.io.Serializable;
import java.util.List;

public record PlayerPairResult(
        Long player1Id,
        Long player2Id,
        int totalMinutes,
        List<MatchDuration> matchDurations
) implements Serializable {

    public record MatchDuration(
            Long matchId,
            int minutes
    ) implements Serializable {}
}