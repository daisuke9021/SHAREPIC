package com.sharepic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sharepic.dao.UserDao;
import com.sharepic.user.User;

/**
 * テーブル「SHAREPIC_USER」関連の処理を行います。
 * @author daisuke
 *
 */
@Component
public class UserService {

	@Autowired
	UserDao dao;

	/**
	 * ユーザー登録処理
	 */
	public int registerUser(String userName, String password) {
		User user = new User(userName, password);
		int result = dao.insert(user);
		return result;
	}

	/**
	 * ユーザー検索処理
	 */
	public int searchUser(String userName, String password) {
		User user = new User(userName, password);
		int result = dao.select(user);
		return result;
	}
}
