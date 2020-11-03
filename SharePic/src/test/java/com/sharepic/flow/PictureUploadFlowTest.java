package com.sharepic.flow;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharepic.TestAppConfig;
import com.sharepic.picture.Picture;
import com.sharepic.util.DBCommonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("default")
public class PictureUploadFlowTest {

	@Autowired
	PictureUploadFlow flow;

	@Before
	public void before() {
		DBCommonUtils.openCassandraConnection();
	}

	@After
	public void after() {
		DBCommonUtils.closeCassandraConnection();
	}

	@Test
	public void uploadPictureTest() throws Exception {

		//入力データ準備
		Picture picture = new Picture();
		picture.setObjectUrl("/Users/daisuke/Desktop/IMG_2567.jpeg");
		picture.setPoster("marika");
		picture.setCaption("良い感じに写真がとれました！");
		picture.setTopic("私の好きなもの");

		//実行
		boolean result = flow.uploadPicture(picture);

		//検証
		assertThat(result, is(true));

	}


}
