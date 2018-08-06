package com.andy.yy.user.service.impl.test;

import com.andy.yy.user.entity.UserEntity;
import com.andy.yy.user.service.UserInnerService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserInnerServiceImplTest {

	static {
		System.setProperty("appName", "yy-user");
		System.setProperty("env", "test");
		System.setProperty("dubbo.application.logger", "slf4j");
	}

	private UserInnerService userInnerService;

	@Before
	public void setUp() throws Exception {
		String[] configLocations = {"classpath:META-INF/spring/applicationContext.xml"};
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocations);
		userInnerService = applicationContext.getBean("userInnerService", UserInnerService.class);
	}

	@Ignore
	@Test
	public void testSaveUser() {
		UserEntity user = new UserEntity();
		user.setAccount("360121199211257536");
		user.setEmail("123@gmail.com");
		user.setPwd("123456");
		user.setHeadImg("http:\\sdafasf2131dsaf.png");
		userInnerService.saveUser(user);
	}
}
