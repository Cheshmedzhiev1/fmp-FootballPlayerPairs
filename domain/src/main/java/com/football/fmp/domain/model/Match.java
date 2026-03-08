package com.football.fmp.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

public record Match(
        Long id,
        Long aTeamId,
        Long bTeamId,
        LocalDate date,
        String score
) implements Serializable {
}

// Serializable - meaning that this object can be converted into bytes, then reconstructed back from those bytes
// Object - > bytes , serialization
// byets -> object ,deserialization