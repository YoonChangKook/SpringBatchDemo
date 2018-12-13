package com.example.batchdemo.task;

import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;

public class TestChunkReader implements ItemReader<String> {
	private static final Logger logger = LoggerFactory.getLogger(TestChunkReader.class);

	private Queue<String> datas;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		logger.info("Chunk Step Started");
		this.datas = (Queue<String>)stepExecution.getJobExecution()
			.getExecutionContext()
			.get("datas");
	}

	@Override
	public String read() {
		logger.info("Chunk Read");

		return this.datas.poll();
	}
}
