package com.sharepic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SharePicApplication {

	@Autowired
	SharepicStartUp startUp;

	public static void main(String[] args) {
		SpringApplication.run(SharePicApplication.class, args);
	}

}
