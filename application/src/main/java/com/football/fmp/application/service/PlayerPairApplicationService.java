package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.PlayerDrivenPort;
import com.football.fmp.application.port.driven.RecordDrivenPort;
import com.football.fmp.application.port.driving.ForFindLongestPlayingPair;
import com.football.fmp.domain.model.Player;
import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;
import com.football.fmp.domain.service.PlayerPairAnalyzer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerPairApplicationService implements ForFindLongestPlayingPair {

    private final RecordDrivenPort recordDrivenPort;
    private final PlayerDrivenPort playerDrivenPort;
    private final PlayerPairAnalyzer playerPairAnalyzer;

    public PlayerPairApplicationService(
            RecordDrivenPort recordDrivenPort,
            PlayerDrivenPort playerDrivenPort
    ) {
        this.recordDrivenPort = recordDrivenPort;
        this.playerDrivenPort = playerDrivenPort;
        this.playerPairAnalyzer = new PlayerPairAnalyzer();
    }

    @Override
    public PlayerPairResult findLongestPlayingPair() {
        List<Record> records = recordDrivenPort.findAll();
        List<Player> players = playerDrivenPort.findAll();

        Map<Long, Long> playerToTeamMap = new HashMap<>();
        for (Player player : players) {
            playerToTeamMap.put(player.id(), player.teamId());
        }

        return playerPairAnalyzer.findLongestPlayingPair(records, playerToTeamMap);
    }
}
