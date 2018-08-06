package com.andy.yy.app.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.andy.yy.app.bean.WebJsonBean;
import com.andy.yy.app.exception.WebExceptionCode;
import com.andy.yy.app.interceptor.WebSocketSessionInterceptor;
import com.andy.yy.base.core.ServiceException;
import com.andy.yy.base.interceptor.AuthorityContext;
import com.andy.yy.base.log.LoggerUtils;
import com.andy.yy.user.dto.MessageDTO;
import com.andy.yy.user.enums.MessageTypeEnum;
import com.andy.yy.user.service.UserForAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringWebSocketHandler extends TextWebSocketHandler {

	private static final LoggerUtils logger = LoggerUtils.newLogger(SpringWebSocketHandler.class);
	private static final Map<String, WebSocketSession> users = new HashMap<>();

	@Autowired
	private UserForAppService userForAppService;

	public SpringWebSocketHandler() {
	}

	/**
	 * 连接成功时触发
	 */
	public void afterConnectionEstablished(WebSocketSession session) {
		try {
			AuthorityContext context = WebSocketSessionInterceptor.getAuthorityContext();
			if (context == null) {
				throw new ServiceException(WebExceptionCode.TOKEN_EXPIRED);
			}
			logger.info("用户: {},id: {}上线, 当前在线总人数: {}", context.getUserName(), context.getUserId(), users.size());
			users.put(context.getUserId().toString(), session);
//			users.put((String)session.getAttributes().get("USER_ID"), session);
			// 推送离线未处理的消息给用户
			List<MessageDTO> msgs = userForAppService.queryOfflineMsgByTo(context.getUserId());
			if (!CollectionUtils.isEmpty(msgs)) {
				for (MessageDTO msg : msgs) {
					if (msg != null) {
						TextMessage returnMessage = new TextMessage(JSON.toJSONString(msg));
						session.sendMessage(returnMessage);
						userForAppService.doMsg(msg.getId());
					}
				}
			}
		} catch (Exception ex) {
			this.handlerException(ex, session);
		}
	}

	/**
	 * 关闭连接时触发
	 */
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
		try {
			logger.debug("websocket closed......");
			if(session.isOpen()) {
				session.close();
				logger.debug("websocket connection closed......");
			}
			users.remove(session);
			AuthorityContext context = WebSocketSessionInterceptor.getAuthorityContext();
			if (context == null) {
				throw new ServiceException(WebExceptionCode.TOKEN_EXPIRED);
			}
			logger.info("用户: {},id: {}下线, 当前在线总人数: {}", context.getUserName(), context.getUserId(), users.size()-1);
		} catch (Exception ex) {
			this.handlerException(ex, session);
		}
	}

	/**
	 * 错误时触发
	 */
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		try {
			logger.debug("websocket error......");
			if(session.isOpen()) {
				session.close();
				logger.debug("websocket connection closed......");
			}
			users.remove(session);
			AuthorityContext context = WebSocketSessionInterceptor.getAuthorityContext();
			if (context == null) {
				throw new ServiceException(WebExceptionCode.TOKEN_EXPIRED);
			}
			logger.info("用户: {},id: {}下线, 当前在线总人数: {}", context.getUserName(), context.getUserId(), users.size()-1);
		} catch (Exception ex) {
			this.handlerException(ex, session);
		}
	}

	/**
	 * 处理客户端消息
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) {
		try {
			AuthorityContext context = WebSocketSessionInterceptor.getAuthorityContext();
			if (context == null) {
				throw new ServiceException(WebExceptionCode.TOKEN_EXPIRED);
			}
			AuthorityContext cxt = WebSocketSessionInterceptor.getToken((String)session.getAttributes().get("TOKEN"));
			if (cxt == null) {
				throw new ServiceException(WebExceptionCode.TOKEN_EXPIRED);
			}
			String textMsg = message.getPayload().toString();
			logger.info("收到用户: {},id: {}的消息message为: {}", context.getUserName(), context.getUserId(), textMsg);
			super.handleTextMessage(session, message);
			if (StringUtils.isNotEmpty(textMsg)) {
				MessageDTO msg = JSON.toJavaObject(JSON.parseObject(textMsg), MessageDTO.class);
				if (msg != null) {
					msg.setFrom(context.getUserId());
					MessageDTO messageDTO = userForAppService.handlerMsg(msg);
					if (messageDTO != null && messageDTO.getId() != 0L) {
						boolean send = sendMessageToUser(messageDTO.getTo().toString(), JSON.toJSONString(messageDTO));
						if (send) {
							userForAppService.doMsg(messageDTO.getId());
						}
					}
				}
			}
		} catch (Exception ex) {
			this.handlerException(ex, session);
		}
	}

	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给某个用户发送消息
	 * @param userId
	 * @param message
	 */
	public boolean sendMessageToUser(String userId, String message) {
		if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(message)) {
			WebSocketSession userSession = users.get(userId);
			try {
				if (userSession != null && userSession.isOpen()) {
					userSession.sendMessage(new TextMessage(message));
					return true;
				}
			} catch (IOException e) {
				logger.info("exception in send msg, ex: {}", e.getMessage());
			}
		}
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * @param message
	 */
	public void sendMessageToAllUser(TextMessage message) {
		for (WebSocketSession user : users.values()) {
			try {
				if (user.isOpen()) {
					user.sendMessage(message);
				}
			} catch (IOException e) {
				logger.info("exception in send all msg, ex: {}", e.getMessage());
			}
		}
	}

	public void handlerException(Exception ex, WebSocketSession session) {
		logger.info("exception in handlerException, ex: {}", ex.getMessage());
		WebJsonBean webJsonBean;
		if (ex instanceof ServiceException) {
			ServiceException serviceException = (ServiceException) ex;
			int index = serviceException.getCodeIndex();
			switch (index) {
				case 10005:
				case 10004:
					webJsonBean = new WebJsonBean(serviceException.getCodeIndex(), serviceException.getCodeMessage());
					break;
				default:
					webJsonBean = new WebJsonBean(WebExceptionCode.FAIL.getIndex(), serviceException.getCodeMessage());
			}
		} else {
			webJsonBean = new WebJsonBean(WebExceptionCode.CONNECTION_TIME_OUT);
		}
		MessageDTO dto = new MessageDTO();
		dto.setType(MessageTypeEnum.EXCEPTION.getValue());
		dto.setContent(JSON.toJSONString(webJsonBean));
		try {
			if (session.isOpen()) {
				session.sendMessage(new TextMessage(JSON.toJSONString(dto)));
			}
		} catch (IOException e) {
			logger.info("exception in send msg, ex: {}", e.getMessage());
		}
	}
}
