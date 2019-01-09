package com.example.batchdemo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class TestParallel implements Tasklet {
	private final Logger logger = LoggerFactory.getLogger(TestParallel.class);

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
		logger.info("Parallel Task");

		return RepeatStatus.FINISHED;
	}
}
