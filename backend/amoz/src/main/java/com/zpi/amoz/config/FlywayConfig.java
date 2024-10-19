package com.zpi.amoz.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

import java.sql.SQLException;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

import java.sql.SQLException;

@Configuration
@EnableRetry
public class FlywayConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Bean(initMethod = "migrate")
    @Retryable(
            maxAttempts = 100,
            backoff = @Backoff(delay = 2000)
    )
    public Flyway flyway() throws Exception {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSourceUrl, dataSourceUsername, dataSourcePassword)
                    .baselineOnMigrate(true)
                    .load();
            flyway.migrate();
            return flyway;
        } catch (Exception e) {
            System.err.println("Could not establish connection with database" + e.getMessage());
            throw e;
        }
    }
}
