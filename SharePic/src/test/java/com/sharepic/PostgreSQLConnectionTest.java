package com.sharepic;

import org.junit.Test;

import com.sharepic.util.DBCommonUtils;

public class PostgreSQLConnectionTest {

	@Test
	public void openAndClose() {

		DBCommonUtils.openSqlSession();
		DBCommonUtils.closeSqlSession();
	}

}
