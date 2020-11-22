package com.sharepic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sharepic.dao.PictureDao;
import com.sharepic.picture.Picture;

@Component
public class PictureService {

	@Autowired
	PictureDao pictureDao;

	/**
	 * 重複する値のないトピックリストを生成する
	 * @return
	 * @throws Exception
	 */
	public List<String> getFilteredTopicList() throws Exception {
		//トピック一覧取得
		List<String> topicList = pictureDao.getTopicList();
		//重複するもはを1つにまとめる
		List<String> filteredTopicList = new ArrayList<>();
		for (String topic : topicList) {
			if(!filteredTopicList.contains(topic)) {
				filteredTopicList.add(topic);
			}
		}
		filteredTopicList.stream().forEach(e -> System.out.println(e));
		return filteredTopicList;
	}

	/**
	 * ホーム画面に表示する写真情報を取得する
	 * @return pictureMap
	 * @throws Exception
	 */
	public Map<String,List<Picture>> getHomePictures() throws Exception {
		//トピックリスト取得
		List<String> topicList = getFilteredTopicList();

		int count = 3;
		if(topicList.size() < 3) {
			count = topicList.size();
		}
		//トピックに紐つく写真取得
		Map<String,List<Picture>> pictureMap = new HashMap<>();
		for(int i = 0; i<count; i++) {
			//検索処理
			List<Picture> pictureListByTopic = new ArrayList<>();
			Picture condPic = new Picture();
			condPic.setTopic(topicList.get(i));
			pictureListByTopic = pictureDao.select(condPic);
			if(pictureListByTopic.size() > 0) {
				pictureMap.put(topicList.get(i), pictureListByTopic);
			}
		}

		return pictureMap;
	}

	/**
	 * 引数のトピック名に紐づく写真リストを取得する。
	 */
	public List<Picture> getPictureByTopic(String targetTopic) throws Exception {
		//検索処理
		Picture condPic = new Picture();
		condPic.setTopic(targetTopic);
		List<Picture> pictureList = pictureDao.select(condPic);

		return pictureList;
	}

	/**
	 * 引数の投稿者名に紐づく写真リストを取得する。
	 */
	public List<Picture> getPictureByPoster(String targetPoster) throws Exception {
		//検索処理
		Picture condPic = new Picture();
		condPic.setPoster(targetPoster);
		List<Picture> pictureList = pictureDao.select(condPic);

		return pictureList;
	}

	/**
	 * objectUrlをキーとして削除処理を行う。
	 * @param objectUrl
	 * @throws Exception
	 */
	public void deletePicture(String objectUrl) throws Exception {
		//削除処理
		Picture condPic = new Picture();
		condPic.setObjectUrl(objectUrl);
		pictureDao.delete(condPic);
	}

}
