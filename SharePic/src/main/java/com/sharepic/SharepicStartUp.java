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
//		logger.debug("DBコネクションを確立します。");
		System.out.println("DBコネクションを確立します。");
		boolean result = DBCommonUtils.openSqlSession();
		if(result) {
//			logger.debug("DBコネクションの確立に成功しました。");
			System.out.println("DBコネクションの確立に成功しました。");
		} else {
//			logger.debug("DBコネクションの確立に失敗しました。");
			System.out.println("DBコネクションの確立に失敗しました。");
		}
	}

	@PreDestroy
	public void endApplication() {
//		logger.debug("DBコネクションを切断します。");
		System.out.println("DBコネクションを切断します。");
		boolean result = DBCommonUtils.closeSqlSession();
		if(result) {
//			logger.debug("DBコネクションの切断に成功しました。");
			System.out.println("DBコネクションの切断に成功しました。");
		} else {
//			logger.debug("DBコネクションの切断に失敗しました。");
			System.out.println("DBコネクションの切断に失敗しました。");
		}

//		logger.debug("アプリケーションを終了します。");
		System.out.println("アプリケーションを終了します。");
	}


}
