package com.football.fmp.outbound.adapters.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "players", indexes = {
        @Index(name = "idx_players_team_id", columnList = "teamId")
})  // players are always looked up by the team
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer teamNumber;
    private String position;
    private String fullName;
    private Long teamId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getTeamNumber() { return teamNumber; }
    public void setTeamNumber(Integer teamNumber) { this.teamNumber = teamNumber; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
}