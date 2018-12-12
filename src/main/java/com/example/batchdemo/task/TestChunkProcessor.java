package com.example.batchdemo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class TestChunkProcessor implements ItemProcessor<String, String> {
	private static Logger logger = LoggerFactory.getLogger(TestChunkProcessor.class);

	@Override
	public String process(String item) {
		logger.info("Chunk Processor");
		return item;
	}
}
