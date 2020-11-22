package com.sharepic.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sharepic.mapper.SharepicPictureStoreMapper;
import com.sharepic.picture.Picture;
import com.sharepic.service.PictureService;

/**
 * テーブル「PICTURE_STORE」に登録｜更新｜検索｜削除を行う。
 * @author daisuke
 *
 */
@Component
public class PictureDao {

//	@Autowired
//	@Qualifier("cassandraConnection")
//	Session cassandraSession;

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

		//マッパー取得
		SharepicPictureStoreMapper mapper = postgresqlSession.getMapper(SharepicPictureStoreMapper.class);

		//登録日時・更新日時のデータ作成
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Timestamp now = Timestamp.valueOf(sdf.format(new Date()));
		picture.setInsertDate(now.toString());
		picture.setUpdateDate(now.toString());

		System.out.println("ーーーーーーーーーーーーーーー");
		System.out.println("登録データ：");
		System.out.println(picture.toString());
		System.out.println("ーーーーーーーーーーーーーーー");
		int result = 0;

		//実行
		try {
			result = mapper.insert(picture);

//			logger.debug("テーブル：PICTURE_STORE｜処理種別：INSERT｜登録処理に成功しました。");
			System.out.println("テーブル：PICTURE_STORE｜処理種別：INSERT｜登録処理に成功しました。");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("テーブル：PICTURE_STORE｜処理種別：INSERT｜登録処理に失敗しました。");
		}

		if(result == 0) {
			System.out.println("登録に失敗しています....");
		} else {
			postgresqlSession.commit();
			System.out.println("登録に成功しました！");
		}

//		logger.debug("【終了】テーブル：PICTURE_STORE｜処理種別：INSERT");
		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：PICTURE_STORE｜処理種別：INSERT↑↑↑↑↑↑↑");

	}


	/**
	 * 検索処理
	 */
	public List<Picture> select(Picture picture) throws Exception {

//		logger.debug("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：SEARCH↓↓↓↓↓↓↓");
		System.out.println("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：SEARCH↓↓↓↓↓↓↓");

		//マッパー取得
		SharepicPictureStoreMapper mapper = postgresqlSession.getMapper(SharepicPictureStoreMapper.class);

		List<Picture> pictureList = new ArrayList<>();

		//実行
		try {
			pictureList = mapper.select(picture);
			System.out.println("ーーーーーーーーーーーーーーー");
			System.out.println("検索結果");
			for(Picture picItem : pictureList) {
				System.out.println(picItem);
			}
			System.out.println("ーーーーーーーーーーーーーーー");
			System.out.println("テーブル：PICTURE_STORE｜処理種別：SEARCH｜取得件数：" + pictureList.size() + "件");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("テーブル：PICTURE_STORE｜処理種別：SEARCH｜検索処理に失敗しました。");
		}

		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：PICTURE_STORE｜処理種別：SEARCH↑↑↑↑↑↑↑");

		return pictureList;
	}


	/**
	 * トピック一覧取得処理
	 */
	public List<String> getTopicList() throws Exception {

//		logger.debug("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：SEARCH↓↓↓↓↓↓↓");
		System.out.println("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：SEARCH↓↓↓↓↓↓↓");

		//マッパー取得
		SharepicPictureStoreMapper mapper = postgresqlSession.getMapper(SharepicPictureStoreMapper.class);

		List<String> topicList = new ArrayList<>();

		//実行
		try {
			topicList = mapper.getTopicList();

			System.out.println("テーブル：PICTURE_STORE｜処理種別：SEARCH｜取得件数：" + topicList.size() + "件");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("テーブル：PICTURE_STORE｜処理種別：SEARCH｜検索処理に失敗しました。");
		}

		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：PICTURE_STORE｜処理種別：SEARCH↑↑↑↑↑↑↑");

		return topicList;
	}


	/**
	 * 削除処理
	 * @param condPic
	 * @return
	 * @throws Exception
	 */
	public int delete(Picture condPic) throws Exception {

		System.out.println("↓↓↓↓↓↓↓【開始】テーブル：PICTURE_STORE｜処理種別：DELETE↓↓↓↓↓↓↓");
		//マッパー取得
		SharepicPictureStoreMapper mapper = postgresqlSession.getMapper(SharepicPictureStoreMapper.class);

		int result = 0;
		try {
			result = mapper.delete(condPic);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("テーブル：PICTURE_STORE｜処理種別：DELETE｜削除処理に失敗しました。");
		}

		if(result == 0) {
			System.out.println("削除に失敗しています....");
		} else {
			postgresqlSession.commit();
			System.out.println("削除に成功しました！");
		}

		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：PICTURE_STORE｜処理種別：DELETE↑↑↑↑↑↑↑");
		return result;
	}

//	/**
//	 *  トピックIDに埋め込むトピックシーケンスの取得
//	 */
//	public int getTopicSequence() {
//
////		logger.debug("【開始】テーブル：TOPIC_SEQUENCE｜処理種別：SELECT");
//		System.out.println("↓↓↓↓↓↓↓【開始】テーブル：TOPIC_SEQUENCE｜処理種別：SELECT↓↓↓↓↓↓↓");
//
//		//マッパー取得
//		SharepicTopicSequenceMapper mapper = postgresqlSession.getMapper(SharepicTopicSequenceMapper.class);
//
//		//戻り値
//		int topicSequence = 0;
//
//		try {
//			topicSequence = mapper.getTopicSequence();
//			System.out.println("テーブル：TOPIC_SEQUENCE｜処理種別：SELECT｜シーケンス番号：" + topicSequence);
//			postgresqlSession.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			postgresqlSession.rollback();
//			System.out.println("テーブル：TOPIC_SEQUENCE｜処理種別：SELECT｜シーケンス取得処理に失敗しました。");
//		}
//
//		System.out.println("↑↑↑↑↑↑↑【終了】テーブル：TOPIC_SEQUENCE｜処理種別：SELECT↑↑↑↑↑↑↑");
//
//		return topicSequence;
//	}

}
