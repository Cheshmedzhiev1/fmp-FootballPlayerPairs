package com.football.fmp.inbound.adapters.bootstrap;

import com.football.fmp.application.port.driving.ForImportCsvData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
// runs automatically when the spring  boot app starts, loads all the csv data into the database before any hhtp request arrives
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);
    private final ForImportCsvData forImportCsvData;

    public DataInitializer(ForImportCsvData forImportCsvData) {
        this.forImportCsvData = forImportCsvData;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Starting CSV data load");
        ClassPathResource resource = new ClassPathResource("csv");
        Path csvPath = resource.getFile().toPath();
        forImportCsvData.importFromDirectory(csvPath.toString());
        LOGGER.info("CSV data is now loaded");
    }
}
