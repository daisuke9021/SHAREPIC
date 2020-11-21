package com.sharepic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sharepic.picture.Picture;

@Mapper
public interface SharepicPictureStoreMapper {

	/**
	 * 登録処理
	 * @return 登録件数
	 */
	int insert(@Param("meta") Picture meta);

	/**
	 * 検索処理(COUNT)
	 * @return 取得件数
	 */
	int count(@Param("cond") Picture cond);

	/**
	 * 検索処理(SELECT)
	 * @return 検索結果
	 */
	List<Picture> select(@Param("cond") Picture cond);

	/**
	 * トピック一覧取得処理(SELECT)
	 * @return 検索結果
	 */
	List<String> getTopicList();

}
