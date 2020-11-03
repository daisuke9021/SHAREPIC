package com.sharepic.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sharepic.mapper.SharepicUserMapper;
import com.sharepic.user.User;

/**
 * テーブル「SHAREPIC_USER」に登録｜更新｜検索｜削除を行う。
 * @author daisuke
 *
 */
@Component
public class UserDao {

//	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("postgresqlConnection")
	private SqlSession sqlSession;

	/**
	 * 登録処理
	 */
	public int insert(User user) {

//		logger.debug("【開始】テーブル：SHAREPIC_USER｜処理種別：INSERT");
		System.out.println("【開始】テーブル：SHAREPIC_USER｜処理種別：INSERT");

		//登録日時・更新日時のデータ作成
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Timestamp now = Timestamp.valueOf(sdf.format(new Date()));
		user.setInsertTime(now);
		user.setUpdateTime(now);

		//マッパー取得
		SharepicUserMapper mapper = sqlSession.getMapper(SharepicUserMapper.class);


		//実行
		int result = 0;
		try {
			result = mapper.insert(user);
//			logger.debug("テーブル：SHAREPIC_USER｜処理種別：INSERT｜登録件数：" + result + "件");
			System.out.println("テーブル：SHAREPIC_USER｜処理種別：INSERT｜登録件数：" + result + "件");
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback();
//			logger.debug("テーブル：SHAREPIC_USER｜処理種別：INSERT｜登録処理に失敗しました。");
			System.out.println("テーブル：SHAREPIC_USER｜処理種別：INSERT｜登録処理に失敗しました。");

		}

//		logger.debug("【終了】テーブル：SHAREPIC_USER｜処理種別：INSERT");
		System.out.println("【終了】テーブル：SHAREPIC_USER｜処理種別：INSERT");
		return result;
	}

	/**
	 * 検索処理
	 */
	public int select(User user) {

//		logger.debug("【開始】テーブル：SHAREPIC_USER｜処理種別：COUNT");
		System.out.println("【開始】テーブル：SHAREPIC_USER｜処理種別：COUNT");

		//マッパー取得
		SharepicUserMapper mapper = sqlSession.getMapper(SharepicUserMapper.class);

		//実行
		int result = 0;
		try {
			result = mapper.count(user);
//		logger.debug("テーブル：SHAREPIC_USER｜処理種別：COUNT｜取得件数：" + result + "件");
		System.out.println("テーブル：SHAREPIC_USER｜処理種別：COUNT｜取得件数：" + result + "件");
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback();
//			logger.debug("テーブル：SHAREPIC_USER｜処理種別：COUNT｜検索処理に失敗しました。");
			System.out.println("テーブル：SHAREPIC_USER｜処理種別：COUNT｜検索処理に失敗しました。");

		}

//		logger.debug("【終了】テーブル：SHAREPIC_USER｜処理種別：COUNT");
		System.out.println("【終了】テーブル：SHAREPIC_USER｜処理種別：COUNT");

		return result;
	}

}
