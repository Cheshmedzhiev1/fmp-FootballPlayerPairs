package com.football.fmp.controller;

import com.football.fmp.data.entity.TeamEntity;
import com.football.fmp.data.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @PostMapping
    public ResponseEntity<TeamEntity> createTeam(@RequestBody TeamEntity team) {  //create
        TeamEntity saved = teamRepository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<TeamEntity>> getAllTeams() {   //read all
        return ResponseEntity.ok(teamRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamEntity> getTeam(@PathVariable Long id) {   //read one
        return teamRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamEntity> updateTeam(@PathVariable Long id, @RequestBody TeamEntity team) {  //update
        if (!teamRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        team.setId(id);
        return ResponseEntity.ok(teamRepository.save(team));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {   // delete
        if (!teamRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        teamRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}