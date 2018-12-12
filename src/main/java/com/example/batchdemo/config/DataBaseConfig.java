package com.example.batchdemo.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.example.batchdemo.repository", sqlSessionFactoryRef = "sqlSessionFactory")
@PropertySource("classpath:application.properties")
public class DataBaseConfig {
	private static final Logger logger = LoggerFactory.getLogger(DataBaseConfig.class);

	@Bean
	@ConfigurationProperties(prefix = "spring.test.datasource")
	public DataSource dataSource() {
		logger.debug("datasource Initialized.");
		return DataSourceBuilder.create().build();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		sqlSessionFactoryBean.setTypeAliasesPackage("com.example.batchdemo.domain");
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));

		org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
		mybatisConfig.setCacheEnabled(true);
		mybatisConfig.setLazyLoadingEnabled(true);
		mybatisConfig.setAggressiveLazyLoading(true);
		mybatisConfig.setDefaultExecutorType(ExecutorType.REUSE);
		mybatisConfig.setJdbcTypeForNull(JdbcType.NULL);
		sqlSessionFactoryBean.setConfiguration(mybatisConfig);

		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public PlatformTransactionManager sqlTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
