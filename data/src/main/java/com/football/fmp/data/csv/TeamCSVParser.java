package com.football.fmp.data.csv;

import com.football.fmp.data.entity.TeamEntity;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TeamCSVParser {

    public static List<TeamEntity> parseTeams(Path filePath) throws IOException {
        List<String[]> records = CSVParser.parse(filePath);
        List<TeamEntity> teams = new ArrayList<>();

        for (String[] record : records) {
            if (record.length >= 4) {
                TeamEntity team = new TeamEntity();
                team.setId(Long.parseLong(record[0]));
                team.setName(record[1]);
                team.setManagerFullName(record[2]);
                team.setGroupName(record[3]);
                teams.add(team);
            }
        }

        return teams;
    }
}