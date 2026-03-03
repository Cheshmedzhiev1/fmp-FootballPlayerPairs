package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.TeamDrivenPort;
import com.football.fmp.application.port.driving.ForTeam;
import com.football.fmp.domain.model.Team;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamApplicationService implements ForTeam {

    private final TeamDrivenPort teamDrivenPort;

    public TeamApplicationService(TeamDrivenPort teamDrivenPort) {
        this.teamDrivenPort = teamDrivenPort;
    }

    @Override
    public Team create(Team team) {
        return teamDrivenPort.save(team);
    }

    @Override
    public List<Team> findAll() {
        return teamDrivenPort.findAll();
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamDrivenPort.findById(id);
    }

    @Override
    public Team update(Long id, Team team) {
        if (!teamDrivenPort.existsById(id)) {
            return null;
        }
        Team updated = new Team(id, team.name(), team.managerFullName(), team.groupName());
        return teamDrivenPort.save(updated);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!teamDrivenPort.existsById(id)) {
            return false;
        }
        teamDrivenPort.deleteById(id);
        return true;
    }
}
