package com.hdvon.nmp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

	 
	/** 
     * 时间戳转成UTC格式时间(非标准utc时间)
     * @param ms 
     * @return 
     */  
    public static String transForDate(long ms) {  
    	
    	if(ms==0) {
    		return null;
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String strUtc = sdf.format(new Date(ms*1000));
		return strUtc;
		
    }
    
    
    public static String formatDate(long ms, String format) {  
    	
    	if(ms == 0) {
    		
    		return null;
    	}
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
		String strUtc = sdf.format(new Date(ms*1000));
		return strUtc;
		
    }

    
    public static void main(String[] args) {
    	transForDate(1537236886);
	}

}
