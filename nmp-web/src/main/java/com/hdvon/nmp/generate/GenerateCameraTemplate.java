package com.hdvon.nmp.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.generate.util.ExcelUtil;
import com.hdvon.nmp.generate.util.StyleUtil;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.vo.DictionaryVo;

import cn.hutool.core.util.StrUtil;

public class GenerateCameraTemplate {

	/**
	 * 下载摄像机导入模板
	 * @param request
	 * @param response
	 * @param templateName
	 * @param fileName
	 * @param dicMap
	 * @param cols
	 * @throws IOException
	 */
	public static final void downloadCameraTemplate(HttpServletRequest request, HttpServletResponse response, String templateName, String fileName, Map<String, List<DictionaryVo>> dicMap, String cols, int colWidth) throws IOException {
	    try {
	        fileName = StrUtil.isBlank(fileName)?null:new String(fileName.getBytes("GBK"), "ISO-8859-1");
	    } catch (UnsupportedEncodingException e1) {
	        e1.printStackTrace();
	    }
	    // 获取模板位置，读取数据库（也可以读取配置文件或写死）
	    String templatePath = "templates" + File.separator;
	    // 实际位置
	    String path = templatePath + templateName;
	    System.out.println(path);
	    // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
	    response.setContentType("multipart/form-data");
	    // 2.设置文件头：最后一个参数是设置下载文件名
	    response.setHeader("Content-Disposition", "attachment;fileName="
	            + (StrUtil.isBlank(fileName)?templateName:fileName));
	    response.addHeader("Content-Type", "application/vnd.ms-excel");
	    OutputStream out = null;
	    // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
	    File file = new File(path);
	    HSSFRow row = null;
	    try {
	        FileInputStream inputStream = new FileInputStream(file);
	        HSSFWorkbook xwb= new HSSFWorkbook(inputStream);
			HSSFSheet sheet = xwb.getSheetAt(0);
			row = sheet.getRow(0);
			sheet.setDefaultColumnWidth(colWidth);
			int maxCols = Integer.parseInt(cols);
	        for(int i= 0;i<maxCols;i++){
	        	HSSFCell cell = row.getCell(i);
	        	if(cell == null) {
	        		cell = row.createCell(i);
	        		cell.setCellValue("");
	        		continue;
	        	}
	        	String value = FileUtil.getCellValue(cell);
	        	if(StrUtil.isNotBlank(value)) {
	        		List<DictionaryVo> dicVos = dicMap.get(value);
	        		String cellContent = ExcelUtil.generateCellContent(dicVos);
	        		cell.setCellValue(StrUtil.isBlank(cellContent) ? value : cellContent);
	        		cell.setCellStyle(StyleUtil.explainStyle(xwb));
	        	}
	        }
	        out = response.getOutputStream();
	        xwb.write(out);
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        if(out != null) {
	        	out.close();
	        }
	    }
	}
	
	/**
	 * 获取模板中第一行说明
	 * @param templateName
	 * @return
	 */
	public static HSSFRow getCameraTemplateExplain(String templateName, String cols, Map<String, List<DictionaryVo>> dicMap) {
		String templatePath = "templates" + File.separator;
		String path = templatePath + templateName;
		 File file = new File(path);
		 HSSFRow row = null;
		 try {
			FileInputStream inputStream = new FileInputStream(file);
			HSSFWorkbook xwb= new HSSFWorkbook(inputStream);
			HSSFSheet sheet = xwb.getSheetAt(0);
			row = sheet.getRow(0);
			int maxCols = Integer.parseInt(cols);
	        for(int i= 0;i<maxCols;i++){
	        	HSSFCell cell = row.getCell(i);
	        	if(cell == null) {
	        		cell = row.createCell(i);
	        		cell.setCellValue("");
	        		continue;
	        	}
	        	String value = FileUtil.getCellValue(cell);
	        	if(StrUtil.isNotBlank(value)) {
	        		List<DictionaryVo> dicVos = dicMap.get(value);
	        		String cellContent = ExcelUtil.generateCellContent(dicVos);
	        		cell.setCellValue(StrUtil.isBlank(cellContent) ? value : cellContent);
	        	}
	        }
		} catch (FileNotFoundException e) {
			throw new ServiceException("找不到模板文件！");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}
	
}
