package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.PlayerDrivenPort;
import com.football.fmp.application.port.driving.ForPlayer;
import com.football.fmp.domain.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerApplicationService implements ForPlayer {

    private final PlayerDrivenPort playerDrivenPort;

    public PlayerApplicationService(PlayerDrivenPort playerDrivenPort) {
        this.playerDrivenPort = playerDrivenPort;
    }

    @Override
    public Player create(Player player) {
        return playerDrivenPort.save(player);
    }

    @Override
    public List<Player> findAll() {
        return playerDrivenPort.findAll();
    }

    @Override
    public Optional<Player> findById(Long id) {
        return playerDrivenPort.findById(id);
    }

    @Override
    public Player update(Long id, Player player) {
        if (!playerDrivenPort.existsById(id)) {
            return null;
        }
        Player updated = new Player(id, player.teamNumber(), player.position(), player.fullName(), player.teamId());
        return playerDrivenPort.save(updated);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!playerDrivenPort.existsById(id)) {
            return false;
        }
        playerDrivenPort.deleteById(id);
        return true;
    }
}
