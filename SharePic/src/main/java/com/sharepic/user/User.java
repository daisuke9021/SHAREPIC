package com.sharepic.user;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class User {

	//ユーザ名
	private String username;
	//パスワード
	private String password;
	//登録日時
	private Timestamp insertTime;
	//更新日時
	private Timestamp updateTime;

	public User () {}
	public User (String pUsername, String pPassword) {
		this.username = pUsername;
		this.password = pPassword;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("USERNAME：");
		sb.append(username);
		sb.append(System.lineSeparator());
		sb.append("PASSWORD：");
		sb.append(password);
		sb.append(System.lineSeparator());
		sb.append("登録日時：");
		sb.append(insertTime);
		sb.append(System.lineSeparator());
		sb.append("更新日時：");
		sb.append(updateTime);
		sb.append(System.lineSeparator());

		return sb.toString();
	}

}
