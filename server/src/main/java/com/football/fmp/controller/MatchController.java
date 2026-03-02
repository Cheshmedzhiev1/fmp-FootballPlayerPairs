package com.football.fmp.controller;

import com.football.fmp.data.entity.MatchEntity;
import com.football.fmp.data.repository.MatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchRepository matchRepository;

    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @PostMapping
    public ResponseEntity<MatchEntity> createMatch(@RequestBody MatchEntity match) {
        MatchEntity saved = matchRepository.save(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<MatchEntity>> getAllMatches() {
        return ResponseEntity.ok(matchRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchEntity> getMatch(@PathVariable Long id) {
        return matchRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchEntity> updateMatch(@PathVariable Long id, @RequestBody MatchEntity match) {
        if (!matchRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        match.setId(id);
        return ResponseEntity.ok(matchRepository.save(match));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        if (!matchRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        matchRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}