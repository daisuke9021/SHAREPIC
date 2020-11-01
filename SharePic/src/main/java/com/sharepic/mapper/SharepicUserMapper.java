package com.sharepic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sharepic.user.User;

@Mapper
public interface SharepicUserMapper {

	/**
	 * 登録処理
	 * @return 登録件数
	 */
	int insert(@Param("meta") User user);

	/**
	 * 検索処理
	 * @return 取得件数
	 */
	int count(@Param("cond") User user);

}
