package com.hdvon.nmp.generate.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class StyleUtil {
	/**
	 * 设置说明行样式
	 * @param xwb
	 * @return
	 */
	public  static CellStyle explainStyle(HSSFWorkbook xwb){
		CellStyle style = xwb.createCellStyle();
		Font explainfont = xwb.createFont();
		explainfont.setColor(HSSFColorPredefined.RED.getIndex());
		style.setFont(explainfont);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setVerticalAlignment(VerticalAlignment.TOP);
		style.setWrapText(true);
		style.setLocked(true);
		return style;
	}
	/**
	 * 设置表头样式
	 * @param xwb
	 * @return
	 */
	public  static CellStyle headStyle(SXSSFWorkbook xwb){
		CellStyle style = xwb.createCellStyle();
		Font headfont = xwb.createFont();
		headfont.setFontHeightInPoints((short) 14);
		headfont.setBold(true);
		style.setFont(headfont);
		
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setLocked(true);
		return style;
	}
	
	/**
	 * 设置说明行样式
	 * @param xwb
	 * @return
	 */
	public static CellStyle explainStyle(SXSSFWorkbook xwb){
		CellStyle style = xwb.createCellStyle();
		Font explainfont = xwb.createFont();
		explainfont.setColor(HSSFColorPredefined.RED.getIndex());
		style.setFont(explainfont);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setVerticalAlignment(VerticalAlignment.TOP);
		style.setWrapText(true);
		style.setLocked(true);
		return style;
	}
}
