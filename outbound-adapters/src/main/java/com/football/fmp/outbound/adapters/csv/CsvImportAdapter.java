package com.football.fmp.outbound.adapters.csv;

import com.football.fmp.application.port.driven.CsvImportDrivenPort;
import com.football.fmp.outbound.adapters.persistence.repository.MatchJpaRepository;
import com.football.fmp.outbound.adapters.persistence.repository.PlayerJpaRepository;
import com.football.fmp.outbound.adapters.persistence.repository.RecordJpaRepository;
import com.football.fmp.outbound.adapters.persistence.repository.TeamJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CsvImportAdapter implements CsvImportDrivenPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvImportAdapter.class);

    private final TeamJpaRepository teamJpaRepository;
    private final PlayerJpaRepository playerJpaRepository;
    private final MatchJpaRepository matchJpaRepository;
    private final RecordJpaRepository recordJpaRepository;

    public CsvImportAdapter(
            TeamJpaRepository teamJpaRepository,
            PlayerJpaRepository playerJpaRepository,
            MatchJpaRepository matchJpaRepository,
            RecordJpaRepository recordJpaRepository
    ) {
        this.teamJpaRepository = teamJpaRepository;
        this.playerJpaRepository = playerJpaRepository;
        this.matchJpaRepository = matchJpaRepository;
        this.recordJpaRepository = recordJpaRepository;
    }

    @Override
    public void importFromDirectory(String directoryPath) throws Exception {
        LOGGER.info("Loading CSV data from {}", directoryPath);

        Path teamsPath = Paths.get(directoryPath, "teams.csv");
        var teams = TeamCSVParser.parseTeams(teamsPath);
        teamJpaRepository.saveAll(teams);
        LOGGER.info("Loaded {} teams", teams.size());

        Path playersPath = Paths.get(directoryPath, "players.csv");
        var players = PlayerCSVParser.parsePlayers(playersPath);
        playerJpaRepository.saveAll(players);
        LOGGER.info("Loaded {} players", players.size());

        Path matchesPath = Paths.get(directoryPath, "matches.csv");
        var matches = MatchCSVParser.parseMatches(matchesPath);
        matchJpaRepository.saveAll(matches);
        LOGGER.info("Loaded {} matches", matches.size());

        Path recordsPath = Paths.get(directoryPath, "records.csv");
        var records = RecordCSVParser.parseRecords(recordsPath);
        recordJpaRepository.saveAll(records);
        LOGGER.info("Loaded {} records", records.size());
    }
}
