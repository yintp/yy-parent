package com.andy.yy.app.websocket.config;

import com.andy.yy.app.websocket.handler.SpringWebSocketHandler;
import com.andy.yy.app.websocket.interceptor.SpringWebSocketHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class SpringWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(springWebSocketHandler(), "/websocket/socketServer.do")
				.addInterceptors(new SpringWebSocketHandlerInterceptor());
	}

	@Bean
	public SpringWebSocketHandler springWebSocketHandler(){
		return new SpringWebSocketHandler();
	}
}
