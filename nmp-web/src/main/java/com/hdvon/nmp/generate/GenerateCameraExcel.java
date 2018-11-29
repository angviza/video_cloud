package com.hdvon.nmp.generate;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.generate.util.CheckTypeEnum;
import com.hdvon.nmp.generate.util.ExcelUtil;
import com.hdvon.nmp.generate.util.StyleUtil;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DictionaryVo;

import cn.hutool.core.util.StrUtil;

public class GenerateCameraExcel {
	
	private static final int cursRow = 2;//第一行到数据行的偏移行数（表头以上的行数）
	private static final int[] validCols= {0,1,2,3,4,5,6};//设备编号、设备名称
	/**
	 * 根据导入的文件对象获得导入的数据
	 * @param file
	 * @param titles
	 * @param titleNames
	 * @param cols
	 * @return
	 */
	public static Map<String,Object> generateCameraExcel(MultipartFile file, List<CheckAttributeVo> checkAttrs, int headNo, Map<String,List<DictionaryVo>> dicMap, Map<String,String> dicKeyMap){
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String originalName = file.getOriginalFilename();
		Workbook workbook= null;
		try {
			if(FileUtil.isExcel2007(originalName)) {
				workbook = new XSSFWorkbook(inputStream);
			}else if(FileUtil.isExcel2003(originalName)) {
				workbook = new HSSFWorkbook(inputStream);
			}     
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(workbook == null) {
			throw new ServiceException("请选择正确的excel文件！");
		}
        //获取第一个sheet页    
        Sheet sheet = workbook.getSheetAt(0);        
        //容器    
        List<Map<String, String>> excelData = new ArrayList<Map<String, String>>();  
        int lastRowNum = sheet.getLastRowNum();//最后一行行号
        Row headRow = sheet.getRow(1);//除去
        int lastCellNum = headRow.getLastCellNum();//最后一列列号
        int maxCols = checkAttrs.size();
        if(lastCellNum != maxCols) {
        	throw new ServiceException("表头列数不对，请检查！");
        }
        ExcelUtil.checkColHead(headRow, 0, checkAttrs, lastCellNum, cursRow);
        //遍历行 从下标第2行开始（去除下表第0行说明和第1行标题）
        for (int i = 0; i < lastRowNum+1-cursRow; i++) {
			Row row= sheet.getRow(i+cursRow);
            if(row!=null){
            	Map<String,String> map = new HashMap<String,String>();
            	if(ExcelUtil.checkBlankRow(row)) continue;
				//遍历列
               for(int j=0; j<lastCellNum; j++) {
            	   Cell headCell = headRow.getCell(j);
            	   Cell cell = row.getCell(j);
            	   CheckAttributeVo checkVo = checkAttrs.get(j);
            	   String value = ExcelUtil.checkCellValue(cell, headCell, i, j, checkVo, dicMap, dicKeyMap, cursRow);
            	   map.put(checkVo.getAttr(), value);
               }
               excelData.add(map);
            }    
        }    
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("excelData", excelData);
        result.put("validCols", validCols);
        result.put("cursRow", cursRow);
        return result;    
	}
	
	/**
	 * 数据量可能大于65535，导出到2007以上版本excel
	 * @param request
	 * @param response
	 * @param result
	 * @param titleNames
	 * @param titles
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	public static void export2007Data(SXSSFWorkbook xwb, int sheetSize, int flushRows, List<Map<String,Object>> result,
			List<String> titleNames, List<String> titles, String templateName, HSSFRow explainRow, int colWidth) throws IOException {

		int count = result.size();
		int sheets = count/sheetSize+(count%sheetSize > 0 ? 1:0);
		for(int i=0;i<sheets;i++) {
			SXSSFSheet sheet = xwb.createSheet();
			sheet.setDefaultColumnWidth(colWidth);
			SXSSFRow row0 =sheet.createRow(0);//创建下标第0行说明行，并将模板中对应单元格的说明填充进去
			for(int j=0;j<titles.size();j++) {
				String cellVal = explainRow.getCell(j) == null ? "" : explainRow.getCell(j).getStringCellValue();
				SXSSFCell cell = row0.createCell(j);
				cell.setCellValue(cellVal);
				cell.setCellStyle(StyleUtil.explainStyle(xwb));
			}
			
			SXSSFRow row1 =sheet.createRow(1);//创建下表第1行表头行
			for(int j=0;j<titleNames.size();j++) {
				SXSSFCell cell = row1.createCell(j);
			    cell.setCellValue(titleNames.get(j));
			    cell.setCellStyle(StyleUtil.headStyle(xwb));
			}
			
			List<Map<String,Object>> subList = result.subList(i*sheetSize, (i+1)*sheetSize > count ? count : (i+1)*sheetSize - 1);
			//导出数据
			for(int j=0;j<subList.size();j++) {
				SXSSFRow row2 = sheet.createRow(2+j);//从下表第2行开始创建数据行
				Map<String,Object> map = subList.get(j);
				for(int k=0;k<titles.size();k++) {
					SXSSFCell cell = row2.createCell(k);
					if(map.get(titles.get(k)) !=null) {
						cell.setCellValue(map.get(titles.get(k)).toString());
					}else {
						cell.setCellValue("");
					}
				}
				if(j%flushRows == 0) {
					sheet.flushRows();
				}
			}
		}
	}
	
}
