package com.football.fmp.inbound.adapters.rest;

import com.football.fmp.application.port.driving.ForFindLongestPlayingPair;
import com.football.fmp.domain.model.PlayerPairResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player-pairs")
public class PlayerPairController {

    private final ForFindLongestPlayingPair forFindLongestPlayingPair;

    public PlayerPairController(ForFindLongestPlayingPair forFindLongestPlayingPair) {
        this.forFindLongestPlayingPair = forFindLongestPlayingPair;
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
