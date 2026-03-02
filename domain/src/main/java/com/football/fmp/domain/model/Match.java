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