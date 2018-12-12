package com.example.batchdemo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDataDAO {
	int insertData(String data);

	List<String> selectAll();

	void deleteAll();
}
