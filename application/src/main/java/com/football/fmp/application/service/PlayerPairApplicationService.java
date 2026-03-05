package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.RecordDrivenPort;
import com.football.fmp.application.port.driving.ForFindLongestPlayingPair;
import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;
import com.football.fmp.domain.service.PlayerPairAnalyzer;
import org.springframework.stereotype.Service;

import java.util.List;
// loads all the records from the database via RecordDrivenPort, loads all players via PlayerDrivenPort,
// builds a map of playerid -> teamid , then calls PlayerPairAnalyzer
// the analyzer needs to know which team each player belongs to so it only pairs teammates

@Service
public class PlayerPairApplicationService implements ForFindLongestPlayingPair {

    private final RecordDrivenPort recordDrivenPort;
    private final PlayerPairAnalyzer playerPairAnalyzer;

    public PlayerPairApplicationService(RecordDrivenPort recordDrivenPort) {
        this.recordDrivenPort = recordDrivenPort;
        this.playerPairAnalyzer = new PlayerPairAnalyzer();
    }

    @Override
    public PlayerPairResult findLongestPlayingPair() {
        List<Record> records = recordDrivenPort.findAll();
        return playerPairAnalyzer.findLongestPlayingPair(records);
    }
}