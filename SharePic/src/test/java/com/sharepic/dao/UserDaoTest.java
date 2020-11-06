package com.sharepic.dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.sharepic.mapper.SharepicUserMapper;
import com.sharepic.user.User;
import com.sharepic.util.DBCommonUtils;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoTest {

	@InjectMocks
	private UserDao dao = new UserDao();

	@Mock
	private SqlSession mockSqlSession;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		DBCommonUtils.openSqlSession();
	}
	@After
	public void after() {
		DBCommonUtils.closeSqlSession();
	}

//	@Test
	public void insertTest() throws Exception {

		//@Mockで空っぽになってるsqlSessionを定義する必要がある
		SharepicUserMapper mapper = DBCommonUtils.getPostgresqlConnection().getMapper(SharepicUserMapper.class);
		Mockito.when(mockSqlSession.getMapper(SharepicUserMapper.class)).thenReturn(mapper);

		String username = "makito";
		String password = "ito";
		User user = new User(username, password);

		int result = dao.insert(user);

		System.out.println("登録件数：" + result);
	}

	@Test
	public void searchTest() {
		String username = "crsc1209";
		String password = null;
		User user = new User(username, password);

		//@Mockで空っぽになってるsqlSessionを定義する必要がある
		SharepicUserMapper mapper = DBCommonUtils.getPostgresqlConnection().getMapper(SharepicUserMapper.class);
		Mockito.when(mockSqlSession.getMapper(SharepicUserMapper.class)).thenReturn(mapper);

		int result = dao.select(user);

		System.out.println("取得件数：" + result);
	}

}
