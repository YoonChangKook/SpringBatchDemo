package com.example.batchdemo.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.AbstractBatchConfiguration;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class SpringBatchConfig {
	private static final Logger logger = LoggerFactory.getLogger(SpringBatchConfig.class);

	@Bean
	public BatchConfigurer batchConfigurer() {
		return new DefaultBatchConfigurer(batchDataSource());
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.test.batch.datasource")
	public DataSource batchDataSource() {
		logger.debug("batch datasource Initialized.");
		return DataSourceBuilder.create().build();
	}
}
