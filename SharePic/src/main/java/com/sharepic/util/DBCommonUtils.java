package com.sharepic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBCommonUtils {

	 //コネクション
	private static SqlSession sqlSession = null;

	/**
	 * PostgreSQLとのコネクションを確立します。
	 * @return 確立結果｜成功：true｜失敗：false
	 */
	public static boolean openSqlSession() {

		boolean result = true;

		try {
			String configFilePath = Paths.get("src","main","resources").toRealPath(LinkOption.NOFOLLOW_LINKS).toString()
					+  File.separator
					+ PropertyUtils.getProperties().getString("mybatis_config_filepath");
			InputStream input = new FileInputStream(configFilePath);
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			SqlSessionFactory factory = builder.build(input);
			sqlSession = factory.openSession();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * PostgreSQLとのコネクションを切断します。
	 * @return 切断結果｜成功：true｜失敗：false
	 */
	public static boolean closeSqlSession() {

		boolean result = true;

		try {
			sqlSession.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * PostgreSQLとのコネクションを提供します。
	 * @return sqlSession コネクション
	 */
	public static SqlSession getSqlSession() {
		return sqlSession;
	}

}
