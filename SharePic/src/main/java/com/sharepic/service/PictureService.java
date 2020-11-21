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

@Component
public class PictureService {

	@Autowired
	PictureDao pictureDao;

	/**
	 * ホーム画面に表示する写真情報を取得する
	 * @return pictureMap
	 * @throws Exception
	 */
	public Map<String,List<Picture>> getHomePictures() throws Exception {
		//トピック一覧取得
		List<String> topicList = pictureDao.getTopicList();
		//3つのトピック選定
		Set<String> topicSet = new LinkedHashSet<>();
		for (String topic : topicList) {
			topicSet.add(topic);
			if(topicSet.size() == 3) {
				break;
			}
		}

		//トピックに紐つく写真取得
		Map<String,List<Picture>> pictureMap = new HashMap<>();
		for(String topic : topicSet) {
			//検索処理
			List<Picture> pictureListByTopic = new ArrayList<>();
			Picture condPic = new Picture();
			condPic.setTopic(topic);
			pictureListByTopic = pictureDao.select(condPic);
			if(pictureListByTopic.size() > 0) {
				pictureMap.put(topic, pictureListByTopic);
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

}
