package com.hdvon.nmp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共工具类
 */
public class CommonUtil {

    /**
     * 数字转换为二进制列表，如10=2+8，7=1+2+4
     * @param value
     * @return
     */
    public static List<Integer> toBinaryList(String value){
        int num = Integer.parseInt(value);
        String numStr = Integer.toBinaryString(num);
        int arrIndex = numStr.length() -1;
        List<Integer> list = new ArrayList<>();
        for(int i = arrIndex ; i >= 0 ; i-- ){
            int currIndex = arrIndex - i;
            char currChar = numStr.charAt(i);
            if('1' == currChar){
                double base = 2;
                double exp = Double.parseDouble(currIndex+"");
                int result = (int)Math.pow(base,exp);
                System.out.println(result);
                list.add(result);
            }
        }
        return list;
    }
}
