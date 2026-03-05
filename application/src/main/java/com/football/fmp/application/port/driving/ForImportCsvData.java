package com.football.fmp.application.port.driving;

public interface ForImportCsvData {
    void importFromDirectory(String directoryPath) throws Exception;
}
 //driving port
// implemented  by -> CsvImportAppService, called by ->DataInitializer on app startup