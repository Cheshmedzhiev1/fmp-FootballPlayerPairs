package com.football.fmp.data.csv;

import com.football.fmp.data.entity.RecordEntity;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RecordCSVParser {

    public static List<RecordEntity> parseRecords(Path filePath) throws IOException {
        List<String[]> records = CSVParser.parse(filePath);
        List<RecordEntity> recordEntities = new ArrayList<>();

        for (String[] record : records) {
            if (record.length >= 5) {
                RecordEntity recordEntity = new RecordEntity();
                recordEntity.setId(Long.parseLong(record[0]));
                recordEntity.setPlayerId(Long.parseLong(record[1]));
                recordEntity.setMatchId(Long.parseLong(record[2]));
                recordEntity.setFromMinutes(Integer.parseInt(record[3]));

                String toMinutes = record[4];
                if (toMinutes.equalsIgnoreCase("NULL") || toMinutes.isEmpty()) {
                    recordEntity.setToMinutes(90);
                } else {
                    recordEntity.setToMinutes(Integer.parseInt(toMinutes));
                }

                recordEntities.add(recordEntity);
            }
        }

        return recordEntities;
    }
}