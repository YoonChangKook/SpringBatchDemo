package com.example.batchdemo.config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchdemo.task.TestChunkProcessor;
import com.example.batchdemo.task.TestChunkReader;
import com.example.batchdemo.task.TestChunkWriter;
import com.example.batchdemo.task.TestReader;

@Configuration
@Import(DataBaseConfig.class)
public class BatchTaskConfig {
	private static final Logger logger = LoggerFactory.getLogger(BatchTaskConfig.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("sqlTransactionManager")
	private PlatformTransactionManager transactionManager;

	@Scheduled(cron = "*/10 * * * * *")
	public void dataManagementScheduling() throws JobExecutionAlreadyRunningException, JobRestartException,
		JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		logger.info("*********dataManagementScheduling Start : " + new Date() + "*********");
		JobParameters param = new JobParametersBuilder().addString("JobID",
			String.valueOf(System.currentTimeMillis())).toJobParameters();
		jobLauncher.run(testJob(), param);
		logger.info("*********dataManagementScheduling End : " + new Date() + "*********");
	}

	@Bean
	public Job testJob() {
		return jobBuilderFactory.get("testJob")
			.start(testReadStep())
			.next(testWriteStep())
			.build();
	}

	@Bean
	public Step testReadStep() {
		return stepBuilderFactory.get("testReadStep")
			.tasklet(testReader())
			.build();
	}

	@Bean
	public Step testWriteStep() {
		return stepBuilderFactory.get("testWriteStep")
			.transactionManager(transactionManager)
			.<String, String>chunk(2)
			.faultTolerant()
			.skip(Exception.class)
			.skipLimit(10)
			.reader(testChunkReader())
			.processor(testChunkProcessor())
			.writer(testChunkWriter())
			.build();
	}

	@Bean
	public Tasklet testReader() {
		return new TestReader();
	}

	@Bean
	public ItemReader<String> testChunkReader() {
		return new TestChunkReader();
	}

	@Bean
	public ItemProcessor<String, String> testChunkProcessor() {
		return new TestChunkProcessor();
	}

	@Bean
	public ItemWriter<String> testChunkWriter() {
		return new TestChunkWriter();
	}
}
