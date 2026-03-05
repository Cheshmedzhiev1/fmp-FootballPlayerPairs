package com.football.fmp.inbound.adapters.rest;

import com.football.fmp.application.port.driving.ForFindLongestPlayingPair;
import com.football.fmp.application.port.driving.ForPlayer;
import com.football.fmp.domain.model.Player;
import com.football.fmp.domain.model.PlayerPairResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final ForPlayer forPlayer;

    private final ForFindLongestPlayingPair forFindLongestPlayingPair;

    public PlayerController(ForPlayer forPlayer, ForFindLongestPlayingPair forFindLongestPlayingPair) {
        this.forPlayer = forPlayer;
        this.forFindLongestPlayingPair = forFindLongestPlayingPair;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forPlayer.create(player));
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(forPlayer.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        return forPlayer.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        Player updated = forPlayer.update(id, player);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        if (!forPlayer.deleteById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/longest")
    public ResponseEntity<PlayerPairResult> getLongestPlayingPair() {
        PlayerPairResult result = forFindLongestPlayingPair.findLongestPlayingPair();
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
