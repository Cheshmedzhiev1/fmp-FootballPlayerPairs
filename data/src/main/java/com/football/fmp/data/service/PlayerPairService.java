package com.football.fmp.data.service;

import com.football.fmp.data.entity.PlayerEntity;
import com.football.fmp.data.entity.RecordEntity;
import com.football.fmp.data.repository.PlayerRepository;
import com.football.fmp.data.repository.RecordRepository;
import com.football.fmp.domain.model.PlayerPairResult;
import com.football.fmp.domain.model.Record;
import com.football.fmp.domain.service.PlayerPairAnalyzer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerPairService {

    private final PlayerPairAnalyzer analyzer;
    private final RecordRepository recordRepository;
    private final PlayerRepository playerRepository;

    public PlayerPairService(PlayerPairAnalyzer analyzer,
                             RecordRepository recordRepository,
                             PlayerRepository playerRepository) {
        this.analyzer = analyzer;
        this.recordRepository = recordRepository;
        this.playerRepository = playerRepository;
    }

    public PlayerPairResult findLongestPlayingPair() {

        List<RecordEntity> recordEntities = recordRepository.findAll();  // loads all records from data

        List<Record> records = recordEntities.stream()
                .map(e -> new Record(
                        e.getId(),
                        e.getPlayerId(),
                        e.getMatchId(),
                        e.getFromMinutes(),
                        e.getToMinutes()
                ))
                .collect(Collectors.toList());

        List<PlayerEntity> players = playerRepository.findAll();
        Map<Long, Long> playerToTeamMap = new HashMap<>();
        for (PlayerEntity player : players) {
            playerToTeamMap.put(player.getId(), player.getTeamId());
        }

        return analyzer.findLongestPlayingPair(records, playerToTeamMap);
    }
}