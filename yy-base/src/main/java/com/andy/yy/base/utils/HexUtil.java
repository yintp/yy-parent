package com.andy.yy.base.utils;

public class HexUtil {
	private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	public static final byte[] emptybytes = new byte[0];

	/**
	 * 将单个字节转成Hex String
	 */
	public static String byteToHexStr(byte b) {
		char[] buf = new char[2];
		buf[1] = digits[b & 0xF];
		b = (byte) (b >>> 4);
		buf[0] = digits[b & 0xF];
		return new String(buf);
	}

	/**
	 * 将字节数组转成Hex String
	 */
	public static String bytesToHexStr(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return bytesToHexStr(bytes, 0, bytes.length);
	}

	public static String bytesToHexStr(byte[] bytes, int offset, int length) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		if (offset < 0) {
			throw new IllegalArgumentException("offset(" + offset + ")");
		}
		if (offset + length > bytes.length) {
			throw new IllegalArgumentException(
					"offset + length(" + offset + length + ") > bytes.length(" + bytes.length + ")");
		}
		char[] buf = new char[2 * length];
		for (int i = 0; i < length; i++) {
			byte b = bytes[i + offset];
			buf[2 * i + 1] = digits[b & 0xF];
			b = (byte) (b >>> 4);
			buf[2 * i + 0] = digits[b & 0xF];
		}
		return new String(buf);
	}

	/**
	 * 将单个hex Str转换成字节
	 */
	public static byte hexStrToByte(String str) {
		if (str != null && str.length() == 1) {
			return charToByte(str.charAt(0));
		} else {
			return 0;
		}
	}

	/**
	 * 字符到字节
	 */
	public static byte charToByte(char ch) {
		if (ch >= '0' && ch <= '9') {
			return (byte) (ch - '0');
		} else if (ch >= 'a' && ch <= 'f') {
			return (byte) (ch - 'a' + 10);
		} else if (ch >= 'A' && ch <= 'F') {
			return (byte) (ch - 'A' + 10);
		} else {
			return 0;
		}
	}

	/**
	 * 将单个hex Str转换成字节数组
	 */
	public static byte[] hexStrToBytes(String str) {
		if (str == null || str.equals("")) {
			return emptybytes;
		}
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			char high = str.charAt(i * 2);
			char low = str.charAt(i * 2 + 1);
			bytes[i] = (byte) (charToByte(high) * 16 + charToByte(low));
		}
		return bytes;
	}
}
