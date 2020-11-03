package com.sharepic.dao;

import java.util.List;

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
public class PictureDaoTest {

	@Autowired
	PictureDao pictureDao;

	@Before
	public void before() {
		DBCommonUtils.openCassandraConnection();
		DBCommonUtils.openSqlSession();
	}

	@After
	public void after() {
		DBCommonUtils.closeCassandraConnection();
		DBCommonUtils.closeSqlSession();
	}

	@Test
	public void insertTest() throws Exception {

		insert1();
		insert2();
		insert3();
		insert4();

	}

//	@Test
	public void searchTest() throws Exception {
		//入力データ用意
		String query = "SELECT * FROM sharepic_keyspace.picture_store";
		List<Picture> dtoList = pictureDao.select(query);
		System.out.println(dtoList);

	}

//	@Test
	public void getTopicSequence() throws Exception {
		//５回シーケンス取得してみる
		for (int i = 0; i < 5; i++) {
			int sequence = pictureDao.getTopicSequence();
			System.out.println(sequence);
		}
	}


	private void insert1() throws Exception {
		//入力データ用意
		Picture picture1 = new Picture();
		picture1.setObjectUrl("xxxx://trip1.url");
		picture1.setPoster("makito");
		picture1.setTopic("旅行");
		picture1.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture1);

		//入力データ用意
		Picture picture2 = new Picture();
		picture2.setObjectUrl("xxxx://trip2.url");
		picture2.setPoster("makito");
		picture2.setTopic("旅行");
		picture2.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture2);

		//入力データ用意
		Picture picture3 = new Picture();
		picture3.setObjectUrl("xxxx://trip3.url");
		picture3.setPoster("makito");
		picture3.setTopic("旅行");
		picture3.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture3);

		//入力データ用意
		Picture picture4 = new Picture();
		picture4.setObjectUrl("xxxx://trip4.url");
		picture4.setPoster("makito");
		picture4.setTopic("旅行");
		picture4.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture4);
	}

	private void insert2() throws Exception {
		//入力データ用意
		Picture picture1 = new Picture();
		picture1.setObjectUrl("xxxx://camera1.url");
		picture1.setPoster("takakuwa");
		picture1.setTopic("カメラ");
		picture1.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture1);

		//入力データ用意
		Picture picture2 = new Picture();
		picture2.setObjectUrl("xxxx://camera2.url");
		picture2.setPoster("takakuwa");
		picture2.setTopic("カメラ");
		picture2.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture2);

		//入力データ用意
		Picture picture3 = new Picture();
		picture3.setObjectUrl("xxxx://camera3.url");
		picture3.setPoster("takakuwa");
		picture3.setTopic("カメラ");
		picture3.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture3);

		//入力データ用意
		Picture picture4 = new Picture();
		picture4.setObjectUrl("xxxx://camera4.url");
		picture4.setPoster("takakuwa");
		picture4.setTopic("カメラ");
		picture4.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture4);
	}
	private void insert3() throws Exception {
		//入力データ用意
		Picture picture1 = new Picture();
		picture1.setObjectUrl("xxxx://food1.url");
		picture1.setPoster("chihiro");
		picture1.setTopic("食べ物");
		picture1.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture1);

		//入力データ用意
		Picture picture2 = new Picture();
		picture2.setObjectUrl("xxxx://food2.url");
		picture2.setPoster("chihiro");
		picture2.setTopic("食べ物");
		picture2.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture2);

		//入力データ用意
		Picture picture3 = new Picture();
		picture3.setObjectUrl("xxxx://food3.url");
		picture3.setPoster("chihiro");
		picture3.setTopic("食べ物");
		picture3.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture3);

		//入力データ用意
		Picture picture4 = new Picture();
		picture4.setObjectUrl("xxxx://food4.url");
		picture4.setPoster("chihiro");
		picture4.setTopic("食べ物");
		picture4.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture4);
	}

	private void insert4() throws Exception {
		//入力データ用意
		Picture picture1 = new Picture();
		picture1.setObjectUrl("xxxx://sport1.url");
		picture1.setPoster("tamaki");
		picture1.setTopic("スポーツ");
		picture1.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture1);

		//入力データ用意
		Picture picture2 = new Picture();
		picture2.setObjectUrl("xxxx://sport2.url");
		picture2.setPoster("tamaki");
		picture2.setTopic("スポーツ");
		picture2.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture2);

		//入力データ用意
		Picture picture3 = new Picture();
		picture3.setObjectUrl("xxxx://sport3.url");
		picture3.setPoster("tamaki");
		picture3.setTopic("スポーツ");
		picture3.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture3);

		//入力データ用意
		Picture picture4 = new Picture();
		picture4.setObjectUrl("xxxx://sport4.url");
		picture4.setPoster("tamaki");
		picture4.setTopic("スポーツ");
		picture4.setCaption("This is a sample caption");
		//登録処理実行
		pictureDao.insert(picture4);
	}

}
