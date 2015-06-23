package com.ctrlz.meetingcoming.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatUtils {

	@SuppressLint("SimpleDateFormat")
	public static String getDate(String unixDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(unixDate));
			return sdf.format(calendar.getTime());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "DB";
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getUnixDate(String dataString){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");

		try {
			long millionSeconds = sdf.parse(dataString).getTime();//毫秒
			return ""+ millionSeconds;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "DB";
	}
	
	public static String getUnixDateWithLine(String dataString){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {
			long millionSeconds = sdf.parse(dataString).getTime();//毫秒
			return ""+ millionSeconds;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "DB";
	}

	public static String changeS2H(String seconds) {
		StringBuffer sf = new StringBuffer();
		long s = Long.parseLong(seconds);
		
		int D = (int) (s / 86400000);
		s = s % 86400000;
		int N = (int) (s / 3600000);
		s = s % 3600000;
		int K = (int) (s / 60000);
		s = s % 60000;
		if(D > 0){
			sf.append("" + D).append("天");
			return ">"+ D + "天"; 
		}
		if (N > 0) {
			sf.append("" + N).append("小时");
		}
		if (K > 0) {
			sf.append("" + K).append("分钟");
		}
		return sf.toString();
	}

	public static float transform(String time) {
		String temp[] = time.split(":");
		int hours = Integer.valueOf(temp[0]);
		int minutes = Integer.valueOf(temp[1]);
		float allSeconds = hours * 60 * 60 + minutes * 60;
		return allSeconds;
	}

}
