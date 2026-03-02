package com.football.fmp.domain.model;

import java.io.Serializable;

public record Player(
        Long id,
        Integer teamNumber,
        String position,
        String fullName,
        Long teamId
) implements Serializable {
}