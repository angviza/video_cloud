package com.hdvon.nmp.generate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.generate.util.ExcelUtil;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DepartmentVo;

public class GenerateDepartmentExcel {
	private static final int cursRow = 2;//第一行到数据行的偏移行数（表头以上的行数）
	private static final int cursCol = 2;//父编号偏移编号的列数，在编号右边为正，在编号左边为负
	private static final int[] validCols= {0,1,2,3};//编号、名称、父编号依次的列号
	
	/**
	 * 导入部门数据
	 * @param file
	 * @param titles
	 * @return
	 * @throws IOException
	 */
	public static Map<String,Object> generateDeptData(MultipartFile file, List<CheckAttributeVo> checkAttrs) throws IOException {
		InputStream inputStream = file.getInputStream();
		String originalName = file.getOriginalFilename();
		Workbook workbook= null;
		if(FileUtil.isExcel2007(originalName)) {
			workbook = new XSSFWorkbook(inputStream);
		}else if(FileUtil.isExcel2003(originalName)) {
			workbook = new HSSFWorkbook(inputStream);
		}     
		if(workbook == null) {
			throw new ServiceException("请选择正确的excel文件！");
		}
        //获取第一个sheet页    
        Sheet sheet = workbook.getSheetAt(0);   
        
        Row headRow = sheet.getRow(1);
        int lastCellNum = headRow.getLastCellNum();//最后一列列号
		if(lastCellNum != checkAttrs.size()) {
        	throw new ServiceException("表头列数不对，请检查！");
        }
        ExcelUtil.checkColHead(headRow, 0, checkAttrs, lastCellNum, cursRow);
        
        //容器    
        List<Map<String, String>> excelData = new ArrayList<Map<String, String>>();      
        for (int i = 0; i < sheet.getLastRowNum()+1-cursRow; i++) {
			Row row= sheet.getRow(i+cursRow);
            if(row!=null){
        		Map<String,String> map = new HashMap<String,String>();
        		if(ExcelUtil.checkBlankRow(row)) continue;
				//遍历列
               //for(int j=0; j<row.getLastCellNum(); j++) { // 若最后一列为空，此循环有误
            	for(int j=0; j<lastCellNum; j++) {
            	   Cell headCell = headRow.getCell(j);
            	   Cell cell = row.getCell(j);
            	   CheckAttributeVo checkVo = checkAttrs.get(j);
            	   String value = ExcelUtil.checkCellValue(cell, headCell, i, j, checkVo, null, null, cursRow);
            	   map.put(checkVo.getAttr(), value);
               }
            	excelData.add(map);
            }    
        }    
        Map<String,List<String>> treeData = ExcelUtil.checkTreeData(excelData, TreeType.DEPARTMENT.getVal(), validCols, null, null, cursRow, cursCol);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("excelData", excelData);
        map.put("treeData", treeData);
        map.put("validCols", validCols);
        map.put("cursRow", cursRow);
        return map;    
	}
	/**
	 * 导出部门数据
	 * @param response
	 * @param titles
	 * @param fileName
	 * @param result
	 * @throws IOException
	 */
	public static void exportDeptData(HttpServletResponse response, String[] titles, String[] titleNames, String fileName, List<Map<String,Object>> result) throws IOException {
		HSSFWorkbook wb;
		OutputStream output = null;
		String tempName = fileName;
		try {
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			fileName +="_"+df.format(date)+".xls";

			wb= new HSSFWorkbook();
			HSSFSheet sh = wb.createSheet();

			// 设置列宽
			for(int i = 0; i < titles.length; i++){
				sh.setColumnWidth( i, 256*20+184);
			}

			// 第一行表头标题，CellRangeAddress 参数：行 ,行, 列,列
			HSSFRow row = sh.createRow(0);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(new HSSFRichTextString(tempName));
			cell.setCellStyle(fontStyle(wb));
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0,titles.length-1));

			// 第二行
			HSSFRow row3 = sh.createRow(1);

			// 第二行的列
			for(int i=0; i < titles.length; i++){
				cell = row3.createCell(i);
				cell.setCellValue(new HSSFRichTextString(titleNames[i]));
				cell.setCellStyle(fontStyle(wb));
			}

			for(int i=0;i<result.size();i++) {
				row = sh.createRow(i+2);
				Map<String,Object> map = result.get(i);
				for(int j=0;j < titles.length;j++) {
					cell = row.createCell(j);
					if(map.get(titles[j]) !=null) {
						cell.setCellValue(map.get(titles[j]).toString());
					}else {
						cell.setCellValue("");
					}
				}
			}

			output = response.getOutputStream();
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition", "attachment;charset=utf-8;filename="+fileName);
			response.setContentType("text/html;charset=utf-8");
			response.setContentType("application/msexcel");
			wb.write(output);
			output.flush();
			output.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取单元格的值
	 * @auther xuguocai
	 * 1、表头标题样式 2、表头列名样式
	 * @return HSSFCellStyle
	 */
	private  static HSSFCellStyle fontStyle(HSSFWorkbook wb){
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 14);
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setLocked(true);
		return style;
	}
	
	public static List<Map<String,Object>> transDeptEntityToMap(List<DepartmentVo> deptVos){
		List<Map<String,Object>> deptList = new ArrayList<>();
        for(DepartmentVo item : deptVos){
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("id", item.getId());
            tempMap.put("parentDepName", item.getParentDepName());
            tempMap.put("name", item.getName());
            tempMap.put("depCode", item.getDepCode());
            tempMap.put("parentDepCode", item.getParentDepCode());
            tempMap.put("orgName", item.getOrgName());
            tempMap.put("isConstructor", item.getIsConstructor() == null ? null : (item.getIsConstructor() == 1 ? "是" : "否"));
            tempMap.put("isBuilder", item.getIsBuilder() == null ? null : (item.getIsBuilder() == 1 ? "是" : "否"));
            tempMap.put("contactor", item.getContactor());
            tempMap.put("telelno", item.getTelelno());
            tempMap.put("mobile", item.getMobile());
            tempMap.put("address", item.getAddress());
            tempMap.put("description", item.getDescription());
            deptList.add(tempMap);
        }
	    return deptList;
	}
}
