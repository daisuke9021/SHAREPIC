package com.sharepic.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharepic.TestAppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("default")
public class S3ServiceTest {

	@Autowired
	S3Service service;

	@Test
	public void putObject() throws Exception {

		String target =  "/Users/daisuke/Desktop/IMG_2567.jpeg";

		boolean result = service.putObject(target);

		assertThat(result, is(true));

	}

}
