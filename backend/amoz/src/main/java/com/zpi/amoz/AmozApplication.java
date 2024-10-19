package com.zpi.amoz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableRetry
@EnableJpaRepositories(basePackages = "com.zpi.amoz.repository")
public class AmozApplication {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AmozApplication.class);
		app.run(args);
	}
}
