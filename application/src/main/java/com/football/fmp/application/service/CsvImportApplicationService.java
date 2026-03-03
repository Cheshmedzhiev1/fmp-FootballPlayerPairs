package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.CsvImportDrivenPort;
import com.football.fmp.application.port.driving.ForImportCsvData;
import org.springframework.stereotype.Service;

@Service
public class CsvImportApplicationService implements ForImportCsvData {

    private final CsvImportDrivenPort csvImportDrivenPort;

    public CsvImportApplicationService(CsvImportDrivenPort csvImportDrivenPort) {
        this.csvImportDrivenPort = csvImportDrivenPort;
    }

    @Override
    public void importFromDirectory(String directoryPath) throws Exception {
        csvImportDrivenPort.importFromDirectory(directoryPath);
    }
}
