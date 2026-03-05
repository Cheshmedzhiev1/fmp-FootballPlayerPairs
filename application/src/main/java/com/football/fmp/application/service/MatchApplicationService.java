package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.MatchDrivenPort;
import com.football.fmp.application.port.driving.ForMatch;
import com.football.fmp.domain.model.Match;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchApplicationService implements ForMatch {

    private final MatchDrivenPort matchDrivenPort;   // application service implements the matchDrivenPort and sets its behaviour

    public MatchApplicationService(MatchDrivenPort matchDrivenPort) {
        this.matchDrivenPort = matchDrivenPort;
    }

    @Override
    public Match create(Match match) {
        return matchDrivenPort.save(match);
    }

    @Override
    public List<Match> findAll() {
        return matchDrivenPort.findAll();
    }

    @Override
    public Optional<Match> findById(Long id) {
        return matchDrivenPort.findById(id);
    }

    @Override
    public Match update(Long id, Match match) {
        if (!matchDrivenPort.existsById(id)) {
            return null;
        }
        Match updated = new Match(id, match.aTeamId(), match.bTeamId(), match.date(), match.score());
        return matchDrivenPort.save(updated);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!matchDrivenPort.existsById(id)) {
            return false;
        }
        matchDrivenPort.deleteById(id);
        return true;
    }
}
