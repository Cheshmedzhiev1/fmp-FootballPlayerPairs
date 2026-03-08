package com.football.fmp.outbound.adapters.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "records", indexes = {
        @Index(name = "idx_records_match_id", columnList = "matchId"),
        @Index(name = "idx_records_player_id", columnList = "playerId")
}) // records table index is a must as our main algo hits it
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;
    private Long matchId;
    private Integer fromMinutes;
    private Integer toMinutes;

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