package com.football.fmp.outbound.adapters.persistence;

import com.football.fmp.application.port.driven.MatchDrivenPort;
import com.football.fmp.domain.model.Match;
import com.football.fmp.outbound.adapters.persistence.entity.MatchEntity;
import com.football.fmp.outbound.adapters.persistence.repository.MatchJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MatchPersistenceAdapter implements MatchDrivenPort {

    private final MatchJpaRepository matchJpaRepository;

    public MatchPersistenceAdapter(MatchJpaRepository matchJpaRepository) {
        this.matchJpaRepository = matchJpaRepository;
    }

    @Override
    public Match save(Match match) {
        MatchEntity entity = toEntity(match);
        MatchEntity saved = matchJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Match> findAll() {
        return matchJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Match> findById(Long id) {
        return matchJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return matchJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        matchJpaRepository.deleteById(id);
    }

    private MatchEntity toEntity(Match match) {
        MatchEntity entity = new MatchEntity();
        entity.setId(match.id());
        entity.setATeamId(match.aTeamId());
        entity.setBTeamId(match.bTeamId());
        entity.setDate(match.date());
        entity.setScore(match.score());
        return entity;
    }

    private Match toDomain(MatchEntity entity) {
        return new Match(
                entity.getId(),
                entity.getATeamId(),
                entity.getBTeamId(),
                entity.getDate(),
                entity.getScore()
        );
    }
}
