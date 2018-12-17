package com.example.batchdemo.task;

import java.util.LinkedList;
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

public class TestTasklet implements Tasklet, StepExecutionListener {
	private final Logger logger = LoggerFactory.getLogger(TestTasklet.class);

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
		datas.add("손근우");
		datas.add("국윤창");
		datas.add("김동철");
		datas.add("조현욱");
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
