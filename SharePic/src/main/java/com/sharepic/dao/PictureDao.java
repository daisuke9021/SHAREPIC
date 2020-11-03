package com.sharepic.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.sharepic.mapper.SharepicTopicSequenceMapper;
import com.sharepic.picture.Picture;
import com.sharepic.service.PictureService;

/**
 * テーブル「PICTURE_STORE」に登録｜更新｜検索｜削除を行う。
 * @author daisuke
 *
 */
@Component
public class PictureDao {

	@Autowired
	@Qualifier("cassandraConnection")
	Session cassandraSession;

	@Autowired
	@Qualifier("postgresqlConnection")
	SqlSession postgresqlSession;

	@Autowired
	PictureService pictureService;

	/**
	 * 登録処理
	 */
	public void insert(Picture picture) throws Exception {

//		logger.debug("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：INSERT↓↓↓↓↓↓↓");
		System.out.println("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：INSERT↓↓↓↓↓↓↓");

		//トピック一覧の取得
		Map<String,String> topicMap = pictureService.getTopicList();
		String topic_id = null;
		//登録済のトピックかどうか判定
		boolean isTopicRegistered = false;
		for(Map.Entry<String,String> entry : topicMap.entrySet()) {
			//登録済のトピックであればトピックIDを控える
			if(entry.getValue().equals(picture.getTopic())) {
				topic_id = entry.getKey();
				//ここでトピックIDも控えちゃうのがベスト
				isTopicRegistered = true;
			}
		}
		//未登録のトピックの場合、新しく作成する
		if(!isTopicRegistered) {
			topic_id = "topicIdxxx" + getTopicSequence();
		}
		picture.setTopicId(topic_id);

		//登録日時・更新日時のデータ作成
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Timestamp now = Timestamp.valueOf(sdf.format(new Date()));
		picture.setInsertTime(now.toString());
		picture.setUpdateTime(now.toString());

		//マッパー取得
		Mapper<Picture> mapper = getCassandraMapper(Picture.class);

		//実行
		try {
			mapper.save(picture);
//			logger.debug("テーブル：PICTURE_STORE｜処理種別：INSERT｜登録処理に成功しました。");
			System.out.println("テーブル：PICTURE_STORE｜処理種別：INSERT｜登録処理に成功しました。");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("テーブル：PICTURE_STORE｜処理種別：INSERT｜登録処理に失敗しました。");
		}

//		logger.debug("【終了】テーブル：PICTURE_STORE｜処理種別：INSERT");
		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：PICTURE_STORE｜処理種別：INSERT↑↑↑↑↑↑↑");

	}


	/**
	 * 検索処理
	 */
	public List<Picture> select(String query) throws Exception {

//		logger.debug("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：SEARCH↓↓↓↓↓↓↓");
		System.out.println("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：SEARCH↓↓↓↓↓↓↓");
		List<Picture> pictureList = new ArrayList<>();

		//マッパー取得
		Mapper<Picture> mapper = getCassandraMapper(Picture.class);

		//実行
		try {
			ResultSet results = cassandraSession.execute(query);
			pictureList = mapper.map(results).all();
			System.out.println("テーブル：PICTURE_STORE｜処理種別：SEARCH｜取得件数：" + pictureList.size() + "件");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("テーブル：PICTURE_STORE｜処理種別：SEARCH｜検索処理に失敗しました。");
		}

		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：PICTURE_STORE｜処理種別：SEARCH↑↑↑↑↑↑↑");

		return pictureList;
	}

	/**
	 *  トピックIDに埋め込むトピックシーケンスの取得
	 */
	public int getTopicSequence() {

//		logger.debug("【開始】テーブル：TOPIC_SEQUENCE｜処理種別：SELECT");
		System.out.println("↓↓↓↓↓↓↓【開始】テーブル：TOPIC_SEQUENCE｜処理種別：SELECT↓↓↓↓↓↓↓");

		//マッパー取得
		SharepicTopicSequenceMapper mapper = postgresqlSession.getMapper(SharepicTopicSequenceMapper.class);

		//戻り値
		int topicSequence = 0;

		try {
			topicSequence = mapper.getTopicSequence();
			System.out.println("テーブル：TOPIC_SEQUENCE｜処理種別：SELECT｜シーケンス番号：" + topicSequence);
			postgresqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			postgresqlSession.rollback();
			System.out.println("テーブル：TOPIC_SEQUENCE｜処理種別：SELECT｜シーケンス取得処理に失敗しました。");
		}

		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：TOPIC_SEQUENCE｜処理種別：SELECT↑↑↑↑↑↑↑");

		return topicSequence;
	}

	/**
	 * マッパー取得
	 */
	public Mapper getCassandraMapper(Class clazz) throws Exception {
		MappingManager manager = new MappingManager(cassandraSession);
		Mapper mapper = manager.mapper(clazz);
		return mapper;
	}

}
