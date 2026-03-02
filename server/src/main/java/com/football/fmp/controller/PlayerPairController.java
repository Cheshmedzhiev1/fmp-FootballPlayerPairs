package com.football.fmp.controller;

import com.football.fmp.data.service.PlayerPairService;
import com.football.fmp.domain.model.PlayerPairResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player-pairs")
public class PlayerPairController {

    private final PlayerPairService playerPairService;

    public PlayerPairController(PlayerPairService playerPairService) {
        this.playerPairService = playerPairService;
    }

    @GetMapping("/longest")
    public ResponseEntity<PlayerPairResult> getLongestPlayingPair() {
        PlayerPairResult result = playerPairService.findLongestPlayingPair();

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}