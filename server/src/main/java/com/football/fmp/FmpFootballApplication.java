package com.football.fmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.football.fmp.data.entity")
@EnableJpaRepositories(basePackages = "com.football.fmp.data.repository")
public class FmpFootballApplication {
    public static void main(String[] args) {
        SpringApplication.run(FmpFootballApplication.class, args);
    }
}