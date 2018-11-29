package com.hdvon.nmp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;

public class DateHandlerUtils {
	/**
	 * 获取两个日期之间的天数
	 * @param startTime
	 * @param endTime
	 * @param dateFormat
	 * @return
	 */
	public static long getDateNum(String startTime, String endTime, String dateFormat) {
		SimpleDateFormat ft = new SimpleDateFormat(dateFormat);
		long quot = 0;
		try {
			Date date1 = ft.parse(startTime);
			Date date2 = ft.parse(endTime);
			quot = date2.getTime() - date1.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}
	
	
	/**
	 * 获取两个日期之间的所有日期
	 * @param startTime
	 * @param endTime
	 * @param dateFormat
	 * @throws Exception
	 */
	public static List<String> getDateArr(String startTime, String endTime, String dateFormat) throws Exception {
		List<String> dateArr = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat); 
		Date begin = sdf.parse(startTime);      
		Date end = sdf.parse(endTime);      
		double between = (end.getTime() - begin.getTime())/1000;//除以1000是为了转换成秒      
		double day = between/(24*3600);
		
		for(int index = 0;index <= day;index++) {
			Calendar cd = Calendar.getInstance();   
		    cd.setTime(sdf.parse(startTime));   
		    cd.add(Calendar.DATE, index);//增加一天   
		    //cd.add(Calendar.MONTH, n);//增加一个月
		    dateArr.add(sdf.format(cd.getTime()));
		}
		return dateArr;
	}
	
	
	/**
	 * 获取时间段
	 * @param startTime
	 * @param endTime
	 * @param dateFormat
	 * @throws Exception
	 */
	public static List<String> getPeriods() throws Exception {
		List<String> periods = new ArrayList<String>();
		
		try {
			Calendar cd = Calendar.getInstance(); 
			String startTime = DateUtil.format(cd.getTime(), "yyyy-MM-dd")+" 00:00:00";
			String endTime = DateUtil.format(cd.getTime(), "yyyy-MM-dd")+" 23:59:59";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date begin = sdf.parse(startTime);      
			Date end = sdf.parse(endTime);      
			double hours = (end.getTime() - begin.getTime())/1000/3600;//转换成小时     
			
			String str = "";
			for(int index = 0;index <= hours;index++) {
				if (index < 10) {
					str += "0"+index+":00:00-"+"0"+index+":59:59"+",";
				} else {
					str += index+":00:00-"+index+":59:59"+",";
				}
			}
			
			String items[] = str.split(",");
			periods = Arrays.asList(items);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return periods;
	}
	
	public static void main(String[] args) {
		try {
			List<String> list = DateHandlerUtils.getPeriods();
			for (int index = 0;index < list.size();index++) {
				System.out.println(list.get(index));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		transForDate(1537236886);
	}
	
	
	/** 
     * 时间戳转成UTC格式时间(非标准utc时间)
     * @param ms 
     * @return 
     */  
    public static String transForDate(long ms){  
    	if(ms==0) {
    		return null;
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String strUtc = sdf.format(new Date(ms*1000));
		return strUtc;
    }

}
