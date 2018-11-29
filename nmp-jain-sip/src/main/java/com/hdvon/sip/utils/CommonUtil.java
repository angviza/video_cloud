package com.hdvon.sip.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hdvon.sip.exception.SipSystemException;

/**
 * 公共帮助类
 * 
 * @author wanshaojian
 * @since 2018-10-15
 */
public class CommonUtil {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String b = "0123456789";

	
	public static String formatDate(Date date) {
		return sdf.format(date);
	}
	

	
	public static Date parseDate(String dateStr)throws SipSystemException {
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new SipSystemException(e);
		}
		return date;
	}
	
	public static Date parseDate1(String dateStr)throws SipSystemException {
		Date date = null;
		try {
			date = sdf1.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
        	throw new SipSystemException(e);
		}
		return date;
	}

	public static long parseTimeSecond(String dateStr)throws SipSystemException {
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
        	throw new SipSystemException(e);
		}
		long time = date.getTime()/1000;
		return time;
	}
	
	
	
	public static long parseTimeSecondByFormat(String dateStr,SimpleDateFormat format)throws SipSystemException {
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
        	throw new SipSystemException(e);
		}
		long time = date.getTime()/1000;
		return time;
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

	/**
     * 根据属性名获取属性值
     * 
     * @param fieldName
     * @param object
     * @return
     */
    public static void setFieldValueByReflect(String fieldName, Object object,String newValue)throws SipSystemException {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            field.set(object, newValue);
        } catch (Exception e) {
        	throw new SipSystemException(e);
        } 
    }



	public static void main(String[] args) {

	}
}
