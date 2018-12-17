package com.example.batchdemo.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.batchdemo.repository.TestDataDAO;

public class TestChunkWriter implements ItemWriter<String> {
	private static final Logger logger = LoggerFactory.getLogger(TestChunkWriter.class);

	@Autowired
	private TestDataDAO testDataDAO;

	@Override
	public void write(List<? extends String> list) {
		logger.info("Chunk Write Count: {}", list.size());
		for(String data : list) {
			testDataDAO.insertData(data);
//			if(data.equals("조현욱")) {
//				throw new IllegalStateException(data + " Exception");
//			}
		}
	}
}
