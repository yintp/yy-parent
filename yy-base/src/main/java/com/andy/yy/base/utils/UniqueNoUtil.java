package com.andy.yy.base.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;

public class UniqueNoUtil {

	public enum UniqueNoType {
		P
	}

	private static Calendar cal = Calendar.getInstance();
	private static int seq = 0;
	private static final int ROTATION = 999;
	private static int ipMix = 0;
	private static final String LOCAL_HOST = "127.0.0.1";

	/**
	 * 唯一单号生成规则：类型+时间（精确到毫秒）+最后一段的IP地址+序列号
	 */
	public static synchronized String next(UniqueNoType type) {
		if (seq > ROTATION) {
			seq = 0;
		}
		if (ipMix == 0) {
			try {
				String ipAddress = getLocalIp();
				String[] ipAddresses = ipAddress.split("\\.");
				ipMix = Integer.parseInt(ipAddresses[3]);
			} catch (Exception e) {
				ipMix = 1;
			}
		}
		cal.setTimeInMillis(System.currentTimeMillis());
		return type + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL%2$03d%3$03d", cal, ipMix, seq++);
	}

	private static String getLocalIp() {
		String localIp = LOCAL_HOST;
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = addresses.nextElement();
					if (ip != null && (ip instanceof Inet4Address) && !LOCAL_HOST.equals(ip.getHostAddress())) {
						localIp = ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
		}
		return localIp;
	}
}
