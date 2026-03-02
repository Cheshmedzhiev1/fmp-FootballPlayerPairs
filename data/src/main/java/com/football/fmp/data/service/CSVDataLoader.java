package com.football.fmp.data.service;

import com.football.fmp.data.csv.*;
import com.football.fmp.data.entity.*;
import com.football.fmp.data.repository.*;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CSVDataLoader {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final RecordRepository recordRepository;

    public CSVDataLoader(TeamRepository teamRepository,
                         PlayerRepository playerRepository,
                         MatchRepository matchRepository,
                         RecordRepository recordRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.recordRepository = recordRepository;
    }

    public void loadAllData(String csvDirectoryPath) throws Exception {
        System.out.println("Loading CSV data from: " + csvDirectoryPath);

        Path teamsPath = Paths.get(csvDirectoryPath, "teams.csv");    //load the teams
        List<TeamEntity> teams = TeamCSVParser.parseTeams(teamsPath);
        teamRepository.saveAll(teams);
        System.out.println("Loaded " + teams.size() + " teams");

        Path playersPath = Paths.get(csvDirectoryPath, "players.csv");  // load  the players
        List<PlayerEntity> players = PlayerCSVParser.parsePlayers(playersPath);
        playerRepository.saveAll(players);
        System.out.println("Loaded " + players.size() + " players");

        Path matchesPath = Paths.get(csvDirectoryPath, "matches.csv");  //load the matches
        List<MatchEntity> matches = MatchCSVParser.parseMatches(matchesPath);
        matchRepository.saveAll(matches);
        System.out.println("Loaded " + matches.size() + " matches");

        Path recordsPath = Paths.get(csvDirectoryPath, "records.csv");  // load t he records
        List<RecordEntity> records = RecordCSVParser.parseRecords(recordsPath);
        recordRepository.saveAll(records);
        System.out.println("Loaded " + records.size() + " records");

        System.out.println("All the CSV data is now loaded!");
    }
}