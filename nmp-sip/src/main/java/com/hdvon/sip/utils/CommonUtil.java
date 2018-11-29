package com.hdvon.sip.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 公共帮助类
 * 
 * @author wanshaojian
 *
 */
public class CommonUtil {
	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String b = "0123456789";

	
	public static String formatDate(Date date) {
		return sdf.format(date);
	}
	
	public static Date convertDate(String dateStr) {
		Date date = null;
		try {
			date = sdf1.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public static String convertFormatDate(Date date) {
		return sdf1.format(date);
	}
	


	/**
	 * 生成随机tag
	 * 
	 * @return
	 */
	public static String getTags() {
		char[] rands = new char[8];
		for (int i = 0; i < rands.length; i++) {
			int rand = (int) (Math.random() * b.length());
			rands[i] = b.charAt(rand);
		}
		return String.valueOf(rands);
	}



	public static void main(String[] args) {

	}
}
