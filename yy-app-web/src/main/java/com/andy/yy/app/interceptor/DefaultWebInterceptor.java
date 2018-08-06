package com.andy.yy.app.interceptor;

import com.andy.yy.base.log.LoggerUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author richard
 * @since 2018/2/3 22:40
 */
public class DefaultWebInterceptor implements HandlerInterceptor {

	private static final LoggerUtils logger = LoggerUtils.newLogger("SLOW_URL");
	private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.addHeader("Access-Control-Max-Age", "1800");
		long start = System.currentTimeMillis();
		startTime.set(start);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long cost = System.currentTimeMillis() - startTime.get();
		if (cost > 500) {
			StringBuffer requestURL = request.getRequestURL();
			if (request.getQueryString() != null) {
				requestURL.append("?").append(request.getQueryString());
			}
			String completeURL = requestURL.toString();
			if (cost < 3000) {
				logger.info("url: {},cost: {}ms", completeURL, cost);
			} else {
				logger.info("url: {},cost: {}ms", completeURL, cost);
			}
		}
	}
}
