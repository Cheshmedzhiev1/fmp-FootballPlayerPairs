package com.football.fmp.application.port.driven;

import com.football.fmp.domain.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchDrivenPort {
    Match save(Match match);
    List<Match> findAll();
    Optional<Match> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
