package com.example.batchdemo.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.batchdemo.repository.TestDataDAO;

public class TestWriter implements Tasklet, StepExecutionListener {
	private final Logger logger = LoggerFactory.getLogger(TestWriter.class);

	private Queue<String> datas;

	@Autowired
	private TestDataDAO testDataDAO;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("Test writer started.");
		this.datas = (Queue<String>)stepExecution
			.getJobExecution()
			.getExecutionContext()
			.get("datas");
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws InterruptedException {
		String data = this.datas.poll();
		logger.info("INSERT !@#!@#");
		testDataDAO.insertData(data);
		Thread.sleep(5000);

//		if(data.equals("Test str3")) {
//			throw new IllegalStateException("TEST");
//		}

		if(this.datas.isEmpty()) {
			logger.info("Test writer ended.");
			return RepeatStatus.FINISHED;
		} else {
			logger.info("Test writer repeat. " + this.datas.size());
			return RepeatStatus.CONTINUABLE;
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}
}
