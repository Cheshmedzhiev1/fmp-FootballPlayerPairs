package com.football.fmp.application.port.driven;

import com.football.fmp.domain.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDrivenPort {
    Team save(Team team);
    List<Team> findAll();
    Optional<Team> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
