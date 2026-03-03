package com.football.fmp.outbound.adapters.persistence;

import com.football.fmp.application.port.driven.TeamDrivenPort;
import com.football.fmp.domain.model.Team;
import com.football.fmp.outbound.adapters.persistence.entity.TeamEntity;
import com.football.fmp.outbound.adapters.persistence.repository.TeamJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TeamPersistenceAdapter implements TeamDrivenPort {

    private final TeamJpaRepository teamJpaRepository;

    public TeamPersistenceAdapter(TeamJpaRepository teamJpaRepository) {
        this.teamJpaRepository = teamJpaRepository;
    }

    @Override
    public Team save(Team team) {
        TeamEntity entity = toEntity(team);
        TeamEntity saved = teamJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Team> findAll() {
        return teamJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return teamJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        teamJpaRepository.deleteById(id);
    }

    private TeamEntity toEntity(Team team) {
        TeamEntity entity = new TeamEntity();
        entity.setId(team.id());
        entity.setName(team.name());
        entity.setManagerFullName(team.managerFullName());
        entity.setGroupName(team.groupName());
        return entity;
    }

    private Team toDomain(TeamEntity entity) {
        return new Team(
                entity.getId(),
                entity.getName(),
                entity.getManagerFullName(),
                entity.getGroupName()
        );
    }
}
