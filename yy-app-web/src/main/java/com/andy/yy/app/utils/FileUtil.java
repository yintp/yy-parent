package com.andy.yy.app.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author richard
 * @since 2018/2/8 15:34
 */
public class FileUtil {
	public static String saveFile(byte[] file, String ext) {
		if (file == null || StringUtils.isEmpty(ext)) {
			return null;
		}
		String name = UUID.randomUUID().toString();
		int hashcode = name.hashCode();
		int node = hashcode & 0xf;
		saveFile(node);
		try {
			String fileName = File.separator + "data" + File.separator + "static" + File.separator + node +
					File.separator + name + "." + ext;
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			fos.write(file);
			fos.close();
		} catch (Exception e) {
			return null;
		}
		return "http://m.yy-happy.com/file/" + node + "/" + name + "." + ext;
	}

	private static void saveFile(int node) {
		String d = File.separator + "data" + File.separator + "static" + File.separator + node;
		File file = new File(d);
		if (!file.exists()) {
			file.mkdir();
		}
	}
}
