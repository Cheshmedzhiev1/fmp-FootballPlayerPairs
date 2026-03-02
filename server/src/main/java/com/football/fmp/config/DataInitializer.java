package com.football.fmp.config;

import com.football.fmp.data.service.CSVDataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CSVDataLoader csvDataLoader;

    public DataInitializer(CSVDataLoader csvDataLoader) {
        this.csvDataLoader = csvDataLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting CSV data load");

        ClassPathResource resource = new ClassPathResource("csv");
        Path csvPath = resource.getFile().toPath();

        csvDataLoader.loadAllData(csvPath.toString());

        System.out.println("CSV data is now loaded!");
    }
}