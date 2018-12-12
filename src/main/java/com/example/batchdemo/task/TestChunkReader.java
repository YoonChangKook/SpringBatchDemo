package com.example.batchdemo.task;

import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;

public class TestChunkReader implements ItemReader<String> {
	private static final Logger logger = LoggerFactory.getLogger(TestChunkReader.class);

	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		logger.info("Chunk Step Started");
		this.stepExecution = stepExecution;
	}

	@Override
	public String read() {
		logger.info("Chunk Read");
		Queue<String> datas = (Queue<String>)this.stepExecution.getJobExecution()
			.getExecutionContext()
			.get("datas");

		return datas.poll();
	}
}
