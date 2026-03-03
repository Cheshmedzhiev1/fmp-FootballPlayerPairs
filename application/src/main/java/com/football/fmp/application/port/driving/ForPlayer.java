package com.football.fmp.application.port.driving;

import com.football.fmp.domain.model.Player;

import java.util.List;
import java.util.Optional;

public interface ForPlayer {
    Player create(Player player);
    List<Player> findAll();
    Optional<Player> findById(Long id);
    Player update(Long id, Player player);
    boolean deleteById(Long id);
}
