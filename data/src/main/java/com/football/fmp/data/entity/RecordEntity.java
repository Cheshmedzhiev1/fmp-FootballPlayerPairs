package com.football.fmp.data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "records")
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;
    private Long matchId;
    private Integer fromMinutes;
    private Integer toMinutes;

    public RecordEntity() {}

    public RecordEntity(Long id, Long playerId, Long matchId, Integer fromMinutes, Integer toMinutes) {
        this.id = id;
        this.playerId = playerId;
        this.matchId = matchId;
        this.fromMinutes = fromMinutes;
        this.toMinutes = toMinutes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public Integer getFromMinutes() { return fromMinutes; }
    public void setFromMinutes(Integer fromMinutes) { this.fromMinutes = fromMinutes; }

    public Integer getToMinutes() { return toMinutes; }
    public void setToMinutes(Integer toMinutes) { this.toMinutes = toMinutes; }
}