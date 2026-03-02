package com.football.fmp.data.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public static List<String[]> parse(Path filePath) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                if (isFirstLine) {    // skips header
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) { // handles empty lines
                    continue;
                }

                String[] values = parseLine(line); // splits by coma, handling quotes
                records.add(values);
            }
        }

        return records;
    }

    private static String[] parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        values.add(currentValue.toString().trim());  // adds the last value

        return values.toArray(new String[0]);
    }
}