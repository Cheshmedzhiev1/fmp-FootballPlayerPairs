package com.football.fmp.domain.model;

import java.io.Serializable;

public record Record(
        Long id,
        Long playerId,
        Long matchId,
        Integer fromMinutes,
        Integer toMinutes
) implements Serializable {
}