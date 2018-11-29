package com.hdvon.nmp.util;

import cn.hutool.core.util.StrUtil;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.vo.DepartmentImportVo;
import com.hdvon.nmp.vo.DictionaryImportVo;
import com.hdvon.nmp.vo.DictionaryTypeImportVo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtil <T> {

	/**
	 * 下载文件模板
	 * @param request
	 * @param response
	 * @param templateName
	 * @param fileName
	 */
	public static final void downloadTemplate(HttpServletRequest request, HttpServletResponse response, String templateName, String fileName ) {
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
	    OutputStream out;
	    // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
	    File file = new File(path);
	    try {
	        FileInputStream inputStream = new FileInputStream(file);
	        // 3.通过response获取OutputStream对象(out)
	        out = response.getOutputStream();
	        byte[] buffer = new byte[512];
	        int b = inputStream.read(buffer);
	        while (b != -1) {
	            // 4.写到输出流(out)中
	            out.write(buffer, 0, b);
	            b = inputStream.read(buffer);
	        }
	        inputStream.close();
	        out.close();
	        out.flush();
	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * 导出数据
	 * @param request
	 * @param response
	 * @param result
	 * @param titles
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	public static final void exportData(HttpServletRequest request, HttpServletResponse response, List<Map<String,Object>> result,
			String[] titleNames,String[] titles, String fileName, String sheetName) throws IOException {
		//创建HSSFWorkbook对象(excel的文档对象)
	      HSSFWorkbook wb = new HSSFWorkbook();
		//建立新的sheet对象（excel的表单）
		HSSFSheet sheet=wb.createSheet(sheetName);
		//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		//创建标题
		HSSFRow row =sheet.createRow(0);;
		for(int i=0;i<titleNames.length;i++) {
			HSSFCell cell = row.createCell(i);
		    cell.setCellValue(titleNames[i]);
		}
		//导出数据
		for(int i=1;i<result.size()+1;i++) {
			HSSFRow row2 = sheet.createRow(i);
			Map<String,Object> map = result.get(i-1);
			for(int j=0;j<titles.length;j++) {
				HSSFCell cell = row2.createCell(j);
				if(map.get(titles[j]) !=null) {
					cell.setCellValue(map.get(titles[j]).toString());
				}else {
					cell.setCellValue("");
				}
			}
		}
		//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		//sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
		 
		//输出Excel文件
	    OutputStream output = null;
		try {
			output = response.getOutputStream();
			response.reset();
		    response.setHeader("Content-disposition", "attachment; filename="+fileName);
		    response.setContentType("application/msexcel");        
		    wb.write(output);
		    output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(output != null) {
				output.close();
			}
		}
	   
	}

	/**
	 * 导出数据
	 * @auther xuguocai
	 * @param fileName 文件名
	 * @param titles  字段名
	 * @param result 导出数据
	 * @throws IOException
	 */
	public static void exportExcel(HttpServletResponse response,String fileName,String[] titles,List<Map<String,Object>> result){
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
				cell.setCellValue(new HSSFRichTextString(titles[i]));
				cell.setCellStyle(fontStyle(wb));
			}

			//填充数据的内容  i表示行，z表示数据库某表的数据大小，这里使用它作为遍历条件
			int i = 2, z = 0;
			while (z < result.size()) {
				row = sh.createRow(i);
				Map<String,Object> map = result.get(z);
				for(int j=0;j < titles.length;j++) {
					cell = row.createCell(j);
					if(map.get(titles[j]) !=null) {
						cell.setCellValue(map.get(titles[j]).toString());
					}else {
						cell.setCellValue("");
					}
				}
				i++;
				z++;
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
	 * 导入数据
	 * @param file
	 * @param titles
	 * @return
	 * @throws IOException
	 */
	public static final List<Map<String,Object>> importData(MultipartFile file, String[] titles) throws IOException {
		InputStream inputStream = file.getInputStream();
		String originalName = file.getOriginalFilename();
		Workbook workbook= null;
		if(isExcel2007(originalName)) {
			workbook = new XSSFWorkbook(inputStream);
		}else if(isExcel2003(originalName)) {
			workbook = new HSSFWorkbook(inputStream);
		}     
		if(workbook == null) {
			throw new ServiceException("请选择正确的excel文件！");
		}
        //获取第一个sheet页    
        Sheet sheet = workbook.getSheetAt(0);        
        //容器    
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();      
        //遍历行 从下标第一行开始（去除标题）
		int bb = sheet.getLastRowNum();
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			Row row= sheet.getRow(i);
			int aa= row.getLastCellNum();
            if(row!=null){
            	Map<String,Object> map = new HashMap<String,Object>();
				//遍历列
               //for(int j=0; j<row.getLastCellNum(); j++) { // 若最后一列为空，此循环有误
            	for(int j=0; j<titles.length; j++) {
            	   Cell cell = row.getCell(j);
            	   map.put(titles[j], getCellValue(cell));
               }
               ret.add(map);
            }    
        }    
        return ret;    
	}

	/** 
     * 是否是2003的excel，返回true是2003 
     *  
     * @param filePath 文件完整路径 
     * @return boolean true代表是2003 
     */  
    public static boolean isExcel2003(String filePath) {  
        return filePath.matches("^.+\\.(?i)(xls)$");  
    }  
  
    /** 
     * 是否是2007的excel，返回true是2007 
     *  
     * @param filePath 文件完整路径 
     * @return boolean true代表是2007 
     */  
    public static boolean isExcel2007(String filePath) {  
        return filePath.matches("^.+\\.(?i)(xlsx)$");  
    }  
	/**
	 * 导入数据字典数据
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public static List<DictionaryTypeImportVo> importDictionaryData(MultipartFile file) throws IOException{
		List<DictionaryTypeImportVo> list = new ArrayList<DictionaryTypeImportVo>();
		InputStream inputStream = file.getInputStream();
		String originalName = file.getOriginalFilename();
		Workbook workbook= null;
		if(isExcel2007(originalName)) {
			workbook = new XSSFWorkbook(inputStream);
		}else if(isExcel2003(originalName)) {
			workbook = new HSSFWorkbook(inputStream);
		}
		if(workbook == null) {
			throw new ServiceException("请选择正确的excel文件！");
		}
        //获取第一个sheet页    
        Sheet sheet = workbook.getSheetAt(0);        
        
        List<CellRangeAddress> cras = getCombineCell(sheet);    
        //遍历行 从下标第一行开始（去除标题）    
        for (int i = 1; i < sheet.getLastRowNum(); i++) {    
        	Row row= sheet.getRow(i);
  
        	DictionaryTypeImportVo typeVo = new DictionaryTypeImportVo();
        	String chName = getCellValue(row.getCell(0));
        	String enName = getCellValue(row.getCell(1));
        	
        	if(StrUtil.isBlank(chName)) {
        		throw new ServiceException("第["+i+"]行第1列字典类型名称不能为空！");
        	}
        	if(StrUtil.isBlank(enName)) {
        		throw new ServiceException("第["+i+"]行第2列字典类型英文名不能为空！");
        	}
        	typeVo.setChName(chName);
        	typeVo.setEnName(enName);
        	
        	List<DictionaryImportVo> items = new ArrayList<DictionaryImportVo>();  
            if(isMergedRegion(sheet,i,0)){  
                int lastRow = getRowNum(cras,sheet.getRow(i).getCell(0),sheet);  
                  
                for(;i<=lastRow;i++){  
                    row = sheet.getRow(i);  
                    DictionaryImportVo item = new DictionaryImportVo();  
                    item.setChName(getCellValue(row.getCell(2)));
                    item.setValue(getCellValue(row.getCell(3)));
                    items.add(item);  
                }  
                i--;  
            }else{  
                row = sheet.getRow(i);  
                DictionaryImportVo item = new DictionaryImportVo();
                item.setChName(getCellValue(row.getCell(2)));
                item.setValue(getCellValue(row.getCell(3)));
                items.add(item);  
            }  
            typeVo.setDictionarys(items);
            list.add(typeVo);
        }
		return list;
	}
	/**   
    * 判断指定的单元格是否是合并单元格   
    * @param sheet    
    * @param row 行下标   
    * @param column 列下标   
    * @return   
    */    
    public static boolean isMergedRegion(Sheet sheet,int row ,int column) {    
      int sheetMergeCount = sheet.getNumMergedRegions();    
      for (int i = 0; i < sheetMergeCount; i++) {    
        CellRangeAddress range = sheet.getMergedRegion(i);    
        int firstColumn = range.getFirstColumn();    
        int lastColumn = range.getLastColumn();    
        int firstRow = range.getFirstRow();    
        int lastRow = range.getLastRow();    
        if(row >= firstRow && row <= lastRow){    
            if(column >= firstColumn && column <= lastColumn){    
                return true;    
            }    
        }  
      }    
      return false;    
    }   
    /**
     * 合并单元格处理,获取合并行
     * @param sheet
     * @return
     */
    public static List<CellRangeAddress> getCombineCell(Sheet sheet)    
    {    
        List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();    
        //获得一个 sheet 中合并单元格的数量    
        int sheetmergerCount = sheet.getNumMergedRegions();    
        //遍历所有的合并单元格    
        for(int i = 0; i<sheetmergerCount;i++)     
        {    
            //获得合并单元格保存进list中    
            CellRangeAddress ca = sheet.getMergedRegion(i);    
            list.add(ca);    
        }    
        return list;    
    }  
    /**
     * @param listCombineCell
     * @param cell
     * @param sheet
     * @return
     */
    private static int getRowNum(List<CellRangeAddress> listCombineCell,Cell cell,Sheet sheet){  
        int xr = 0;  
        int firstC = 0;    
        int lastC = 0;    
        int firstR = 0;    
        int lastR = 0;    
        for(CellRangeAddress ca:listCombineCell)    
        {  
            //获得合并单元格的起始行, 结束行, 起始列, 结束列    
            firstC = ca.getFirstColumn();    
            lastC = ca.getLastColumn();    
            firstR = ca.getFirstRow();    
            lastR = ca.getLastRow();    
            if(cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR)     
            {    
                if(cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC)     
                {    
                    xr = lastR;  
                }   
            }    
              
        }  
        return xr;  
          
    }  
	/**    
    * 获取单元格的值    
    * @param cell    
    * @return    
    */      
    @SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell){      
        if(cell == null || cell.toString().trim().equals("")) return "";   
        
        String cellValue = "";
        int cellType = cell.getCellType();
        switch(cellType) {
        case Cell.CELL_TYPE_STRING:
        	cellValue = cell.getStringCellValue().trim();
        	cellValue = StrUtil.isBlank(cellValue) ? "" : cellValue;
        	break;
        case Cell.CELL_TYPE_BOOLEAN:
        	cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
        	break;
        case Cell.CELL_TYPE_NUMERIC:
        	if(HSSFDateUtil.isCellDateFormatted(cell)) {
        		
        	}else {
        		cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue()).trim();
        	}
        	break;
        default:
        	cellValue = "";
        	break;
        }
        
        return cellValue;      
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

}
