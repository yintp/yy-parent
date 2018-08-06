package com.andy.yy.app.exception;

import com.alibaba.fastjson.JSON;
import com.andy.yy.app.bean.WebJsonBean;
import com.andy.yy.base.core.ServiceException;
import com.andy.yy.base.log.LoggerUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author richard
 * @since 2018/2/3 23:08
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {

	private static final LoggerUtils logger = LoggerUtils.newLogger(GlobalExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
										 Exception ex) {
		response.addHeader("Content-Type", "application/json;charset=UTF-8");
		StringBuffer requestURL = request.getRequestURL();
		if (request.getQueryString() != null) {
			requestURL.append("?").append(request.getQueryString());
		}
		String completeURL = requestURL.toString();
		if (!(ex instanceof ServiceException)) {
			logger.error("url: {},referer: {},exception: {}", completeURL, request.getHeader("referer"), ex);
		} else {
			ServiceException e = (ServiceException) ex;
			logger.error("url: {},referer: {},codeindex: {}, msg: {}, exception: {}",
					completeURL, request.getHeader("referer"), e.getCodeIndex(), e.getCodeMessage(), e);
		}
		if (handler instanceof HandlerMethod) {
			ResponseBody responseBody = ((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class);
			Class<?> returnType = ((HandlerMethod) handler).getMethod().getReturnType();
			if (responseBody != null && returnType != byte[].class) {
				WebJsonBean webJsonBean;
				if (ex instanceof ServiceException) {
					ServiceException serviceException = (ServiceException) ex;
					int index = serviceException.getCodeIndex();
					switch (index) {
						case 10004:
						case 10005:
							webJsonBean = new WebJsonBean(serviceException.getCodeIndex(), serviceException.getCodeMessage());
							break;
						default:
							webJsonBean = new WebJsonBean(WebExceptionCode.FAIL.getIndex(), serviceException.getCodeMessage());
					}
				} else {
					webJsonBean = new WebJsonBean(WebExceptionCode.CONNECTION_TIME_OUT);
				}
				try {
					response.getWriter().write(JSON.toJSONString(webJsonBean));
				} catch (IOException e) {
					logger.error("GlobalExceptionHandler write cause IOException", e);
				}
				return new ModelAndView();
			}
			ModelAndView modelAndView = new ModelAndView("error/error");
			if (ex instanceof ServiceException) {
				modelAndView.addObject("errorCodeKey", ((ServiceException) ex).getCodeIndex());
				modelAndView.addObject("errorMsg", ((ServiceException) ex).getCodeMessage());
			} else {
				modelAndView.addObject("errorMsg", ex.getMessage());
			}
			return modelAndView;
		} else if (handler instanceof WebSocketHttpRequestHandler) {
			logger.info("websocket excepton global handler");
		}
		return new ModelAndView();
	}
}
