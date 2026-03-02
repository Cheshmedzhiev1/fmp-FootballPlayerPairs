package com.football.fmp.data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer teamNumber;
    private String position;
    private String fullName;
    private Long teamId;

    public PlayerEntity() {}

    public PlayerEntity(Long id, Integer teamNumber, String position, String fullName, Long teamId) {
        this.id = id;
        this.teamNumber = teamNumber;
        this.position = position;
        this.fullName = fullName;
        this.teamId = teamId;
    }

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