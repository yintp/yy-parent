package com.andy.yy.app.websocket.config;


import okhttp3.*;
import okio.ByteString;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		createWebSocket();
	}
	public static void createWebSocket() {
		OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
		WebSocket mWebSocket = client.newWebSocket(new Request.Builder()
				.url("ws://m.yy-happy.com/yy-app-web/websocket/socketServer.do").addHeader("A-SID", "74025B242728436BA942C21CE9EA81D5")
				.build(), new WebSocketListener() {
			@Override
			public void onOpen(WebSocket webSocket, Response response) {
				System.out.println("onOpen - " + response);
			}

			@Override
			public void onMessage(WebSocket webSocket, String text) {
				System.out.println("onMessage - " + text);
			}

			@Override
			public void onMessage(WebSocket webSocket, ByteString bytes) {
				System.out.println("onMessageB - " + bytes);
			}

			@Override
			public void onClosed(WebSocket webSocket, int code, String reason) {
				System.out.println("onClosed - " + reason);
			}

			@Override
			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
				System.out.println("onFailure - " + t.getMessage());
			}
		});
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()) {
			mWebSocket.send(scan.next());
		}
	}
}
