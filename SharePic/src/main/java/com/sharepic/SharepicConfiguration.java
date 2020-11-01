package com.sharepic;

import org.apache.ibatis.session.SqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sharepic.util.DBCommonUtils;

@Configuration
@ComponentScan
public class SharepicConfiguration {

	@Bean(name = "SqlSession")
	public SqlSession provideConnection() {
		return DBCommonUtils.getSqlSession();
	}

}
