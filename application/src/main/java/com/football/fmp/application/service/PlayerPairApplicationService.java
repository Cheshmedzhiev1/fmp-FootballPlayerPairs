package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.RecordDrivenPort;
import com.football.fmp.application.port.driving.ForFindLongestPlayingPair;
import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;
import com.football.fmp.domain.service.PlayerPairAnalyzer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerPairApplicationService implements ForFindLongestPlayingPair {

    private final RecordDrivenPort recordDrivenPort;
    private final PlayerPairAnalyzer playerPairAnalyzer;

    // true -sql query is executed ,  false -we run the analyzer algo
    private static final boolean USE_SQL_QUERY = false;

    public PlayerPairApplicationService(RecordDrivenPort recordDrivenPort) {
        this.recordDrivenPort = recordDrivenPort;
        this.playerPairAnalyzer = new PlayerPairAnalyzer();
    }

    @Override
    public List<PlayerPairResult> findLongestPlayingPair() {
        if (USE_SQL_QUERY) {
            List<PlayerPairResult> results = recordDrivenPort.findLongestPlayingPair();
            if (results == null || results.isEmpty()) {
                return List.of();
            }
            return results;
        } else {
            List<Record> records = recordDrivenPort.findAll();
            return playerPairAnalyzer.findLongestPlayingPair(records);
        }
    }
}