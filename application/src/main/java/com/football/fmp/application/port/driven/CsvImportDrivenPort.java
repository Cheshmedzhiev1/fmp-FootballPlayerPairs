package com.football.fmp.application.port.driven;

public interface CsvImportDrivenPort {
    void importFromDirectory(String directoryPath) throws Exception;
}

  // driven ports are interfaces that our application drives other applications