package com.football.fmp.application.port.driven;

import com.football.fmp.domain.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDrivenPort {
    Player save(Player player);
    List<Player> findAll();
    Optional<Player> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
