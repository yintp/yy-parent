package com.andy.yy.app.web.user.controller;

import com.alibaba.fastjson.JSON;
import com.andy.yy.app.bean.WebJsonBean;
import com.andy.yy.app.exception.WebExceptionCode;
import com.andy.yy.app.interceptor.AuthorityInterceptor;
import com.andy.yy.app.utils.HttpHeaderUtil;
import com.andy.yy.base.log.LoggerUtils;
import com.andy.yy.base.redis.RedisKey;
import com.andy.yy.base.redis.RedisManager;
import com.andy.yy.user.entity.UserEntity;
import com.andy.yy.user.service.UserForAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author richard
 * @since 2018/2/4 22:43
 */
@Controller
@RequestMapping(value = "api/user")
public class UserController {

	private static final LoggerUtils logger = LoggerUtils.newLogger(UserController.class);

	@Autowired
	private UserForAppService userForAppService;

	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public WebJsonBean register(String account, String pwd) {
		logger.info("account: {}, pwd:{}", account, pwd);
		userForAppService.register(account, pwd);
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	@ResponseBody
	public WebJsonBean login(String account, String pwd) {
		logger.info("account: {}, pwd:{}", account, pwd);
		UserEntity user = userForAppService.login(account, pwd);
		Map<String, Object> map = new HashMap<>();
		map.put("token", user.getToken());
		map.put("userId", user.getId());
		return new WebJsonBean(WebExceptionCode.SUCCESS, map);
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	@ResponseBody
	public WebJsonBean logOut(HttpServletRequest request) {
		String token = HttpHeaderUtil.getToken(request);
		RedisManager.delete(RedisKey.TOKEN + token);
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}

	@RequestMapping(value = "info", method = RequestMethod.GET)
	@ResponseBody
	public WebJsonBean getUserInfo() {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		UserEntity user = userForAppService.findUserById(userId);
		logger.info("user: {}", JSON.toJSONString(user));
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getId());
		map.put("account", user.getAccount());
		map.put("token", user.getToken());
		map.put("nickName", user.getNickName());
		map.put("headImg", user.getHeadImg());
		map.put("remark", user.getRemark());
		return new WebJsonBean(WebExceptionCode.SUCCESS, map);
	}

	@RequestMapping(value = "headshot", method = RequestMethod.PATCH)
	@ResponseBody
	public WebJsonBean modifyHeadImg(String imgUrl) {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		userForAppService.modifyHeadImg(userId, imgUrl);
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}

	@RequestMapping(value = "nickname", method = RequestMethod.PATCH)
	@ResponseBody
	public WebJsonBean modifyNickName(String nickName) {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		userForAppService.modifyNickName(userId, nickName);
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}

	@RequestMapping(value = "signature", method = RequestMethod.PATCH)
	@ResponseBody
	public WebJsonBean modifyRemark(String signature) {
		Long userId = AuthorityInterceptor.getAuthorityContext().getUserId();
		userForAppService.modifyRemark(userId, signature);
		return new WebJsonBean(WebExceptionCode.SUCCESS);
	}
}
