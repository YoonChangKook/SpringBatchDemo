package com.example.batchdemo.task;

import java.util.ArrayList;
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

public class TestReader implements Tasklet, StepExecutionListener {
	private final Logger logger = LoggerFactory.getLogger(TestReader.class);

	private Queue<String> datas;

	@Autowired
	private TestDataDAO testDataDAO;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("Test reader started.");
		this.datas = new LinkedList<>();
		this.testDataDAO.deleteAll();
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		datas.add("Test str1");
		datas.add("Test str2");
		datas.add("Test str3");
		datas.add("Test str4 !@#");
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution()
			.getExecutionContext()
			.put("datas", this.datas);

		return ExitStatus.COMPLETED;
	}
}
