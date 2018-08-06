package com.andy.yy.app.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author richard
 * @since 2018/2/8 14:57
 */
public class ImageUtil {

	public static byte[] base64str2bytes(String str) {
		try {
			String imgFile = str.replace(" ", "+");
			byte[] bytes = new BASE64Decoder().decodeBuffer(new String(imgFile
					.getBytes("UTF-8")));
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			return bytes;
		} catch (Exception ex) {
		}
		return null;
	}

	public static String saveImage(String base64Str) {
		if (StringUtils.isEmpty(base64Str)) {
			return "http://m.yy-happy.com/image/default.jpg";
		}
		String imageName;
		String ext;
		int node;
		try {
			ext = base64Str.substring(base64Str.lastIndexOf("data:image/") + 11, base64Str.indexOf(";"));
			if (StringUtils.isEmpty(ext)) {
				ext = "jpg";
			}
			String imgStr = base64Str.substring(base64Str.indexOf(",") + 1);
			byte[] bytes = base64str2bytes(imgStr);
			imageName = UUID.randomUUID().toString();
			int hashcode = imageName.hashCode();
			node = hashcode & 0xf;
			saveFile(node);
			String fileName = File.separator + "data" + File.separator + "image" + File.separator + node +
					File.separator + imageName + "." + ext;
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			return "http://m.yy-happy.com/image/default.jpg";
		}
		return "http://m.yy-happy.com/image/" + node + "/" + imageName + "." + ext;
	}

	private static void saveFile(int node) {
		String d = File.separator + "data" + File.separator + "image" + File.separator + node;
		File file = new File(d);
		if (!file.exists()) {
			file.mkdir();
		}
	}
}
