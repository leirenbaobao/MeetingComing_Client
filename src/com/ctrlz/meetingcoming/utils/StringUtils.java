package com.ctrlz.meetingcoming.utils;

public class StringUtils {

	public static boolean isEmpty(String str) {
		
		return (str == null || str.trim().length() == 0) ? true : false;
	}
}
