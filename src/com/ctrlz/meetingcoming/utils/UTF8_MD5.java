package com.ctrlz.meetingcoming.utils;

import java.security.MessageDigest;
public class UTF8_MD5 {
	/**
	 * 大写 md5加密 (utf-8格式)
	 * @param data
	 * @return
	 */
	private static String encryptMD5(String data) {
		StringBuilder sign = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(data.getBytes("UTF-8"));
			for (byte b : bytes) {
				String hex = Integer.toHexString(b & 0xFF);
				if (hex.length() == 1) {
					sign.append("0");
				}
				sign.append(hex.toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign.toString();
	}

}
