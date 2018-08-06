package com.andy.yy.app.web.user.controller;

import com.andy.yy.app.bean.WebJsonBean;
import com.andy.yy.app.exception.WebExceptionCode;
import com.andy.yy.app.utils.ImageVerifyCodeUtil;
import com.andy.yy.base.log.LoggerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/code")
public class ImageCodeController {

	private static final LoggerUtils logger = LoggerUtils.newLogger(ImageCodeController.class);

	@RequestMapping("/get")
	@ResponseBody
	public WebJsonBean createImageCode(HttpServletRequest request) {
		String imageCodeId = ImageVerifyCodeUtil.createCode();
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("imageCodeId", imageCodeId);
		logger.info("imageCodeId: {}", imageCodeId);
		return new WebJsonBean(WebExceptionCode.SUCCESS, resp);
	}

	@RequestMapping("/show")
	public void showImageCode(HttpServletRequest request, HttpServletResponse response, String imageCodeId) {
		logger.info("imageCodeId: {}", imageCodeId);
		ImageVerifyCodeUtil ivCode = new ImageVerifyCodeUtil();
		ivCode.processRequest(request, response, imageCodeId);
	}
}
