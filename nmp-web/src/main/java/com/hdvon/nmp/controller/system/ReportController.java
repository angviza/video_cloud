package com.hdvon.nmp.controller.system;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.service.ICameraService;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.ICameragrouopService;
import com.hdvon.nmp.service.IDepartmentService;
import com.hdvon.nmp.service.IDeviceService;
import com.hdvon.nmp.service.IRepUseranalysisService;
import com.hdvon.nmp.service.ISysroleService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.service.IUserLogService;
import com.hdvon.nmp.service.IUserService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.DateHandlerUtils;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ReportResponseVo;
import com.hdvon.nmp.vo.SysroleVo;
import com.hdvon.nmp.vo.CameraGroupReportVo;
import com.hdvon.nmp.vo.CameragrouopVo;
import com.hdvon.nmp.vo.DepartmentVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.RepUseranalysisVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Api(value="/hotPoints",tags="报表管理模块",description="报表地址树模块")
@RestController
@RequestMapping("/hotPoints")
public class ReportController extends BaseController{
	
	@Reference
	private IUserLogService userLogService;
	
	@Reference
	private IDeviceService deviceService;
	
	@Reference
	private ICameragrouopService cameragrouopService;
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private IRepUseranalysisService repUseranalysisService;
	@Reference
	private IUserService userService;
	@Reference
	private IDepartmentService departmentService;
	@Reference
	private ISysroleService sysroleService;
	@Reference
	private ITreeNodeService treeNodeService;
	
	
	private Map<String, Object> initializeDate(String startDate, String endDate) {
		
		if (null == startDate || "".equals(startDate) || null == endDate || "".equals(endDate)) {
			Calendar cd = Calendar.getInstance();  
			Date fromDate = null;
			Date toDate = null;
			
			if (null == startDate || "".equals(startDate)) {
				
				if (null != endDate && !"".equals(endDate)) {
					cd.setTime(DateUtil.parse(DateUtil.format(DateUtil.parseDate(endDate), "yyyy-MM-dd"), "yyyy-MM-dd"));
				} else {
					cd.setTime(DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
				}
				
				toDate = cd.getTime();
				
				cd.add(Calendar.DATE, -30);//设置30天之前的日期
				fromDate = cd.getTime();
			} else {
				cd.setTime(DateUtil.parse(DateUtil.format(DateUtil.parseDate(startDate), "yyyy-MM-dd"), "yyyy-MM-dd"));
				fromDate = cd.getTime();
				
				cd.add(Calendar.DATE, 30);//设置30天之后的日期
				toDate = cd.getTime();
			}
			
			startDate = DateUtil.formatDate(fromDate);
			endDate = DateUtil.formatDate(toDate);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		return param;
	}
	
	
	private String checkDateInterval(String startDate, String endDate, Integer selectedType) {
		
		String error = "";
		//获取起始日期和结束日期之间的间隔天数
		long days = DateHandlerUtils.getDateNum(startDate, endDate, "yyyy-MM-dd");
		if (days > WebConstant.REPORT_DAYS) {
			if (selectedType.intValue() == 1) {
				error = "摄像机热度值统计的查询天数需在"+WebConstant.REPORT_DAYS+"天之内";
			}
			
			if (selectedType.intValue() == 2) {
				error = "摄像机组热度值统计的查询天数需在"+WebConstant.REPORT_DAYS+"天之内";
			}
		}
		
		return error;
	}
	
	
	private String checkCamera(String selectedItems, Integer selectedType) {
		
		String error = "";
		
		/**
		if(StrUtil.isBlank(selectedItems)) {
			if (selectedType.intValue() == 1) {
				error = "请选择一个或多个摄像机才能够生成图表";
				return error;
			}
			
			if (selectedType.intValue() == 2) {
				error = "请选择一个或多个摄像机组才能够生成图表";
				return error;
			}
		}
		**/
		
		if (StrUtil.isNotBlank(selectedItems)) {
			String items[] = selectedItems.split(",");
			if (selectedType.intValue() == 1) {
				if (items.length > WebConstant.CAMERA_NUM) {
					error = "最多只能选择"+WebConstant.CAMERA_NUM+"个摄像机";
					return error;
				}
			}
			
			if (selectedType.intValue() == 2) {
				if (items.length > WebConstant.CAMERA_GROUP_NUM) {
					error = "最多只能选择"+WebConstant.CAMERA_GROUP_NUM+"个摄像机组";
					return error;
				}
			}
		}
		
		return error;
	}
	
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value="历史统计摄像机访问热点列表")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="selectedItems",value="设备ID数组或者摄像机组",required=false), 
		 @ApiImplicitParam(name="name",value="设备名称",required=false),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="endDate",value="接受时间",required=false),
		 @ApiImplicitParam(name="selectedType",value="操作类型；1：摄像机   2：摄像机分组",required=false)
	 })
	@PostMapping(value = "/history")
    public ApiResponse<PageInfo<Object>> history(String selectedItems,String startDate,String endDate,
    		String name,Integer selectedType,PageParam pageParam) {
		UserVo userVo = getLoginUser();
		
		Map<String,Object> param = new HashMap<String,Object>();
		
		param.put("name", name);
		
		//初始化日期
		Map<String, Object> map = initializeDate(startDate, endDate);
		startDate = map.get("startDate").toString();
		endDate = map.get("endDate").toString();
		
		//检查日期范围
		String error = checkDateInterval(startDate, endDate, selectedType);
		if (!"".equals(error)) {
			return new ApiResponse().error(error);
		}
		
		startDate = startDate + " 00:00:00";
		endDate = endDate + " 23:59:59";
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		
		if (selectedType.intValue() == 1) {	//摄像机
			
			List<String> deviceIdList = null;
			if(StrUtil.isNotBlank(selectedItems)) {
				
				deviceIdList = new ArrayList<String>();
				
				String items[] = selectedItems.split(",");
				List<String> cameraIdList = Arrays.asList(items);
				
				for (int index = 0;index < cameraIdList.size();index++) {
					DeviceVo vo = deviceService.getDeviceByCameraId(cameraIdList.get(index));
					if (null != vo) {
						deviceIdList.add(vo.getId());
					}
				}
			}
			 
			if(userVo.getAccount().toLowerCase().equals("admin")) {//管理员返回所有
				param.put("isAdmin", 1); 
		    } else {
		    	param.put("userId", userVo.getId());
		    }
			
			param.put("deviceIdList", deviceIdList);
			
			PageInfo<ReportResponseVo> page = userLogService.getHistoryCamaraPage(param, pageParam);
	    	return new ApiResponse().ok().setData(page);
		}
		
		if (selectedType.intValue() == 2) {	//摄像机组
			
			List<String> groupIdList = null;
			//List<String> filterList = new ArrayList<String>();
			List<String> filterList1 = new ArrayList<String>();
			
			if(StrUtil.isNotBlank(selectedItems)) {
				String items[] = selectedItems.split(",");
				groupIdList = Arrays.asList(items);
			}
			
			/*
			for (int index = 0;index < groupIdList.size();index++) {
				int count = cameragrouopService.getCameragroupCountByPid(groupIdList.get(index));
				if (count == 0) {
					filterList.add(groupIdList.get(index));
				}
			}
			*/
			
			param.put("groupIdList", groupIdList);
			 
			if(!userVo.getAccount().toLowerCase().equals("admin")) {
				
				for (int index = 0;index < groupIdList.size();index++) {
		    		
	    			CameragrouopVo vo = cameragrouopService.getGroupById(groupIdList.get(index));
	    			if (null != vo) {
	    				List<CameraPermissionVo> lists = cameraService.getCameraGroupPermission(userVo.getId(), userVo.getAccount(), vo.getId(), null);
			    		if (null != lists && lists.size() > 0) {
			    			for (int idx = 0;idx < lists.size();idx++) {
			    				filterList1.add(lists.get(idx).getDeviceId());
				    		}
			    		}
	    			}
		    	}
				
				param.put("deviceIdList", filterList1);
		    }
			
			PageInfo<CameraGroupReportVo> page = userLogService.getGroupHistoryPage(param, pageParam);
	    	return new ApiResponse().ok().setData(page);
		}
		
		return new ApiResponse().ok();
    }
	
	
	@ApiOperation(value="历史访问摄像机次数(按天统计热度值)")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="selectedItems",value="设备ID数组或者摄像机组",required=false),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="endDate",value="接受时间",required=false),
		 @ApiImplicitParam(name="selectedType",value="操作类型；1：摄像机   2：摄像机分组",required=false)
	})
    @PostMapping(value = "/createHisHotLineChart")
    public ApiResponse<Map<String,Object>> createHisHotLineChart(String selectedItems,String startDate,String endDate,Integer selectedType) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			UserVo userVo = getLoginUser();
			
			//初始化日期
			Map<String, Object> map = initializeDate(startDate, endDate);
			startDate = map.get("startDate").toString();
			endDate = map.get("endDate").toString();
			
			//检查日期范围
			String error = checkDateInterval(startDate, endDate, selectedType);
			if (!"".equals(error)) {
				return new ApiResponse().error(error);
			}
			
			//检查摄像机
			String error1 = checkCamera(selectedItems, selectedType);
			if (!"".equals(error1)) {
				return new ApiResponse().error(error1);
			}
			
			/**
			 * 添加未选择摄像机的业务逻辑
			 * 2018/9/10
			 */
			/********************************************** Added by huanhongliang *********************************/
			String items[] = new String[]{};
			if (StrUtil.isNotBlank(selectedItems)) {
				items = selectedItems.split(",");
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//获取两日期之间的所有日期
			List<String> dates = DateHandlerUtils.getDateArr(startDate, endDate, "yyyy-MM-dd");
			
			Map<String,Object> param = new HashMap<String,Object>();
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Integer> hotPointsList = new ArrayList<Integer>();
			
			startDate = startDate + " 00:00:00";
			endDate = endDate + " 23:59:59";
			
			param.put("startDate", startDate);
			param.put("endDate", endDate);
			
			if (selectedType.intValue() == 1) {	//摄像机
				
				if(userVo.getAccount().toLowerCase().equals("admin")) {//管理员返回所有
					param.put("isAdmin", 1); 
				} else {
					param.put("userId", userVo.getId());
				}
				
				List<String> deviceIdList = new ArrayList<String>();
				
				/**
				 * 添加未选择摄像机时，查询默认的前十条摄像机的业务逻辑
				 * 2018/9/10
				 */
				if (items.length == 0) {
					
					param.put("deviceIdList", null);
					List<ReportResponseVo> list = userLogService.getDefaultHistoryCamara(param);
					if (null != list && list.size() > 0) {
						for (int index = 0; index < list.size(); index++) {
							deviceIdList.add(list.get(index).getId());
						}
					}
				} else {
					
					List<String> cameraIdList = Arrays.asList(items);
					
					for (int index = 0;index < cameraIdList.size();index++) {
						DeviceVo vo = deviceService.getDeviceByCameraId(cameraIdList.get(index));
						if (null != vo) {
							deviceIdList.add(vo.getId());
						}
					}
				}
				
				//遍歷選中的設備
				for (int idx = 0;idx < deviceIdList.size();idx++) {
					
					//清空掉上一个设备、每天统计出来的热度值
					hotPointsList = new ArrayList<Integer>();
					params = new HashMap<String,Object>();
					
					if (null != deviceIdList.get(idx) && !"".equals(deviceIdList.get(idx))) {
						
						DeviceVo vo = deviceService.getDeviceInfo(deviceIdList.get(idx));
						if (null != vo) {
							params.put("name", vo.getSbmc());
							param.put("deviceId", vo.getId());
							
							//遍历所有的日期
							for (int index = 0;index < dates.size();index++) {
								
								//根据日期、设备查询对应统计出来的热度值
								param.put("date", dates.get(index));
								//param.put("dateEmpty", null);
								List<Map<String, Object>> list = userLogService.getHistoryHotPoints(param);
								if (null == list || list.size() == 0) {
									hotPointsList.add(0);
								} else {
									hotPointsList.add(Integer.parseInt(list.get(0).get("hotPoints").toString()));
								}
								
								/**
								 * @author huanhongliang
								 * @date 2018/11/16
								 * @description 将剩下的日期为空的摄像机热点值为0的也一起查出来
								 */
								/**
								param.put("date", null);
								param.put("dateEmpty", "1");
								list = userLogService.getHistoryHotPoints(param);
								if (null != list && list.size() > 0) {
									hotPointsList.add(Integer.parseInt(list.get(0).get("hotPoints").toString()));
								}
								**/
							}
						}
					}
					
					//针对当前设备添加对应的热度值列表
					params.put("data", hotPointsList);
					
					mapList.add(params);
				}
			}
			
			if (selectedType.intValue() == 2) {	//摄像机组
				
				List<String> groupIdList = new ArrayList<String>();
				
				/**
				 * 添加未选择摄像机组时，查询默认的前十条摄像机组的业务逻辑
				 * 2018/9/10
				 */
				if (items.length == 0) {
					
					param.put("groupIdList", null);
					List<CameraGroupReportVo> list = userLogService.getDefaultGroupHistoryList(param);
					if (null != list && list.size() > 0) {
						for (int index = 0; index < list.size(); index++) {
							groupIdList.add(list.get(index).getId());
						}
					}
				} else {
					groupIdList = Arrays.asList(items);
				}
				
				//List<String> filterList = new ArrayList<String>();
				List<String> filterList1 = new ArrayList<String>();
				
				/*
				for (int index = 0;index < groupIdList.size();index++) {
                    int count = cameragrouopService.getCameragroupCountByPid(groupIdList.get(index));
					if (count == 0) {
						filterList.add(groupIdList.get(index));
					}
				}
				*/
				
				if(userVo.getAccount().toLowerCase().equals("admin")) {	//管理员返回所有
					
					//遍歷選中的组
					for (int idx = 0;idx < groupIdList.size();idx++) {
						
						//清空掉上一个組、每天统计出来的热度值
						hotPointsList = new ArrayList<Integer>();
						params = new HashMap<String,Object>();
						
						CameragrouopVo vo = cameragrouopService.getGroupById(groupIdList.get(idx));
						if (null != vo) {
							params.put("name", vo.getName());
							param.put("groupId", vo.getId());
							
							//遍历所有的日期
							for (int index = 0;index < dates.size();index++) {
								//根据日期、組查询对应统计出来的热度值
								param.put("date", dates.get(index));
								List<Map<String, Object>> list = userLogService.getGroupHistoryHotPoints(param);
								if (null == list || list.size() == 0) {
									hotPointsList.add(0);
								} else {
									hotPointsList.add(Integer.parseInt(list.get(0).get("hotPoints").toString()));
								}
							}
						}
						
						//针对当前組添加对应的热度值列表
						params.put("data", hotPointsList);
						
						mapList.add(params);
					}
			    } else {
			    	
			    	for (int index = 0;index < groupIdList.size();index++) {
			    		
			    		filterList1 = new ArrayList<String>();
			    		//清空掉上一个組、每天统计出来的热度值
						hotPointsList = new ArrayList<Integer>();
						params = new HashMap<String,Object>();
			    			
		    			CameragrouopVo vo = cameragrouopService.getGroupById(groupIdList.get(index));
		    			
		    			if (null != vo) {
							params.put("name", vo.getName());
							param.put("groupId", vo.getId());
							
							//获取当前自定义分组节点拥有的摄像机资源
							List<CameraPermissionVo> lists = cameraService.getCameraGroupPermission(userVo.getId(), userVo.getAccount(), vo.getId(), null);
				    		if (null != lists && lists.size() > 0) {
				    			for (int idx = 0;idx < lists.size();idx++) {
				    				filterList1.add(lists.get(idx).getDeviceId());
					    		}
				    			
								param.put("deviceIdList", filterList1);
								
								//遍历所有的日期
								for (int count = 0;count < dates.size();count++) {
									//根据日期、組查询对应统计出来的热度值
									param.put("date", dates.get(count));
									List<Map<String, Object>> list = userLogService.getGroupHistoryHotPoints(param);
									if (null == list || list.size() == 0) {
										hotPointsList.add(0);
									} else {
										hotPointsList.add(Integer.parseInt(list.get(0).get("hotPoints").toString()));
									}
								}
				    		}
				    		
		    			}
			    		
			    		//针对当前組添加对应的热度值列表
						params.put("data", hotPointsList);
						
						mapList.add(params);
			    	}
			    }
				
			}
			
			params = new HashMap<String,Object>();
			params.put("categories", dates);
			params.put("mapList", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="摄像机历史访问次数详情(按天统计热度值)")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="dataRowId",value="设备ID或者摄像机组ID",required=true),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="endDate",value="接受时间",required=false),
		 @ApiImplicitParam(name="selectedType",value="操作类型；1：摄像机   2：摄像机分组",required=false)
	})
    @PostMapping(value = "/createLineChartDetail")
    public ApiResponse<Map<String,Object>> createLineChartDetail(String dataRowId,String startDate,String endDate,Integer selectedType) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			UserVo userVo = getLoginUser();
			
			//初始化日期
			Map<String, Object> map = initializeDate(startDate, endDate);
			startDate = map.get("startDate").toString();
			endDate = map.get("endDate").toString();
			
			//检查日期范围
			String error = checkDateInterval(startDate, endDate, selectedType);
			if (!"".equals(error)) {
				return new ApiResponse().error(error);
			}
			
			//获取两日期之间的所有日期
			List<String> dates = DateHandlerUtils.getDateArr(startDate, endDate, "yyyy-MM-dd");
			
			Map<String,Object> param = new HashMap<String,Object>();
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Integer> hotPointsList = new ArrayList<Integer>();
			
			if (selectedType.intValue() == 1) {	//摄像机
				
				if(userVo.getAccount().toLowerCase().equals("admin")) {//管理员返回所有
					param.put("isAdmin", 1); 
				} else {
					param.put("userId", userVo.getId());
				}
				
				DeviceVo vo = deviceService.getDeviceInfo(dataRowId);
				if (null != vo) {
					params.put("name", vo.getSbmc());
					param.put("deviceId", vo.getId());
					
					//遍历所有的日期
					for (int index = 0;index < dates.size();index++) {
						//根据日期、设备查询对应统计出来的热度值
						param.put("date", dates.get(index));
						List<Map<String, Object>> list = userLogService.getHistoryHotPoints(param);
						if (null == list || list.size() == 0) {
							hotPointsList.add(0);
						} else {
							hotPointsList.add(Integer.parseInt(list.get(0).get("hotPoints").toString()));
						}
					}
				}
					
				//针对当前设备添加对应的热度值列表
				params.put("data", hotPointsList);
				
				mapList.add(params);
			}
			
			if (selectedType.intValue() == 2) {	//摄像机组
				
				List<String> filterList = new ArrayList<String>();
				
				CameragrouopVo vo = cameragrouopService.getGroupById(dataRowId);
				if (null != vo) {
					params.put("name", vo.getName());
					param.put("groupId", vo.getId());
					
					if (!userVo.getAccount().toLowerCase().equals("admin")) {
						
						//获取当前自定义分组节点拥有的摄像机资源
						List<CameraPermissionVo> lists = cameraService.getCameraGroupPermission(userVo.getId(), userVo.getAccount(), vo.getId(), null);
			    		if (null != lists && lists.size() > 0) {
			    			for (int idx = 0;idx < lists.size();idx++) {
			    				filterList.add(lists.get(idx).getDeviceId());
				    		}
			    			
							param.put("deviceIdList", filterList);
			    		}
					}
					
					//遍历所有的日期
					for (int index = 0;index < dates.size();index++) {
						//根据日期、組查询对应统计出来的热度值
						param.put("date", dates.get(index));
						List<Map<String, Object>> list = userLogService.getGroupHistoryHotPoints(param);
						if (null == list || list.size() == 0) {
							hotPointsList.add(0);
						} else {
							hotPointsList.add(Integer.parseInt(list.get(0).get("hotPoints").toString()));
						}
					}
				}
				
				//针对当前組添加对应的热度值列表
				params.put("data", hotPointsList);
				
				mapList.add(params);
			}
			
			params = new HashMap<String,Object>();
			params.put("categories", dates);
			params.put("mapList", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="分时段访问摄像机次数(按时段统计热度值)")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="selectedItems",value="设备ID数组或者摄像机组",required=false),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="endDate",value="接受时间",required=false),
		 @ApiImplicitParam(name="selectedType",value="操作类型；1：摄像机   2：摄像机分组",required=false)
	})
    @PostMapping(value = "/createPeriodHotLineChart")
    public ApiResponse<Map<String,Object>> createPeriodHotLineChart(String selectedItems,String startDate,String endDate,Integer selectedType) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			UserVo userVo = getLoginUser();
			
			//初始化日期
			Map<String, Object> map = initializeDate(startDate, endDate);
			startDate = map.get("startDate").toString();
			endDate = map.get("endDate").toString();
			
			//检查日期范围
			String error = checkDateInterval(startDate, endDate, selectedType);
			if (!"".equals(error)) {
				return new ApiResponse().error(error);
			}
			
			//检查摄像机
			String error1 = checkCamera(selectedItems, selectedType);
			if (!"".equals(error1)) {
				return new ApiResponse().error(error1);
			}
			
			/**
			 * 添加未选择摄像机的业务逻辑
			 * 2018/9/12
			 */
			/********************************************** Added by huanhongliang *********************************/
			String items[] = new String[]{};
			if (StrUtil.isNotBlank(selectedItems)) {
				items = selectedItems.split(",");
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//获取时间段列表
			List<String> periods = DateHandlerUtils.getPeriods();
			startDate = startDate + " 00:00:00";
			endDate = endDate + " 23:59:59";
			
			Map<String,Object> param = new HashMap<String,Object>();
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Integer> hotPointsList = new ArrayList<Integer>();
			
			param.put("startDate", startDate);
			param.put("endDate", endDate);
			
			if (selectedType.intValue() == 1) {	//摄像机
				
				if(userVo.getAccount().toLowerCase().equals("admin")) {//管理员返回所有
					param.put("isAdmin", 1); 
				} else {
					param.put("userId", userVo.getId());
				}
				
				List<String> deviceIdList = new ArrayList<String>();
				
				/**
				 * 添加未选择摄像机时，查询默认的前十条摄像机的业务逻辑
				 * 2018/9/12
				 */
				if (items.length == 0) {
					
					List<ReportResponseVo> list = userLogService.getDefaultHistoryCamara(param);
					if (null != list && list.size() > 0) {
						for (int index = 0; index < list.size(); index++) {
							deviceIdList.add(list.get(index).getId());
						}
					}
				} else {
					
					List<String> cameraIdList = Arrays.asList(items);
					
					for (int index = 0;index < cameraIdList.size();index++) {
						DeviceVo vo = deviceService.getDeviceByCameraId(cameraIdList.get(index));
						if (null != vo) {
							deviceIdList.add(vo.getId());
						}
					}
				}
				
				//遍歷選中的設備
				for (int idx = 0;idx < deviceIdList.size();idx++) {
					
					//清空掉上一个设备、每一个时段统计出来的热度值
					hotPointsList = new ArrayList<Integer>();
					params = new HashMap<String,Object>();
					
					if (null != deviceIdList.get(idx) && !"".equals(deviceIdList.get(idx))) {
						
						DeviceVo vo = deviceService.getDeviceInfo(deviceIdList.get(idx));
						if (null != vo) {
							
							params.put("name", vo.getSbmc());
							param.put("deviceId", vo.getId());
							
							String startTime = "";
							String endTime = "";
							
							//遍历所有的时间段
							for (int index = 0;index < periods.size();index++) {
								
								int hotPoints = 0;
								//获取时间段的起始时间和结束时间
								startTime = periods.get(index).split("-")[0];
								endTime = periods.get(index).split("-")[1];
								
								param.put("startTime", startTime);
								param.put("endTime", endTime);
								
								//查询的起始日期和结束日期
								param.put("startDate", startDate);
								param.put("endDate", endDate);
								
								List<Map<String, Object>> list = userLogService.getCameraPeriodHotPoints(param);
								if (null == list || list.size() == 0) {
									hotPointsList.add(0);
								} else {
									//累加某一个时段内所有的热点值
									for (int count = 0;count < list.size();count++) {
										hotPoints += Integer.parseInt(list.get(count).get("hotPoints").toString());
									}
									hotPointsList.add(hotPoints);
								}
							}
						}
					}
					
					//针对当前设备添加对应的热度值列表
					params.put("data", hotPointsList);
					
					mapList.add(params);
				}
			}
			
			if (selectedType.intValue() == 2) {	//摄像机组
				
				List<String> groupIdList = new ArrayList<String>();
				
				/**
				 * 添加未选择摄像机组时，查询默认的前十条摄像机组的业务逻辑
				 * 2018/9/12
				 */
				if (items.length == 0) {
					
					List<CameraGroupReportVo> list = userLogService.getDefaultGroupHistoryList(param);
					if (null != list && list.size() > 0) {
						for (int index = 0; index < list.size(); index++) {
							groupIdList.add(list.get(index).getId());
						}
					}
				} else {
					groupIdList = Arrays.asList(items);
				}
				
				//List<String> filterList = new ArrayList<String>();
				List<String> filterList1 = new ArrayList<String>();
				
				/*
				for (int index = 0;index < groupIdList.size();index++) {
					//查找出摄像机组的叶子节点
                    int count = cameragrouopService.getCameragroupCountByPid(groupIdList.get(index));
					if (count == 0) {
						filterList.add(groupIdList.get(index));
					}
				}
				*/
				
				if(userVo.getAccount().toLowerCase().equals("admin")) {	//管理员返回所有
					
					//遍歷選中的组叶子节点列表
					for (int idx = 0;idx < groupIdList.size();idx++) {
						
						//清空掉上一个組、每一个时段内统计出来的热度值
						hotPointsList = new ArrayList<Integer>();
						params = new HashMap<String,Object>();
						
						CameragrouopVo vo = cameragrouopService.getGroupById(groupIdList.get(idx));
						if (null != vo) {
							
							params.put("name", vo.getName());
							param.put("groupId", vo.getId());
							
							String startTime = "";
							String endTime = "";
							
							//遍历所有的时段
							for (int index = 0;index < periods.size();index++) {
								
								int hotPoints = 0;
								//获取时间段的起始时间和结束时间
								startTime = periods.get(index).split("-")[0];
								endTime = periods.get(index).split("-")[1];
								
								param.put("startTime", startTime);
								param.put("endTime", endTime);
								
								//查询的起始日期和结束日期
								param.put("startDate", startDate);
								param.put("endDate", endDate);
								
								List<Map<String, Object>> list = userLogService.getCameraGroupPeriodHotPoints(param);
								if (null == list || list.size() == 0) {
									hotPointsList.add(0);
								} else {
									//累加某一个时段内所有的热点值
									for (int count = 0;count < list.size();count++) {
										hotPoints += Integer.parseInt(list.get(count).get("hotPoints").toString());
									}
									hotPointsList.add(hotPoints);
								}
							}
						}
						
						//针对当前組添加对应的热度值列表
						params.put("data", hotPointsList);
						
						mapList.add(params);
					}
			    } else {
			    	
			    	for (int index = 0;index < groupIdList.size();index++) {
			    		
			    		//清空分组节点中上一个组拥有的摄像机资源
			    		filterList1 = new ArrayList<String>();
			    		
			    		//清空掉上一个組、每一个时段内统计出来的热度值
						hotPointsList = new ArrayList<Integer>();
						params = new HashMap<String,Object>();
			    			
		    			CameragrouopVo vo = cameragrouopService.getGroupById(groupIdList.get(index));
		    			
		    			if (null != vo) {
		    				
							params.put("name", vo.getName());
							param.put("groupId", vo.getId());
							
							String startTime = "";
							String endTime = "";
							
							//获取当前自定义分组节点拥有的摄像机资源
							List<CameraPermissionVo> lists = cameraService.getCameraGroupPermission(userVo.getId(), userVo.getAccount(), vo.getId(), null);
				    		if (null != lists && lists.size() > 0) {
				    			for (int idx = 0;idx < lists.size();idx++) {
				    				filterList1.add(lists.get(idx).getDeviceId());
					    		}
				    			
								param.put("deviceIdList", filterList1);
								
								//遍历所有的时段
								for (int count = 0;count < periods.size();count++) {
									
									int hotPoints = 0;
									//获取时间段的起始时间和结束时间
									startTime = periods.get(index).split("-")[0];
									endTime = periods.get(index).split("-")[1];
									
									param.put("startTime", startTime);
									param.put("endTime", endTime);
									
									//查询的起始日期和结束日期
									param.put("startDate", startDate);
									param.put("endDate", endDate);
									
									List<Map<String, Object>> list = userLogService.getCameraGroupPeriodHotPoints(param);
									if (null == list || list.size() == 0) {
										hotPointsList.add(0);
									} else {
										//累加某一个时段内所有的热点值
										for (int idx = 0;idx < list.size();idx++) {
											hotPoints += Integer.parseInt(list.get(idx).get("hotPoints").toString());
										}
										hotPointsList.add(hotPoints);
									}
								}
				    		}
				    		
		    			}
			    		
			    		//针对当前組添加对应的热度值列表
						params.put("data", hotPointsList);
						
						mapList.add(params);
			    	}
			    }
				
			}
			
			params = new HashMap<String,Object>();
			List<String> hours = new ArrayList<String>();
			
			for (int index = 0; index < periods.size(); index++) {
				hours.add(index + "时");
			}
			
			params.put("categories", hours);
			params.put("mapList", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="摄像机分时段访问次数详情(按时段统计热度值)")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="dataRowId",value="设备ID或者摄像机组ID",required=true),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="endDate",value="接受时间",required=false),
		 @ApiImplicitParam(name="selectedType",value="操作类型；1：摄像机   2：摄像机分组",required=false)
	})
    @PostMapping(value = "/createPeriodLineChartDetail")
    public ApiResponse<Map<String,Object>> createPeriodLineChartDetail(String dataRowId,String startDate,String endDate,Integer selectedType) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			UserVo userVo = getLoginUser();
			
			//初始化日期
			Map<String, Object> map = initializeDate(startDate, endDate);
			startDate = map.get("startDate").toString();
			endDate = map.get("endDate").toString();
			
			//检查日期范围
			String error = checkDateInterval(startDate, endDate, selectedType);
			if (!"".equals(error)) {
				return new ApiResponse().error(error);
			}
			
			//获取时间段列表
			List<String> periods = DateHandlerUtils.getPeriods();
			startDate = startDate + " 00:00:00";
			endDate = endDate + " 23:59:59";
			
			Map<String,Object> param = new HashMap<String,Object>();
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Integer> hotPointsList = new ArrayList<Integer>();
			
			if (selectedType.intValue() == 1) {	//摄像机
				
				if(userVo.getAccount().toLowerCase().equals("admin")) {//管理员返回所有
					param.put("isAdmin", 1); 
				} else {
					param.put("userId", userVo.getId());
				}
				
				DeviceVo vo = deviceService.getDeviceInfo(dataRowId);
				if (null != vo) {
					params.put("name", vo.getSbmc());
					param.put("deviceId", vo.getId());
					
					String startTime = "";
					String endTime = "";
					
					//遍历所有的时段
					for (int index = 0;index < periods.size();index++) {
						
						int hotPoints = 0;
						//获取时间段的起始时间和结束时间
						startTime = periods.get(index).split("-")[0];
						endTime = periods.get(index).split("-")[1];
						
						param.put("startTime", startTime);
						param.put("endTime", endTime);
						
						//查询的起始日期和结束日期
						param.put("startDate", startDate);
						param.put("endDate", endDate);
						
						List<Map<String, Object>> list = userLogService.getCameraPeriodHotPoints(param);
						if (null == list || list.size() == 0) {
							hotPointsList.add(0);
						} else {
							
							//累加某一个时段内所有的热点值
							for (int count = 0;count < list.size();count++) {
								hotPoints += Integer.parseInt(list.get(count).get("hotPoints").toString());
							}
							hotPointsList.add(hotPoints);
						}
					}
				}
					
				//针对当前设备添加对应的热度值列表
				params.put("data", hotPointsList);
				
				mapList.add(params);
			}
			
			if (selectedType.intValue() == 2) {	//摄像机组
				
				List<String> filterList = new ArrayList<String>();
				
				CameragrouopVo vo = cameragrouopService.getGroupById(dataRowId);
				if (null != vo) {
					params.put("name", vo.getName());
					param.put("groupId", vo.getId());
					
					String startTime = "";
					String endTime = "";
					
					if (!userVo.getAccount().toLowerCase().equals("admin")) {
						
						//获取当前自定义分组节点拥有的摄像机资源
						List<CameraPermissionVo> lists = cameraService.getCameraGroupPermission(userVo.getId(), userVo.getAccount(), vo.getId(), null);
			    		if (null != lists && lists.size() > 0) {
			    			for (int idx = 0;idx < lists.size();idx++) {
			    				filterList.add(lists.get(idx).getDeviceId());
				    		}
			    			
							param.put("deviceIdList", filterList);
			    		}
					}
					
					//遍历所有的时段
					for (int index = 0;index < periods.size();index++) {
						
						int hotPoints = 0;
						//获取时间段的起始时间和结束时间
						startTime = periods.get(index).split("-")[0];
						endTime = periods.get(index).split("-")[1];
						
						param.put("startTime", startTime);
						param.put("endTime", endTime);
						
						//查询的起始日期和结束日期
						param.put("startDate", startDate);
						param.put("endDate", endDate);
						
						List<Map<String, Object>> list = userLogService.getCameraGroupPeriodHotPoints(param);
						if (null == list || list.size() == 0) {
							hotPointsList.add(0);
						} else {
							
							//累加某一个时段内所有的热点值
							for (int idx = 0;idx < list.size();idx++) {
								hotPoints += Integer.parseInt(list.get(idx).get("hotPoints").toString());
							}
							hotPointsList.add(hotPoints);
						}
					}
				}
				
				//针对当前組添加对应的热度值列表
				params.put("data", hotPointsList);
				
				mapList.add(params);
			}
			
			params = new HashMap<String,Object>();
			List<String> hours = new ArrayList<String>();
			
			for (int index = 0; index < periods.size(); index++) {
				hours.add(index + "时");
			}
			
			params.put("categories", hours);
			params.put("mapList", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="实时统计摄像机访问热点列表")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="selectedItems",value="设备ID数组或者摄像机组",required=false), 
		 @ApiImplicitParam(name="name",value="设备名称",required=false),
		 @ApiImplicitParam(name="selectedType",value="操作类型；1：摄像机   2：摄像机分组",required=false)
	 })
	@PostMapping(value = "/getRealTimeCameraList")
    public ApiResponse<PageInfo<Object>> getRealTimeCameraList(String selectedItems,String name,Integer selectedType,PageParam pageParam) {
		UserVo userVo = getLoginUser();
		
		Map<String,Object> param = new HashMap<String,Object>();
		
		param.put("name", name);
		
		Calendar cal = Calendar.getInstance();
		String currentDate = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
		param.put("currentDate", currentDate);
		
		/**
		 * 添加未选择摄像机的业务逻辑
		 * 2018/9/13
		 */
		/********************************************** Added by huanhongliang *********************************/
		String items[] = new String[]{};
		if (StrUtil.isNotBlank(selectedItems)) {
			items = selectedItems.split(",");
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if (selectedType.intValue() == 1) {	//摄像机
			
			if(userVo.getAccount().toLowerCase().equals("admin")) {//管理员返回所有
				param.put("isAdmin", 1); 
		    } else {
		    	param.put("userId", userVo.getId());
		    }
			
			List<String> deviceIdList = new ArrayList<String>();
			
			/**
			 * 添加未选择摄像机时，查询默认的前50条摄像机的业务逻辑
			 * 2018/9/13
			 */
			if (items.length == 0) {
				
				List<ReportResponseVo> list = userLogService.getDefaultRealTimeCamera(param);
				if (null != list && list.size() > 0) {
					for (int index = 0; index < list.size(); index++) {
						deviceIdList.add(list.get(index).getId());
					}
				}
			} else {
				
				List<String> cameraIdList = Arrays.asList(items);
				
				for (int index = 0;index < cameraIdList.size();index++) {
					DeviceVo vo = deviceService.getDeviceByCameraId(cameraIdList.get(index));
					if (null != vo) {
						deviceIdList.add(vo.getId());
					}
				}
			}
			
			param.put("deviceIdList", deviceIdList);
			
			PageInfo<ReportResponseVo> page = userLogService.getRealTimeCameraPage(param, pageParam);
	    	return new ApiResponse().ok().setData(page);
		}
		
		if (selectedType.intValue() == 2) {	//摄像机组
			
			//List<String> filterList = new ArrayList<String>();
			List<String> filterList1 = new ArrayList<String>();
			List<String> groupIdList = new ArrayList<String>();
			
			/**
			 * 添加未选择摄像机组时，查询默认的前50条摄像机组的业务逻辑
			 * 2018/9/13
			 */
			if (items.length == 0) {
				
				List<CameraGroupReportVo> list = userLogService.getDefaultRealTimeCameraGroup(param);
				if (null != list && list.size() > 0) {
					for (int index = 0; index < list.size(); index++) {
						groupIdList.add(list.get(index).getId());
					}
				}
			} else {
				groupIdList = Arrays.asList(items);
			}
			
			/**
			for (int index = 0;index < groupIdList.size();index++) {
				//查找出摄像机组的叶子节点
                int count = cameragrouopService.getCameragroupCountByPid(groupIdList.get(index));
				if (count == 0) {
					filterList.add(groupIdList.get(index));
				}
			}
			*/
			
			param.put("groupIdList", groupIdList);
			 
			//if(!userVo.getAccount().toLowerCase().equals("admin")) {
				
				for (int index = 0;index < groupIdList.size();index++) {
		    		
	    			CameragrouopVo vo = cameragrouopService.getGroupById(groupIdList.get(index));
	    			if (null != vo) {
	    				
	    				//获取当前自定义分组节点拥有的摄像机资源
	    				List<CameraPermissionVo> lists = cameraService.getCameraGroupPermission(userVo.getId(), userVo.getAccount(), vo.getId(), null);
			    		if (null != lists && lists.size() > 0) {
			    			for (int idx = 0;idx < lists.size();idx++) {
			    				filterList1.add(lists.get(idx).getDeviceId());
				    		}
			    		}
	    			}
		    	}
				
				param.put("deviceIdList", filterList1);
		    //}
			
			PageInfo<CameraGroupReportVo> page = userLogService.getRealTimeCameraGroupPage(param, pageParam);
	    	return new ApiResponse().ok().setData(page);
		}
		
		return new ApiResponse().ok();
    }

	
	@ApiOperation(value="用户操作统计分析")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="departmentIds",value="部门ids",required=false), 
		 @ApiImplicitParam(name="account",value="用户账号名",required=false),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="endDate",value="接受时间",required=false),
		 @ApiImplicitParam(name="sortProp",value="排序字段",required=false),
		 @ApiImplicitParam(name="order",value="排序方式",required=false)
	 })
   @GetMapping(value = "/userAnalysis")
   public ApiResponse<List<RepUseranalysisVo>> userAnalysis(String departmentIds,String account,
		   String startDate,String endDate,String sortProp,String order,PageParam pageParam) {
		UserVo userVo = getLoginUser();
		PageInfo<RepUseranalysisVo> page=null;
		Map<String,Object> param = new HashMap<String,Object>();
		List<String> departmentId=new ArrayList<String>();
		if(StrUtil.isNotBlank(departmentIds)) {
			departmentId = Arrays.asList(departmentIds.split(","));
		}
		param.put("departmentIds", departmentId);
		param.put("account", account);
		param.put("startDate", startDate+" 00:00:00");
		param.put("endDate", endDate+" 23:59:59");
		if(StrUtil.isBlank(sortProp)) {
			param.put("sortProp", "total");
			param.put("order", "desc");	
		}else {
			param.put("sortProp", sortProp);
			if(order.contains("desc")) {
				param.put("order", "desc");	
			}else {
				param.put("order", "asc");	
			}
		}
		page=repUseranalysisService.getUseranalysisByadmin(param,pageParam);
		
   	   return new ApiResponse().ok().setData(page);
   }
	
	@ApiOperation(value="导出用户操作统计")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="departmentIds",value="部门ids",required=false), 
		 @ApiImplicitParam(name="account",value="用户账号名",required=false),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="endDate",value="接受时间",required=false),
		 @ApiImplicitParam(name="sortProp",value="排序字段",required=false),
		 @ApiImplicitParam(name="order",value="排序方式",required=false)
	 })
   @GetMapping(value = "/exportUserAnalysis")
   public ApiResponse<List<RepUseranalysisVo>> exportUserAnalysis(String departmentIds,String account,
		   String startDate,String endDate,String sortProp,String order) {
		UserVo userVo = getLoginUser();
		List<Map<String,Object>> listMap=null;
		Map<String,Object> param = new HashMap<String,Object>();
		List<String> departmentId=new ArrayList<String>();
		if(StrUtil.isNotBlank(departmentIds)) {
			departmentId = Arrays.asList(departmentIds.split(","));
		}
		param.put("departmentIds", departmentId);
		param.put("account", account);
		param.put("startDate", startDate+" 00:00:00");
		param.put("endDate", endDate+" 23:59:59");
		//默认排序
		if(StrUtil.isBlank(sortProp)) {
			param.put("sortProp", "total");
			param.put("order", "desc");	
		}else {
			param.put("sortProp", sortProp);
			if(order.contains("desc")) {
				param.put("order", "desc");	
			}else {
				param.put("order", "asc");	
			}
		}
		listMap=repUseranalysisService.exportUserAnalysis(param);
		
		String filename="用户操作情况";
		String[] titleNames = new String[]{"账号","所属部门","在线时长(分钟)","登录次数","点播次数","录像回看次数","下载次数","云台控制次数","总计(不含云台控制)"};
		String[] titles = new String[]{"account","departmentName","onlineTotal","loiginTotal","inviteTotal","replayTotal","downloadTotal","controlTotal","total"};
		try {
			exportData(request,response,listMap,titleNames,titles,filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
   	   return new ApiResponse().ok();
   }
	
	
	@ApiOperation(value="僵死用户统计")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="departmentIds",value="部门ids",required=false), 
		 @ApiImplicitParam(name="account",value="用户账号名",required=false),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="timeDate",value="多久不登录时间",required=false)
	 })
  @GetMapping(value = "/staticUser")
  public ApiResponse<List<UserVo>> staticUser(String departmentIds,String account,
		  String timeDate,PageParam pageParam) {
		UserVo userVo = getLoginUser();
		PageInfo<UserVo> page=null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("departmentId", departmentIds);
		param.put("account", account);
		param.put("timeDate", timeDate);
		page=userService.getStaticUser(param,pageParam);
		
  	   return new ApiResponse().ok().setData(page);
  }
	
	
	@ApiOperation(value="导出用户登录情况")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="departmentIds",value="部门ids",required=false), 
		 @ApiImplicitParam(name="account",value="用户账号名",required=false),
		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
		 @ApiImplicitParam(name="timeDate",value="多久不登录时间",required=false)
	 })
	 @GetMapping(value = "/exportStaticUser")
	 public ApiResponse<List<UserVo>> exportStaticUser(String departmentIds,String account,String startDate,String timeDate) {
		UserVo userVo = getLoginUser();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("departmentId", departmentIds);
		param.put("account", account);
		param.put("timeDate", timeDate);
		//为空默认一个月不登录
//		if(startDate ==null) {
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        Calendar c = Calendar.getInstance();
//			c.setTime(new Date());
//	        c.add(Calendar.MONTH, -1);
//	        Date m = c.getTime();
//	        String mon = format.format(m);
//	        param.put("startDate", mon);
//	        System.out.println("过去一个月："+mon);
//		}else {
//			param.put("startDate", startDate+" 23:59:59");
//		}
		List<Map<String,Object>> userList=userService.exportStaticUser(param);
		String filename="用户登录情况";
		String[] titleNames = new String[]{"姓名","账号","身份证号","手机号","所属部门","最后登录时间","未登录本系统天数"};
		String[] titles = new String[]{"name","account","card_id","mobile_phone","departmentName","last_login_date","lastLogin"};
		try {
			exportData(request,response,userList,titleNames,titles,filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
 	   return new ApiResponse().ok();
    }
	
	
    @ApiOperation(value="部门列表")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="departmentIds",value="部门ids",required=false)
	 })
	 @GetMapping(value = "/getDepartment")
	 public ApiResponse<List<DepartmentVo>> getDepartment(String departmentIds,PageParam pageParam) {
		List<String> listIds=new ArrayList<String>();
		UserVo userVo = getLoginUser();
		if(StrUtil.isNotBlank(departmentIds)) {
			listIds=Arrays.asList(departmentIds.split(","));
		}
		if(StrUtil.isBlank(departmentIds)) {
			List<DepartmentVo> demList=departmentService.getUserDepartments(userVo.getId());
			/*String pid="0";
		    List<DepartmentVo> demList=	departmentService.getDepartmentByPid(pid);*/
		    for(DepartmentVo deVo : demList) {
		    	listIds.add(deVo.getId());
		    }	
		}
		PageInfo<DepartmentVo> page=null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("departmentIds", listIds);
		
		page=departmentService.getDepartmentUserCount(param,pageParam);
		
	   return new ApiResponse().ok().setData(page);
     }
	
	
	 @ApiOperation(value="部门用户详细")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="departmentId",value="部门id",required=true)
	 })
	 @GetMapping(value = "/departmentUser")
	 public ApiResponse<List<UserVo>> departmentUser(String departmentId,PageParam pageParam) {
		 PageInfo<UserVo> pageInfo=null;
		 UserVo userVo = getLoginUser();
		 List<String> listIds=new ArrayList<String>();
		 if(StrUtil.isNotBlank(departmentId)) {
			listIds=Arrays.asList(departmentId.split(","));
		 }
		 if(StrUtil.isBlank(departmentId)) {
		    List<DepartmentVo> demList=departmentService.getUserDepartments(userVo.getId());
		    if(demList.size()>0) {
		    	listIds.add(demList.get(0).getId());
		   }
		 }
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("departmentIds", listIds);
		pageInfo = userService.getUsersByDepart(pageParam, param);
	   return new ApiResponse().ok().setData(pageInfo);
    }
	 
	 @ApiOperation(value="导出部门用户")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="departmentIds",value="部门ids",required=false)
	 })
	 @GetMapping(value = "/exportDepartmentUser")
	 public ApiResponse<List<DepartmentVo>> exportDepartmentUser(String departmentIds,
			 HttpServletRequest request, HttpServletResponse response) {
		List<String> listIds=new ArrayList<String>();
		UserVo userVo = getLoginUser();
		if(StrUtil.isNotBlank(departmentIds)) {
			listIds=Arrays.asList(departmentIds.split(","));
		}
		if(StrUtil.isBlank(departmentIds)) {
//			String pid="0";
//		    List<DepartmentVo> demList=	departmentService.getDepartmentByPid(pid);
		    List<DepartmentVo> demList=departmentService.getUserDepartments(userVo.getId());
		    for(DepartmentVo deVo : demList) {
		    	listIds.add(deVo.getId());
		    }	
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("departmentIds", listIds);
		List<Map<String,Object>> userList=userService.exportDepartmentUser(param);
		String filename="部门用户";
		String[] titleNames = new String[]{"姓名","账号","身份证号","手机号","最后登录时间","所属部门","所属部门上级名称"};
		String[] titles = new String[]{"name","account","card_id","mobile_phone","last_login_date","departmentName","parentDepName"};
		try {
			exportData(request,response,userList,titleNames,titles,filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	   return new ApiResponse().ok();
     }
	 
	 
	 @ApiOperation(value="角色分析统计")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="roleIds",value="角色ids",required=false)
	 })
	 @GetMapping(value = "/getSysrole")
	 public ApiResponse<List<SysroleVo>> getSysrole(String roleIds,PageParam pageParam) {
		List<String> listIds=new ArrayList<String>();
		UserVo userVo = getLoginUser();
		PageInfo<SysroleVo> page=null;
		if(StrUtil.isNotBlank(roleIds)) {
			
			listIds=Arrays.asList(roleIds.split(","));
		}else {
			List<String> userIds=new ArrayList<String>();
			userIds.add(userVo.getId());
			 List<SysroleVo> sysRole=userService.getSysrolesByUserId(userIds);
			 for(SysroleVo vo :sysRole) {
				 listIds.add(vo.getId()); 
			 }
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleIds", listIds);
		page=sysroleService.getRoleUserCount(param,pageParam);
	    return new ApiResponse().ok().setData(page);
     }
	 
	 @ApiOperation(value="导出角色用户")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="roleIds",value="角色ids",required=false)
	 })
	 @GetMapping(value = "/exportRoleUser")
	 public ApiResponse<List<DepartmentVo>> exportRoleUser(String roleIds,
			 HttpServletRequest request, HttpServletResponse response) {
		List<String> listIds=new ArrayList<String>();
		UserVo userVo = getLoginUser();
		if(StrUtil.isNotBlank(roleIds)) {
			listIds=Arrays.asList(roleIds.split(","));
		}else {
			List<String> userIds=new ArrayList<String>();
			userIds.add(userVo.getId());
			 List<SysroleVo> sysRole=userService.getSysrolesByUserId(userIds);
			 for(SysroleVo vo :sysRole) {
				 listIds.add(vo.getId()); 
			 }
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleIds", listIds);
		
		List<Map<String,Object>> userList=userService.exportRoleUser(param);
		String filename="角色用户";
		String[] titleNames = new String[]{"姓名","账号","身份证号","手机号","最后登录时间","所属角色","所属角色上级名称"};
		String[] titles = new String[]{"name","account","card_id","mobile_phone","last_login_date","roleName","parentName"};
		try {
			exportData(request,response,userList,titleNames,titles,filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	   return new ApiResponse().ok();
     }
	 
	 
	 private void exportData(HttpServletRequest request, HttpServletResponse response, List<Map<String,Object>> result,
			 String[] titleNames,String[] titles,String filename) throws IOException {
			//创建HSSFWorkbook对象(excel的文档对象)
		      HSSFWorkbook wb = new HSSFWorkbook();
			//建立新的sheet对象（excel的表单）
			HSSFSheet sheet=wb.createSheet();
			//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			//创建标题
			HSSFRow row =sheet.createRow(0);;
			for(int i=0;i<titleNames.length;i++) {
				HSSFCell cell = row.createCell(i);
			    cell.setCellValue(titleNames[i]);
			}
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
	     	sheet.setColumnWidth(4, 5000);
	     	sheet.setColumnWidth(5, 5000);
	     	sheet.setColumnWidth(6, 4000);
	     
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
			    response.setHeader("Content-disposition", "attachment; "
			    		+ "filename="+new String(filename.getBytes("GBK"), "ISO_8859_1") + ".xls");
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
