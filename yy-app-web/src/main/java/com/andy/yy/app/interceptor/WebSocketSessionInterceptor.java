package com.andy.yy.app.interceptor;

import com.andy.yy.app.utils.HttpHeaderUtil;
import com.andy.yy.base.interceptor.AuthorityContext;
import com.andy.yy.base.log.LoggerUtils;
import com.andy.yy.base.redis.RedisKey;
import com.andy.yy.base.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author richard
 * @since 2018/2/4 22:43
 */
public class WebSocketSessionInterceptor implements HandlerInterceptor {

	private static final LoggerUtils logger = LoggerUtils.newLogger(WebSocketSessionInterceptor.class);
	private static ThreadLocal<String> tokenContext = new ThreadLocal<>();
	private static ThreadLocal<AuthorityContext> authorityContext = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		this.clearContext();
		String token = HttpHeaderUtil.getToken(request);
		logger.info("token: {},请求websocket...", token);
		if (StringUtils.isNotEmpty(token)) {
			HttpSession session = request.getSession(true);
			session.setAttribute("TOKEN", token);
			tokenContext.set(token);
			AuthorityContext authority = RedisManager.getObject(RedisKey.TOKEN + token, AuthorityContext.class);
			if (authority != null) {
				logger.info("USER_ID: {}", authority.getUserId());
				session.setAttribute("USER_ID", authority.getUserId().toString());
				authorityContext.set(authority);
			}
		}
		return true;
	}

	private void clearContext() {
		tokenContext.set(null);
		authorityContext.set(null);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

	public static AuthorityContext getAuthorityContext() {
		if (tokenContext.get() == null || authorityContext.get() == null || authorityContext.get().getUserId() == null) {
			return null;
		}
		return authorityContext.get();
	}

	public static AuthorityContext getToken(String token) {
		if (tokenContext.get() == null || authorityContext.get() == null || authorityContext.get().getUserId() == null) {
			return null;
		}
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		AuthorityContext authority = RedisManager.getObject(RedisKey.TOKEN + token, AuthorityContext.class);
		if (authority == null) {
			return null;
		}
		return authorityContext.get();
	}
}
