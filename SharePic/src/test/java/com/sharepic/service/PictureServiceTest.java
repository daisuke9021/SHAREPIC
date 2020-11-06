package com.sharepic.service;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharepic.SharepicConfiguration;
import com.sharepic.picture.Picture;
import com.sharepic.util.DBCommonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SharepicConfiguration.class)
//@ActiveProfiles("default")
public class PictureServiceTest {

	@Autowired
	PictureService service;

	@Before
	public void before() {
		DBCommonUtils.openCassandraConnection();
	}

	@After
	public void after() {
		DBCommonUtils.closeCassandraConnection();
	}

	@Test
	public void getHomePicturesTest() throws Exception {
		//入力データなし
		//実行
		Map<String,List<Picture>> pictureMap = service.getHomePictures();

		for(Map.Entry<String,List<Picture>> entry : pictureMap.entrySet()) {
			System.out.println("トピック名：" + entry.getKey());
			for(Picture picture : entry.getValue()) {
				System.out.println(picture);
			}
 		}
	}

	/**
	 * 条件：トピック名
	 */
//	@Test
	public void selectByTopicTest() throws Exception {
		//検索条件
		String targetTopic = "食べ物";
		//実行
		List<Picture> pictureList = service.getPictureByTopic(targetTopic);

		for(Picture picture : pictureList) {
			System.out.println(picture);
		}
	}

	/**
	 * 条件：投稿者
	 */
//	@Test
	public void selectByPosterTest() throws Exception {
		//検索条件
		String targetPoster = "takakuwa";
		//実行
		List<Picture> pictureList = service.getPictureByPoster(targetPoster);

		for(Picture picture : pictureList) {
			System.out.println(picture);
		}
	}




}
