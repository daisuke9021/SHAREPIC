package com.sharepic;

import javax.servlet.MultipartConfigElement;

import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import com.sharepic.util.DBCommonUtils;

@Configuration
@ComponentScan("com.sharepic")
public class SharepicConfiguration {

	@Bean(name = "postgresqlConnection")
	public SqlSession providePostgresqlConnection() {
		return DBCommonUtils.getPostgresqlConnection();
	}

//	@Bean(name = "cassandraConnection")
//	public Session provideCassandraConnection() {
//		return DBCommonUtils.getCassandraConnection();
//	}

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(100));
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));
        return factory.createMultipartConfig();
    }
}
