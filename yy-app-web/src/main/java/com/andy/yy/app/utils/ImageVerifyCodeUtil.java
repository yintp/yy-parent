package com.andy.yy.app.utils;

import com.andy.yy.app.exception.WebExceptionCode;
import com.andy.yy.base.log.LoggerUtils;
import com.andy.yy.base.redis.RedisKey;
import com.andy.yy.base.redis.RedisManager;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author richard
 * @since 2018/2/5 18:14
 */
public class ImageVerifyCodeUtil {

	private static final LoggerUtils logger = LoggerUtils.newLogger(ImageVerifyCodeUtil.class);

	private static int imgWidth = 120;
	private static int imgHeight = 45;
	private static int codeCount = 5;
	private static int x = imgWidth / (codeCount + 1);
	private static int fontHeight = imgHeight - 5;
	private static int codeY = imgHeight - 8;
	private static String fontStyle = "Times New Roman";

	/**
	 * 图形验证码过期时间，redis单位s  10分钟
	 */
	public static Integer EXPTIME = 60*10;
	
	public void processRequest(HttpServletRequest request, HttpServletResponse response, String id) {
		try {
			response.setContentType("image/jpeg");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			Object obj = RedisManager.get(RedisKey.USER_IMAGE_VERIFY_CODE + id);
			if(obj == null) {
			}
			String code = obj.toString();
			
			// 在内存中创建图象
			BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

			// 获取图形上下文
			Graphics2D g = image.createGraphics();

			// 生成随机类
			Random random = new Random();

			// 设定背景色
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, imgWidth, imgHeight);

			// 设定字体
			g.setFont(new Font(fontStyle, Font.PLAIN + Font.ITALIC, fontHeight));

			// 画边框
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, imgWidth - 1, imgHeight - 1);

			// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
			g.setColor(new Color(190,190,190));
			for (int i = 0; i < 30; i++) {
				int x = i * 10;
				int y = 0;
				int xl = i * 10 - 45;
				int yl = 45;
				g.drawLine(x, y, xl, yl);
			}

			char[] chars = code.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char retWord = chars[i];
				g.setColor(new Color(120,123,127));
				g.drawString(String.valueOf(retWord), (i) * x, codeY);
			}
			// 图象生效
			g.dispose();
			ServletOutputStream responseOutputStream = response.getOutputStream();
			// 输出图象到页面
			ImageIO.write(image, "JPEG", responseOutputStream);

			// 以下关闭输入流！
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private static char getSingleNumberChar() {
		Random random = new Random();
		int numberResult = random.nextInt(10);
		int ret = numberResult + 48;
		return (char) ret;
	}

	public static String createCode() {
		Random random = new Random();
		String sRand = ""; // 随机Code
		for (int i = 0; i < codeCount; i++) {
			int wordType = random.nextInt(3);
			char retWord = 0;
			switch (wordType) {
			case 0:
				while (true) { // 去除验证码中的0跟1
					retWord = getSingleNumberChar();
					if (retWord == '0' || retWord == '1')
						continue;
					break;
				}
				break;
			case 1:
				while (true) { // 去除验证码中的0跟1
					retWord = getSingleNumberChar();
					if (retWord == '0' || retWord == '1')
						continue;
					break;
				}
				break;
			case 2:
				while (true) { // 去除验证码中的0跟1
					retWord = getSingleNumberChar();
					if (retWord == '0' || retWord == '1')
						continue;
					break;
				}
				break;
			}
			sRand += String.valueOf(retWord);
		}
		logger.info("rand: {}", sRand);
		String id = newId();
		logger.info("id: {}", id);
		RedisManager.set(RedisKey.USER_IMAGE_VERIFY_CODE + id, sRand, ImageVerifyCodeUtil.EXPTIME);
		return id;
	}

	private static AtomicInteger a = new AtomicInteger();

	/**
	 * 多线程安全的获取一个ID
	 */
	public synchronized static String newId() {
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss", Locale.CHINA);
		DecimalFormat df1 = new DecimalFormat("00000");
		// 获取一个0-999999的数
		int i = (a.incrementAndGet() & Integer.MAX_VALUE) % 100000;
		if (i == 0) {
			i = (a.incrementAndGet() & Integer.MAX_VALUE) % 100000;
		}
		return df.format(System.currentTimeMillis()) + df1.format(i);
	}

	public static boolean verifyCode(String id, String code) {
		boolean result = false;
		Object obj = RedisManager.get(RedisKey.USER_IMAGE_VERIFY_CODE + id);
		if(obj != null){
			String vcode = obj.toString();
			if (vcode.equalsIgnoreCase(code)) {
				result = true;
			}
		}
		RedisManager.delete(RedisKey.USER_IMAGE_VERIFY_CODE + id);
		return result;
	}

}
