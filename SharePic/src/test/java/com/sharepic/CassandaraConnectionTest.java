package com.sharepic;

import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.sharepic.util.PropertyUtils;

public class CassandaraConnectionTest {

	@Test
	public void openAndClose() {

		//NoSQL(Cassandra)とのコネクション
		Cluster cassandraCluster = null;
		Session cassandraSession = null;

		try {
			String HOST = PropertyUtils.getProperties("application").getString("cassandra_host");
			Integer PORT = Integer.valueOf(PropertyUtils.getProperties("application").getString("cassandra_port"));
			System.out.println("Connecting to 【" + HOST + ":" + PORT + "】");
			cassandraCluster = cassandraCluster.builder().addContactPoint(HOST).withPort(PORT).withoutJMXReporting().build();
			cassandraSession = cassandraCluster.connect();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			cassandraSession.close();
			cassandraCluster.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
