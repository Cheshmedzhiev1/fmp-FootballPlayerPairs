package com.football.fmp.application.port.driving;

import com.football.fmp.domain.model.Team;

import java.util.List;
import java.util.Optional;

public interface ForTeam {
    Team create(Team team);
    List<Team> findAll();
    Optional<Team> findById(Long id);
    Team update(Long id, Team team);
    boolean deleteById(Long id);
}
