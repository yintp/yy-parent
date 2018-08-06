package com.andy.yy.base.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

public class ChineseUtil {
	public static String getFirstChar(String str) {
		if (StringUtils.isNotEmpty(str)) {
			Character c = str.charAt(0);
			if (Character.isLetter(c)) {
				return c.toString().toUpperCase();
			}
			try {
				String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(str.charAt(0));
				if (pinyin != null && pinyin.length != 0) {
					return pinyin[0].toUpperCase().substring(0, 1);
				}
			} catch (Exception e) {
			}
		}
		return "Z";
	}
}
