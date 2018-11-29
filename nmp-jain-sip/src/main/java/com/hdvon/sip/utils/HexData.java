package com.hdvon.sip.utils;

import com.hdvon.sip.enums.DirectionEnum;
/**
 * 2进值转化
 * @author wanshaojian
 * @since 2018-10-15
 */
public class HexData {
	/**
	 * 转换16进制
	 * 
	 * @param bts
	 * @return
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}


	public static int getHeight4(byte data) {// 获取高四位
		int height;
		height = ((data & 0xf0) >> 4);
		return height;
	}

	public static int getLow4(byte data) {// 获取低四位
		int low;
		low = (data & 0x0f);
		return low;
	}

	// 使用1字节就可以表示b
	public static String numToHex8(int b) {
		return String.format("%02x", b).toUpperCase();// 2表示需要两个16进行数
	}

	/**
	 * 云台方向控制PTZ指令举例
	 * 
	 * @param em
	 * @param speed
	 * @return
	 */
	private static String cloudControlHex(String directionType, int speed) {
		 StringBuffer ptzCmd = new StringBuffer();
		 String bit1 = "A5",bit2 = "0F",bit3 = "00",bit4 = "00",bit5 = "00",bit6= "00",bit7 ="00",bit8 = "00";
		 ptzCmd.append(bit1).append(bit2).append(bit3);
		 DirectionEnum em = DirectionEnum.getObjectByKey(directionType);
		 if(DirectionEnum.UP.equals(em)) {
			 bit4 = "08";
			 bit5 = HexData.numToHex8(speed);
		
		 }else if(DirectionEnum.DOWN.equals(em)) {
			 bit4 = "04";
			 bit5 = HexData.numToHex8(speed);
		
		 }else if(DirectionEnum.LEFT.equals(em)) {
			 bit4 = "02";
			 bit6 = HexData.numToHex8(speed);
		
		 }else if(DirectionEnum.RIGHT.equals(em)) {
			 bit4 = "01";
			 bit6 = HexData.numToHex8(speed);
		 }else {
			 bit4 = "29";
		 }
		 ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		
		 
		 Long checkCode = byteSum(bit1)
				 +byteSum(bit2)
				 +byteSum(bit3)
				 +byteSum(bit4)
				 +byteSum(bit5)
				 +byteSum(bit6)
				 +byteSum(bit7);
		 checkCode = checkCode%256;
	 
		 bit8 = numToHex8(checkCode.intValue());
		 ptzCmd.append(bit8);
		 return ptzCmd.toString();
	}

	
	private static long byteSum(String byteStr) {
		byte[] byteArr = byteStr.getBytes();
		byte s1 = 0;
		for(byte b1:byteArr) {
			s1+=b1;
		}
		 return s1;
	}
	public static void main(String[] args) {
		
		System.out.println(cloudControlHex(DirectionEnum.UP.getKey(), 1));
	}

}
