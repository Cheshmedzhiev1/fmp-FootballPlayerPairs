package com.football.fmp.data.csv;

import com.football.fmp.data.entity.PlayerEntity;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlayerCSVParser {

    public static List<PlayerEntity> parsePlayers(Path filePath) throws IOException {
        List<String[]> records = CSVParser.parse(filePath);
        List<PlayerEntity> players = new ArrayList<>();

        for (String[] record : records) {
            if (record.length >= 5) {
                PlayerEntity player = new PlayerEntity();
                player.setId(Long.parseLong(record[0]));
                player.setTeamNumber(Integer.parseInt(record[1]));
                player.setPosition(record[2]);
                player.setFullName(record[3]);
                player.setTeamId(Long.parseLong(record[4]));
                players.add(player);
            }
        }

        return players;
    }
}