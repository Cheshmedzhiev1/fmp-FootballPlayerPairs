package com.football.fmp.data.csv;

import com.football.fmp.data.entity.MatchEntity;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MatchCSVParser {

    private static final DateTimeFormatter[] FORMATTERS = {    // supports multiple date formats
            DateTimeFormatter.ofPattern("M/d/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    };

    public static List<MatchEntity> parseMatches(Path filePath) throws IOException {
        List<String[]> records = CSVParser.parse(filePath);
        List<MatchEntity> matches = new ArrayList<>();

        for (String[] record : records) {
            if (record.length >= 5) {
                MatchEntity match = new MatchEntity();
                match.setId(Long.parseLong(record[0]));
                match.setATeamId(Long.parseLong(record[1]));
                match.setBTeamId(Long.parseLong(record[2]));
                match.setDate(parseDate(record[3]));
                match.setScore(record[4]);
                matches.add(match);
            }
        }

        return matches;
    }

    private static LocalDate parseDate(String dateStr) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);     //tries format
            } catch (Exception e) {
                // if fails, try next one
            }
        }
        throw new IllegalArgumentException("Unable to parse date: " + dateStr);
    }
}