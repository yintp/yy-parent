package com.andy.yy.user.main;

public class UserMain {
	static {
		System.setProperty("moduleName", "yy-user-provider");
		System.setProperty("env", "test");
		System.setProperty("dubbo.application.logger", "slf4j");
	}
	public static void main(String[] args) {
		com.alibaba.dubbo.container.Main.main(args);
	}
}
