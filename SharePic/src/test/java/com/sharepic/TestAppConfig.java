package com.sharepic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Session;
import com.sharepic.util.DBCommonUtils;

@Configuration
@ComponentScan("com.sharepic")
public class TestAppConfig {

	//テスト時に@Autowiredクラスをここに書く必要があるってことか
	@Bean(name = "cassandraConnection")
	public Session provideCassandraConnection() {
		return DBCommonUtils.getCassandraConnection();
	}
}
