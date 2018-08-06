package com.andy.yy.app.web.common;

import com.andy.yy.app.bean.WebJsonBean;
import com.andy.yy.app.exception.WebExceptionCode;
import com.andy.yy.app.utils.FileUtil;
import com.andy.yy.app.utils.ImageUtil;
import com.andy.yy.base.log.LoggerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author richard
 * @since 2018/2/8 11:36
 */
@Controller
@RequestMapping(value = "api/upload")
public class ImageUploadController {

	private static final LoggerUtils logger = LoggerUtils.newLogger(ImageUploadController.class);

	@RequestMapping(value = "image", method = RequestMethod.POST)
	@ResponseBody
	public static WebJsonBean image(String file) {
		logger.info("imgFile: {}", file);
		String url = ImageUtil.saveImage(file);
		Map<String, Object> map = new HashMap<>();
		map.put("imageUrl", url);
		return new WebJsonBean(WebExceptionCode.SUCCESS, map);
	}

	@RequestMapping(value = "file", method = RequestMethod.POST)
	@ResponseBody
	public static WebJsonBean image(@RequestParam MultipartFile file) {
		logger.info("file: {}", file);
		if (file == null) {
			return new WebJsonBean(WebExceptionCode.FAIL);
		}
		try {
			String originalFilename = file.getOriginalFilename();
			String temp[] = originalFilename.split("\\.");
			String suffixStr = temp[1];
			String url = FileUtil.saveFile(file.getBytes(), suffixStr);
			Map<String, Object> map = new HashMap<>();
			map.put("fileUrl", url);
			return new WebJsonBean(WebExceptionCode.SUCCESS, map);
		} catch (Exception ex) {
			return new WebJsonBean(WebExceptionCode.FAIL);
		}
	}
}
