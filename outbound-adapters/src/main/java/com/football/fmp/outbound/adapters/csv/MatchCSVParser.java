package com.football.fmp.outbound.adapters.csv;

import com.football.fmp.outbound.adapters.persistence.entity.MatchEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class MatchCSVParser {

    private MatchCSVParser() {
    }

    private static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static List<MatchEntity> parseMatches(Path filePath) throws IOException {
        List<String[]> records = CSVParser.parse(filePath);
        List<MatchEntity> matches = new ArrayList<>();

        for (String[] record : records) {
            if (record.length >= 5) {
                MatchEntity match = new MatchEntity();
                match.setId(Long.parseLong(record[0]));
                match.setATeamId(Long.parseLong(record[1]));
                match.setBTeamId(Long.parseLong(record[2]));
                match.setDate(LocalDate.parse(record[3], CSV_DATE_FORMAT));
                match.setScore(record[4]);
                matches.add(match);
            }
        }

        return matches;
    }
}
