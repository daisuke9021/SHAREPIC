package com.sharepic.flow;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharepic.picture.Picture;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
public class PictureUploadFlowTest {

	@Autowired
	PictureUploadFlow flow;

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
