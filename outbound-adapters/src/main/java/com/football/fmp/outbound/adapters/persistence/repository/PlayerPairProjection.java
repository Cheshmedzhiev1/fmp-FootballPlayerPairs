package com.football.fmp.outbound.adapters.persistence.repository;

public interface PlayerPairProjection {
    Long getPlayer1Id();
    Long getPlayer2Id();
    Long getMatchId();
    int getMinutes();
}