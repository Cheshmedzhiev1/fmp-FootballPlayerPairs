package com.football.fmp.outbound.adapters.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CSVParser {

    private CSVParser() {
    }

    public static List<String[]> parse(Path filePath) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                records.add(parseLine(line));
            }
        }

        return records;
    }

    private static String[] parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentCharacter = line.charAt(i);
            if (currentCharacter == '"') {
                insideQuotes = !insideQuotes;
            } else if (currentCharacter == ',' && !insideQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(currentCharacter);
            }
        }

        values.add(currentValue.toString().trim());
        return values.toArray(new String[0]);
    }
}
