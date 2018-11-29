package com.hdvon.nmp.controller.system;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.ISipLogService;
import com.hdvon.nmp.service.IUserLogService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.UserLogVo;
import com.hdvon.nmp.vo.sip.SipLogVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/userLog",tags="用户行为日志模块",description="用户行为的查询")
@RestController
@RequestMapping("/userLog")
@Slf4j
public class UserLogController extends BaseController {
	
	@Reference
	private IUserLogService userLogService;
	
	@Reference
	private ISipLogService sipLogService;
	
	@ApiOperation(value="分页查询用户行为日志")
	@ApiImplicitParams({
		@ApiImplicitParam(value="操作人",name="name"),
		@ApiImplicitParam(value="操作人账号",name="account"),
		@ApiImplicitParam(value="操作类型",name="type"),
		@ApiImplicitParam(value="操作模块",name="menuId"),
		@ApiImplicitParam(value="操作区域",name="operationObject"),
		@ApiImplicitParam(value="操作开始时间",name="startDate"),
		@ApiImplicitParam(value="操作结束时间",name="endDate"),
	})
	@GetMapping(value = "/getUserLogPage")
    public ApiResponse<PageInfo<UserLogVo>> getUserLogPage(String name, String account,String type,
    		String menuId,String operationObject,String startDate,String endDate ,PageParam pageParam) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("name",name);
		map.put("account", account);
		map.put("type", type);
		map.put("menuId", menuId);
		map.put("operationObject", operationObject);
		map.put("dictionaryType", "operateType");//字典的操作类型
		PageInfo<UserLogVo> page = userLogService.getUserLogPage(map,pageParam);
		return new ApiResponse().ok().setData(page);
    }
	
	@ApiOperation(value="导出用户行为日志")
	@ApiImplicitParams({
		@ApiImplicitParam(value="操作人",name="name"),
		@ApiImplicitParam(value="操作人账号",name="account"),
		@ApiImplicitParam(value="操作类型",name="type"),
		@ApiImplicitParam(value="操作模块",name="menuId"),
		@ApiImplicitParam(value="操作区域",name="operationObject"),
		@ApiImplicitParam(value="操作开始时间",name="startDate"),
		@ApiImplicitParam(value="操作结束时间",name="endDate"),
	})
	@GetMapping(value = "/getUserLogExport")
    public ApiResponse<Object> getUserLogExport(String name, String account,String type,
    		String menuId,String operationObject,String startDate,String endDate,HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("name",name);
		map.put("account", account);
		map.put("type", type);
		map.put("menuId", menuId);
		map.put("dictionaryType", "operateType");//字典的操作类型
		List<Map<String,Object>> list = userLogService.getUserLogList(map);
		String[] titleNames = new String[]{"姓名","账号","操作模块","操作类型","操作时间","操作内容"};
		String[] titles = new String[]{"name","account","menuName","typeName","operation_time","content"};
		SimpleDateFormat sdf2 =  new SimpleDateFormat("yyyyMMdd");
		String fileName=sdf2.format(new Date())+"_user_log.xls";
		try {
			exportData(request, response, list, titleNames,titles,fileName, "用户行为日志");
		}catch (Exception e) {
			return new ApiResponse().error("导出失败！");
		}
		return new ApiResponse().ok();
    }
	
	
	
	@ApiOperation(value="分页查询设备日志")
	@ApiImplicitParams({
		@ApiImplicitParam(value="操作人",name="name"),
		@ApiImplicitParam(value="操作人账号",name="account"),
		@ApiImplicitParam(value="设备名称",name="deviceName"),
		@ApiImplicitParam(value="设备编码",name="deviceCode"),
		@ApiImplicitParam(value="操作开始时间",name="startDate"),
		@ApiImplicitParam(value="操作结束时间",name="endDate"),
	})
	@GetMapping(value = "/getSipLogPage")
    public ApiResponse<PageInfo<SipLogVo>> getUserLogPage(String name, String account,String deviceName,
    		String deviceCode,String startDate,String endDate ,PageParam pageParam) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("name",name);
		map.put("account", account);
		map.put("deviceName", deviceName);
		map.put("deviceCode", deviceCode);
		PageInfo<SipLogVo> page = sipLogService.getSipLogPage(map,pageParam);
		return new ApiResponse().ok().setData(page);
    }
	

	@ApiOperation(value="导出用户行为日志")
	@ApiImplicitParams({
		@ApiImplicitParam(value="操作人",name="name"),
		@ApiImplicitParam(value="操作人账号",name="account"),
		@ApiImplicitParam(value="设备名称",name="deviceName"),
		@ApiImplicitParam(value="设备编码",name="deviceCode"),
		@ApiImplicitParam(value="操作开始时间",name="startDate"),
		@ApiImplicitParam(value="操作结束时间",name="endDate"),
	})
	@GetMapping(value = "/getSipLogExport")
    public ApiResponse<Object> getUserLogExport(String name, String account,String deviceCode,
    		String deviceName,String startDate,String endDate,HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("name",name);
		map.put("account", account);
		map.put("deviceName", deviceName);
		map.put("deviceCode", deviceCode);
		List<Map<String,Object>> list = sipLogService.getSipLogMap(map);
		String[] titleNames = new String[]{"账号","姓名","设备名称","设备编码","操作时间","操作内容"};
		String[] titles = new String[]{"account","name","deviceName","deviceCode","reqTime","content"};
		try {
			 FileUtil.exportExcel(response,"操作设备日志",titles,list);
		}catch (Exception e) {
			return new ApiResponse().error("导出失败！");
		}
		return new ApiResponse().ok();
    }
	
	/**
	 * 导出数据
	 * @param request
	 * @param response
	 * @param result
	 * @param titleNames
	 * @param titles
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	private void exportData(HttpServletRequest request, HttpServletResponse response, List<Map<String,Object>> result,
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
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 3500);
     	sheet.setColumnWidth(4, 4500);
     	sheet.setColumnWidth(5, 50000);
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
		//输出Excel文件
		OutputStream output = response.getOutputStream();
		try {
			response.reset();
		    response.setHeader("Content-disposition", "attachment; filename="+fileName);
		    response.setContentType("application/msexcel;charset=utf-8");  
		    response.setCharacterEncoding("utf-8");
		    
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

}
