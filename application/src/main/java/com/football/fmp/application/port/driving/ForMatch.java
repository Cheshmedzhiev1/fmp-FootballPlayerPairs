package com.football.fmp.application.port.driving;

import com.football.fmp.domain.model.Match;

import java.util.List;
import java.util.Optional;

public interface ForMatch {
    Match create(Match match);

    List<Match> findAll();

    Optional<Match> findById(Long id);

    Match update(Long id, Match match);

    boolean deleteById(Long id);
}

// driving port
// implemented by -> MatchAppService, TeamAppService, RecordAppService , called by -> their respective REST controller
