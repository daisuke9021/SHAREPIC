package com.sharepic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class DBCommonUtils {

	//RDB(PostgreSQL)とのコネクション
	private static SqlSession postgresqlSession = null;
	//NoSQL(Cassandra)とのコネクション
	private static Cluster cassandraCluster = null;
	private static Session cassandraSession = null;

	/**
	 * PostgreSQLとのコネクションを確立します。
	 * @return 確立結果｜成功：true｜失敗：false
	 */
	public static boolean openSqlSession() {

		boolean result = true;

		try {
			String configFilePath = Paths.get("src","main","resources").toRealPath(LinkOption.NOFOLLOW_LINKS).toString()
					+  File.separator
					+ PropertyUtils.getProperties("application").getString("mybatis_config_filepath");
			InputStream input = new FileInputStream(configFilePath);
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			SqlSessionFactory factory = builder.build(input);
			postgresqlSession = factory.openSession();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * Cassandraとのコネクションを確立します。
	 * @return 確立結果｜成功：true｜失敗：false
	 */
	public static boolean openCassandraConnection() {

		boolean result = true;

		try {
			String HOST = PropertyUtils.getProperties("application").getString("cassandra_host");
			Integer PORT = Integer.valueOf(PropertyUtils.getProperties("application").getString("cassandra_port"));
			System.out.println("Connecting to 【" + HOST + ":" + PORT + "】");
			cassandraCluster = cassandraCluster.builder().addContactPoint(HOST).withPort(PORT).withoutJMXReporting().build();
			cassandraSession = cassandraCluster.connect();

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
			postgresqlSession.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * Cassandraとのコネクションを切断します。
	 * @return 切断結果｜成功：true｜失敗：false
	 */
	public static boolean closeCassandraConnection() {

		boolean result = true;

		try {
			cassandraSession.close();
			cassandraCluster.close();
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
	public static SqlSession getPostgresqlConnection() {
		return postgresqlSession;
	}

	/**
	 * Cassandraのクラスタとのコネクション(セッション)を提供します。
	 * @return session コネクション
	 */
	public static Session getCassandraConnection() {
		return cassandraSession;
	}

}
