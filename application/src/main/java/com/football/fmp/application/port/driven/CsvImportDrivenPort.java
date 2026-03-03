package com.football.fmp.application.port.driven;

public interface CsvImportDrivenPort {
    void importFromDirectory(String directoryPath) throws Exception;
}
