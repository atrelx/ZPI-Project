package com.zpi.amoz.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

import javax.sql.DataSource;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableRetry
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Bean
    @Retryable(
            value = { SQLException.class },
            maxAttempts = 100,
            backoff = @Backoff(delay = 2000)
    )
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(dataSourceUrl)
                .username(dataSourceUsername)
                .password(dataSourcePassword)
                .build();
    }
}