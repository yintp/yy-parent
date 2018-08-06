package com.andy.yy.app.interceptor;

import com.andy.yy.app.exception.WebExceptionCode;
import com.andy.yy.app.utils.HttpHeaderUtil;
import com.andy.yy.base.core.ServiceException;
import com.andy.yy.base.interceptor.AuthorityContext;
import com.andy.yy.base.redis.RedisKey;
import com.andy.yy.base.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author richard
 * @since 2018/2/4 22:43
 */
public class AuthorityInterceptor implements HandlerInterceptor {

	private static ThreadLocal<AuthorityContext> authorityContext = new ThreadLocal<>();
	private static ThreadLocal<String> tokenContext = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		this.clearContext();
		String token = HttpHeaderUtil.getToken(request);
		if (StringUtils.isEmpty(token)) {
			throw new ServiceException(WebExceptionCode.NO_LOGIN);
		}
		AuthorityContext authority = RedisManager.getObject(RedisKey.TOKEN + token, AuthorityContext.class);
		if (authority == null) {
			throw new ServiceException(WebExceptionCode.TOKEN_EXPIRED);
		}
		tokenContext.set(token);
		authorityContext.set(authority);
		return true;
	}

	private void clearContext() {
		tokenContext.set(null);
		authorityContext.set(null);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {}

	public static AuthorityContext getAuthorityContext() {
		if (authorityContext.get() == null || authorityContext.get().getUserId() == null) {
			throw new ServiceException(WebExceptionCode.NO_LOGIN);
		}
		if (tokenContext.get() == null) {
			throw new ServiceException(WebExceptionCode.TOKEN_EXPIRED);
		}
		return authorityContext.get();
	}
}
