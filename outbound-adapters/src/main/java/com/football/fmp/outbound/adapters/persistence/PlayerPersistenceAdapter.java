package com.football.fmp.outbound.adapters.persistence;

import com.football.fmp.application.port.driven.PlayerDrivenPort;
import com.football.fmp.domain.model.Player;
import com.football.fmp.outbound.adapters.persistence.entity.PlayerEntity;
import com.football.fmp.outbound.adapters.persistence.repository.PlayerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerPersistenceAdapter implements PlayerDrivenPort {

    private final PlayerJpaRepository playerJpaRepository;

    public PlayerPersistenceAdapter(PlayerJpaRepository playerJpaRepository) {
        this.playerJpaRepository = playerJpaRepository;
    }

    @Override
    public Player save(Player player) {
        PlayerEntity entity = toEntity(player);
        PlayerEntity saved = playerJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Player> findAll() {
        return playerJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Player> findById(Long id) {
        return playerJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return playerJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        playerJpaRepository.deleteById(id);
    }

    private PlayerEntity toEntity(Player player) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(player.id());
        entity.setTeamNumber(player.teamNumber());
        entity.setPosition(player.position());
        entity.setFullName(player.fullName());
        entity.setTeamId(player.teamId());
        return entity;
    }

    private Player toDomain(PlayerEntity entity) {
        return new Player(
                entity.getId(),
                entity.getTeamNumber(),
                entity.getPosition(),
                entity.getFullName(),
                entity.getTeamId()
        );
    }
}
