package com.football.fmp.domain.model;

import java.io.Serializable;

public record Team(
        Long id,
        String name,
        String managerFullName,
        String groupName
) implements Serializable {
}