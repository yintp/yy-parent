package com.andy.yy.base.utils;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

public class MD5Util {

	private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "a", "b", "c", "d", "e", "f"};

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		} catch (Exception exception) {
		}
		return resultString;
	}

	public static final String MD5Base64(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			BASE64Encoder base64Encoder = new BASE64Encoder();
			if (charsetname == null || "".equals(charsetname)) {
				resultString = base64Encoder.encode(md.digest(origin.getBytes()));
			} else {
				resultString = base64Encoder.encode(md.digest(origin.getBytes(charsetname)));
			}
		} catch (Exception ex) {
		}
		return resultString;
	}
}
