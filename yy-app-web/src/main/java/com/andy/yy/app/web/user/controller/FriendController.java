package com.andy.yy.app.web.user.controller;

import com.alibaba.fastjson.JSON;
import com.andy.yy.app.bean.WebJsonBean;
import com.andy.yy.app.exception.WebExceptionCode;
import com.andy.yy.app.interceptor.AuthorityInterceptor;
import com.andy.yy.app.websocket.handler.SpringWebSocketHandler;
import com.andy.yy.base.log.LoggerUtils;
import com.andy.yy.user.dto.FriendDTO;
import com.andy.yy.user.dto.FriendDetailDTO;
import com.andy.yy.user.dto.MessageDTO;
import com.andy.yy.user.dto.SearchDTO;
import com.andy.yy.user.entity.UserEntity;
import com.andy.yy.user.enums.MessageTypeEnum;
import com.andy.yy.user.service.UserForAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "api")
public class FriendController {

	private static final LoggerUtils logger = LoggerUtils.newLogger(FriendController.class);

	@Autowired
	private UserForAppService userForAppService;
	@Autowired
	private SpringWebSocketHandler springWebSocketHandler;

	@RequestMapping(value = "friend/search/{name}", method = RequestMethod.GET)
	@ResponseBody
	public WebJsonBean search(@PathVariable String name) {
		List<SearchDTO> searchs = userForAppService.search(name);
		return new WebJsonBean(WebExceptionCode.SUCCESS, searchs);
	}

	@RequestMapping(value = "friend/{friendId}", method = RequestMethod.GET)
	@ResponseBody
	public WebJsonBean detail(@PathVariable Long friendId) {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		FriendDetailDTO friendDto = userForAppService.detailFriend(userId, friendId);
		return new WebJsonBean(WebExceptionCode.SUCCESS, friendDto);
	}

	@RequestMapping(value = "friends", method = RequestMethod.GET)
	@ResponseBody
	public WebJsonBean list() {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		List<FriendDTO> friends = userForAppService.queryFriends(userId);
		return new WebJsonBean(WebExceptionCode.SUCCESS, friends);
	}

	@RequestMapping(value = "friend/{friendId}", method = RequestMethod.POST)
	@ResponseBody
	public WebJsonBean applyAdd(@PathVariable Long friendId) {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		userForAppService.applyAddFriend(userId, friendId);
		MessageDTO msg = new MessageDTO();
		msg.setType(MessageTypeEnum.ADD_FRIEND.getValue());
		msg.setFrom(userId);
		msg.setTo(friendId);
		Map<String, Object> map = new HashMap<>();
		UserEntity user = userForAppService.findUserById(userId);
		map.put("applyUserId", userId);
		map.put("account", user.getAccount());
		map.put("userName", user.getNickName());
		map.put("headImg", user.getHeadImg());
		map.put("remark", user.getRemark());
		msg.setContent(JSON.toJSONString(map));
		MessageDTO messageDTO = userForAppService.handlerMsg(msg);
		if (messageDTO != null && messageDTO.getId() != 0L) {
			boolean send = springWebSocketHandler.sendMessageToUser(messageDTO.getTo().toString(), JSON.toJSONString(messageDTO));
			if (send) {
				userForAppService.doMsg(messageDTO.getId());
			}
		}
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}

	@RequestMapping(value = "friend/pass/{applyUserId}", method = RequestMethod.PATCH)
	@ResponseBody
	public WebJsonBean pass(@PathVariable Long applyUserId) {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		userForAppService.acceptFriend(applyUserId, userId);
		// 推送信息给好友
		MessageDTO msg = new MessageDTO();
		msg.setType(MessageTypeEnum.PASS_FRIEND.getValue());
		msg.setFrom(userId);
		msg.setTo(applyUserId);
		Map<String, Object> map = new HashMap<>();
		UserEntity user = userForAppService.findUserById(userId);
		map.put("userName", user.getNickName());
		msg.setContent(JSON.toJSONString(map));
		MessageDTO messageDTO = userForAppService.handlerMsg(msg);
		if (messageDTO != null && messageDTO.getId() != 0L) {
			boolean send = springWebSocketHandler.sendMessageToUser(messageDTO.getTo().toString(), JSON.toJSONString(messageDTO));
			if (send) {
				userForAppService.doMsg(messageDTO.getId());
			}
		}
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}

	@RequestMapping(value = "friend/refuse/{applyUserId}", method = RequestMethod.PATCH)
	@ResponseBody
	public WebJsonBean refuse(@PathVariable Long applyUserId) {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		userForAppService.refuseFriend(applyUserId, userId);
		// 推送信息给好友
		MessageDTO msg = new MessageDTO();
		msg.setType(MessageTypeEnum.REFUSE_FRIEND.getValue());
		msg.setFrom(userId);
		msg.setTo(applyUserId);
		Map<String, Object> map = new HashMap<>();
		UserEntity user = userForAppService.findUserById(userId);
		map.put("userName", user.getNickName());
		msg.setContent(JSON.toJSONString(map));
		MessageDTO messageDTO = userForAppService.handlerMsg(msg);
		if (messageDTO != null && messageDTO.getId() != 0L) {
			boolean send = springWebSocketHandler.sendMessageToUser(messageDTO.getTo().toString(), JSON.toJSONString(messageDTO));
			if (send) {
				userForAppService.doMsg(messageDTO.getId());
			}
		}
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}
}
