package com.sharepic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.sharepic.util.DBCommonUtils;

@Component
public class SharepicStartUp {

//	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	public void startApplication() {
//		logger.debug("アプリケーションを起動しました。");
		System.out.println("アプリケーションを起動しました。");

//		logger.debug("PostgreSQLとのコネクションを確立します。");
		System.out.println("PostgreSQLとのコネクションを確立します。");
		boolean openDbResult1 = DBCommonUtils.openSqlSession();
		if(openDbResult1) {
//			logger.debug("PostgreSQLとのコネクションの確立に成功しました。");
			System.out.println("PostgreSQLとのコネクションの確立に成功しました。");
		} else {
//			logger.debug("PostgreSQLとのコネクションの確立に失敗しました。");
			System.out.println("PostgreSQLとのコネクションの確立に失敗しました。");
		}

//		System.out.println("Cassandraとのコネクションを確立します。");
////		logger.debug("Cassandraとのコネクションを確立します。");
//		boolean openDbResult2 = DBCommonUtils.openCassandraConnection();
//		if(openDbResult2) {
////			logger.debug("Cassandraとのコネクションの確立に成功しました。");
//			System.out.println("Cassandraとのコネクションの確立に成功しました。");
//		} else {
////			logger.debug("Cassandraとのコネクションの確立に失敗しました。");
//			System.out.println("Cassandraとのコネクションの確立に失敗しました。");
//		}

	}

	@PreDestroy
	public void endApplication() {
//		logger.debug("PostgreSQLとのコネクションを切断します。");
		System.out.println("PostgreSQLとのコネクションを切断します。");
		boolean closeDbResult1 = DBCommonUtils.closeSqlSession();
		if(closeDbResult1) {
//			logger.debug("PostgreSQLとのコネクションの切断に成功しました。");
			System.out.println("PostgreSQLとのコネクションの切断に成功しました。");
		} else {
//			logger.debug("PostgreSQLとのコネクションの切断に失敗しました。");
			System.out.println("PostgreSQLとのコネクションの切断に失敗しました。");
		}

////		logger.debug("Cassandraとのコネクションを切断します。");
//		System.out.println("Cassandraとのコネクションを切断します。");
//		boolean closeDbResult2 = DBCommonUtils.closeCassandraConnection();
//		if(closeDbResult2) {
////			logger.debug("Cassandraとのコネクションの切断に成功しました。");
//			System.out.println("Cassandraとのコネクションの切断に成功しました。");
//		} else {
////			logger.debug("Cassandraとのコネクションの切断に失敗しました。");
//			System.out.println("Cassandraとのコネクションの切断に失敗しました。");
//		}

//		logger.debug("アプリケーションを終了します。");
		System.out.println("アプリケーションを終了します。");
	}


}
