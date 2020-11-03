package com.sharepic;

import org.apache.ibatis.session.SqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Session;
import com.sharepic.util.DBCommonUtils;

@Configuration
@ComponentScan("com.sharepic")
public class SharepicConfiguration {

	@Bean(name = "postgresqlConnection")
	public SqlSession providePostgresqlConnection() {
		return DBCommonUtils.getPostgresqlConnection();
	}

	@Bean(name = "cassandraConnection")
	public Session provideCassandraConnection() {
		return DBCommonUtils.getCassandraConnection();
	}
}
