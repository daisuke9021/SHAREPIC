package com.sharepic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sharepic.dao.PictureDao;
import com.sharepic.picture.Picture;
import com.sharepic.util.PropertyUtils;

@Component
public class PictureService {

	@Autowired
	PictureDao pictureDao;

	/**
	 * トピック一覧(topic,topic_id)を取得する
	 * @return topicList トピック一覧
	 * @throws Exception
	 */
	public Map<String,String> getTopicList() throws Exception {
		List<Picture> pictureList = selectByCondition("selectTopic","");
		Map<String,String> topicMap = new HashMap<>();
		for(Picture picture : pictureList) {
			//重複するデータがないように制御する
			if(!topicMap.containsKey(picture.getTopicId())) {
				topicMap.put(picture.getTopicId(),picture.getTopic());
			}
		}
		return topicMap;
	}

	/**
	 * ホーム画面に表示する写真情報を取得する
	 * @return pictureMap
	 * @throws Exception
	 */
	public Map<String,List<Picture>> getHomePictures() throws Exception {
		//全データ取得
		List<Picture> allPictureList = selectByCondition("selectAll","");
		//3つのトピック選定
		Set<String> topicSet = new LinkedHashSet<>();
		for (Map.Entry<String,String> entry : getTopicList().entrySet()) {
			topicSet.add(entry.getValue());
			if(topicSet.size() == 3) {
				break;
			}
		}
		//トピックに紐つく写真取得
		Map<String,List<Picture>> pictureMap = new HashMap<>();
		for(String topic : topicSet) {
			List<Picture> pictureListByTopic = new ArrayList<>();
			for(Picture picture : allPictureList) {
				if(topic.equals(picture.getTopic())) {
					pictureListByTopic.add(picture);
				}
			}
			pictureMap.put(topic, pictureListByTopic);
		}

		return pictureMap;
	}

	/**
	 * 引数のトピック名に紐づく写真リストを取得する。
	 */
	public List<Picture> getPictureByTopic(String targetTopic) throws Exception {
		//全データ取得
		List<Picture> allPictureList = selectByCondition("selectAll","");
		//条件に該当するデータを抽出
		List<Picture> filteredPictureList = new ArrayList<>();
		for(Picture picture : allPictureList) {
			if(targetTopic.equals(picture.getTopic())) {
				filteredPictureList.add(picture);
			}
		}

		return filteredPictureList;
	}

	/**
	 * 引数の投稿者名に紐づく写真リストを取得する。
	 */
	public List<Picture> getPictureByPoster(String targetPoster) throws Exception {
		//全データ取得
		List<Picture> allPictureList = selectByCondition("selectAll","");
		//条件に該当するデータを抽出
		List<Picture> filteredPictureList = new ArrayList<>();
		for(Picture picture : allPictureList) {
			if(targetPoster.equals(picture.getPoster())) {
				filteredPictureList.add(picture);
			}
		}

		return filteredPictureList;
	}

	/**
	 * PICUTRE_STORE検索処理
	 * ・第1引数に指定されたSQLに、第2引数に指定された検索条件値を埋め込み、SQLを実行する。
	 * @param sqlName SQL名
	 * @param param 検索条件の値
	 * @return pictureList 検索結果｜リスト形式でPictureオブジェクトが格納されている
	 * @throws Exception
	 */
	public List<Picture> selectByCondition(String sqlName, String param) throws Exception {

		//プロパティファイルからSQL文を取得する
		String query = PropertyUtils.getProperties("cassandraSql").getString(sqlName);
		//条件値を埋め込む
		if (checkSqlName(sqlName)) {
			query = query.replace("#", param);
		}
		System.out.println("実行クエリ：" + query);
		//SQL実行メソッド呼び出し
		List<Picture> pictureList = pictureDao.select(query);

		return pictureList;
	}

	/**
	 * 検索条件の値をSQL文に埋め込むかどうかのチェック
	 * @param sqlName
	 * @return
	 */
	private boolean checkSqlName(String sqlName) {
		boolean hasConditionParam = true;

		if(sqlName.equals("selectTopic") && sqlName.equals("select100")) {
			hasConditionParam = false;
		}

		return hasConditionParam;
	}

}
