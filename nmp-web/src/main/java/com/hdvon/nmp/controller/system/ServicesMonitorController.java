package com.hdvon.nmp.controller.system;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.service.ICameraService;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.ICameraConnectionsService;
import com.hdvon.nmp.service.IDeviceService;
import com.hdvon.nmp.service.IPlatformInfoService;
import com.hdvon.nmp.service.IServicesInfoService;
import com.hdvon.nmp.service.IUserLogService;
import com.hdvon.nmp.service.IUserLoginInfoService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ServicesInfoVo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="/servicesMonitor",tags="后台服务监控报表模块",description="后台服务监控报表模块")
@RestController
@RequestMapping("/servicesMonitor")
public class ServicesMonitorController extends BaseController {
	
	@Reference
	private IUserLogService userLogService;
	
	@Reference
	private IDeviceService deviceService;
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private IServicesInfoService servicesInfoService;
	
	@Reference
	private ICameraConnectionsService cameraConnectionsService;
	
	@Reference
	private IUserLoginInfoService userLoginInfoService;
	
	@Reference
	private IPlatformInfoService platformInfoService;
	
	
	private Map<String, Object> initializeDate(int num, int type) {
		
		Calendar cd = Calendar.getInstance();  
		
		cd.setTime(DateUtil.parse(DateUtil.format(cd.getTime(), "yyyy-MM-dd HH:mm:ss")));
		Date toDate = cd.getTime();
		
		if (type == 1) {
			cd.add(Calendar.HOUR_OF_DAY, num);//设置N小时之前或之后的日期
		} else if (type == 2) {
			cd.add(Calendar.MINUTE, num);//设置N分钟之前或之后的日期
		}
		
		Date fromDate = cd.getTime();
		
		String startTime = DateUtil.format(fromDate, "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtil.format(toDate, "yyyy-MM-dd HH:mm:ss");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return param;
	}
		
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value="实时后台服务基本信息列表")
	@GetMapping(value = "/getRealtimeServicesInfoList")
    public ApiResponse<PageInfo<ServicesInfoVo>> getRealtimeServicesInfoList(PageParam pageParam) {
		
		Map<String,Object> param = new HashMap<String,Object>();
		
		Calendar cal = Calendar.getInstance();
		String currentDate = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
		param.put("currentDate", currentDate);
			
		PageInfo<ServicesInfoVo> page = servicesInfoService.getRealtimeServicesInfoPage(param, pageParam);
    	return new ApiResponse().ok().setData(page);
    }
	
	
	@ApiOperation(value="统计系统资源使用率(按最近1小时、最近8小时、最近24小时统计)")
    @PostMapping(value = "/createSysResourceLineChart")
    public ApiResponse<Map<String,Object>> createSysResourceLineChart() {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			Map<String, Object> map = null;
			String startTime = null;
			String endTime = null;
			
			//获取两日期之间的所有日期
			List<Integer> hours = new ArrayList<Integer>();
			hours.add(1);
			hours.add(8);
			hours.add(24);
			
			List<String> categories = new ArrayList<String>();
			categories.add("最近1小时");
			categories.add("最近8小时");
			categories.add("最近24小时");
			
			List<Map<String, Object>> list = null;
			
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Double> ratesList = new ArrayList<Double>();
			List<String> columns = new ArrayList<String>();
			
			Map<String,Object> param = new HashMap<String,Object>();
			
			list = servicesInfoService.getSystemResourceRates(param);
			if (null != list && list.size() > 0) {
				
				Set<Map.Entry<String, Object>> entrySet = list.get(0).entrySet();
				
				//遍历Map的实体
		        for (Map.Entry<String, Object> entry : entrySet) {
		        	
		        	columns.add(entry.getKey());
		        }
		        
		        //遍历系统资源
		        for (int idx = 0; idx < columns.size(); idx++) {
		        	
		        	params = new HashMap<String,Object>();
		        	
		        	if ("avg_cpu_use_rate".equals(columns.get(idx))) {
		        		params.put("name", "CPU使用率");
		        		params.put("resource", "CPU");
		        	}
		        	
		        	if ("avg_memory_use_rate".equals(columns.get(idx))) {
		        		params.put("name", "内存使用率");
		        		params.put("resource", "内存");
		        	}
		        	
		        	if ("avg_network_use_rate".equals(columns.get(idx))) {
		        		params.put("name", "网络使用率");
		        		params.put("resource", "网络");
		        	}
		        	
		        	if ("avg_disk_use_rate".equals(columns.get(idx))) {
		        		params.put("name", "磁盘使用率");
		        		params.put("resource", "磁盘");
		        	}
		        	
		        	//清空掉上一次统计出来的使用率
		        	ratesList = new ArrayList<Double>();
		        	
		        	//遍历最近1小时、最近8小时、最近24小时
		        	for (int index = 0; index < hours.size(); index++) {
		        		
						map = initializeDate(-hours.get(index), 1);
						startTime = map.get("startTime").toString();
						endTime = map.get("endTime").toString();
						
						param.put("startTime", startTime);
						param.put("endTime", endTime);
						
						list = servicesInfoService.getSystemResourceRates(param);
						if (null != list && list.size() > 0 && null != list.get(0)) {
							
							entrySet = list.get(0).entrySet();
							
							//遍历Set集合中每一个Map的实体，每一个Map实体存有key、value之间的映射关系
					        for (Map.Entry<String, Object> entry : entrySet) {
					        	
					        	//比较实体中的key和columns列表中的是否一样
				        		if (entry.getKey().equals(columns.get(idx))) {
				        			//此处代码在内部循环中只能被执行一次
					        		ratesList.add(Double.parseDouble(entry.getValue().toString()));
					        	}
					        	
					        }
						} else {
							ratesList.add(0.0);
						}
						
					}
		        	
		        	//针对当前系统资源添加对应的使用率列表
					params.put("data", ratesList);
		        	mapList.add(params);
		        }
			}
			
			params = new HashMap<String,Object>();
			params.put("categories", categories);
			params.put("mapList", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="系统资源使用率(按系统资源名称统计)")
    @PostMapping(value = "/computeSysResourceRate")
    public ApiResponse<Map<String,Object>> computeSysResourceRate() {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			Map<String, Object> map = null;
			String startTime = null;
			String endTime = null;
			
			//获取两日期之间的所有日期
			List<Integer> hours = new ArrayList<Integer>();
			hours.add(1);
			hours.add(8);
			hours.add(24);
			
			List<String> categories = new ArrayList<String>();
			categories.add("最近1小时");
			categories.add("最近8小时");
			categories.add("最近24小时");
			
			List<Map<String, Object>> list = null;
			
			List<String> columns = new ArrayList<String>();
			List<String> resources = new ArrayList<String>();
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			Map<String,Object> param = new HashMap<String,Object>();
			Map<String,Object> hoursMap = new HashMap<String,Object>();
			
			list = servicesInfoService.getSystemResourceRates(param);
			if (null != list && list.size() > 0) {
				
				Set<Map.Entry<String, Object>> entrySet = list.get(0).entrySet();
				
				//遍历Map的实体
		        for (Map.Entry<String, Object> entry : entrySet) {
		        	
		        	columns.add(entry.getKey());
		        	
		        	if ("avg_cpu_use_rate".equals(entry.getKey())) {
		        		resources.add("CPU");
        			}
        			
        			if ("avg_memory_use_rate".equals(entry.getKey())) {
        				resources.add("内存");
        			}
        			
        			if ("avg_network_use_rate".equals(entry.getKey())) {
        				resources.add("网络");
        			}
        			
        			if ("avg_disk_use_rate".equals(entry.getKey())) {
        				resources.add("磁盘");
        			}
		        }
		        
		        //遍历最近1小时、最近8小时、最近24小时
	        	for (int index = 0; index < hours.size(); index++) {
	        		
					map = initializeDate(-hours.get(index), 1);
					startTime = map.get("startTime").toString();
					endTime = map.get("endTime").toString();
					
					param.put("startTime", startTime);
					param.put("endTime", endTime);
					
					params = new HashMap<String,Object>();
					
					list = servicesInfoService.getSystemResourceRates(param);
					if (null != list && list.size() > 0 && null != list.get(0)) {
						
						entrySet = list.get(0).entrySet();
						
						//遍历Set集合中每一个Map的实体，每一个Map实体存有key、value之间的映射关系
				        for (Map.Entry<String, Object> entry : entrySet) {
				        	
				        	//遍历每一个key
					        for (int idx = 0; idx < columns.size(); idx++) {
					        	
					        	//比较实体中的key和columns列表中的是否一样
				        		if (entry.getKey().equals(columns.get(idx))) {
				        			
				        			//针对当前系统资源添加对应的使用率列表
									params.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
				        		}
					        }
					        
				        }
				        
					} else {
						params.put("avg_cpu_use_rate", 0);
						
						params.put("avg_memory_use_rate", 0);
						
						params.put("avg_network_use_rate", 0);
						
						params.put("avg_disk_use_rate", 0);
					}
					
					mapList.add(params);
				}
		        
			}
			
			params = new HashMap<String,Object>();
			params.put("categories", categories);
			params.put("mapList", mapList);
			params.put("columns", columns);
			params.put("resources", resources);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="统计平台实时使用情况")
    @PostMapping(value = "/createPlatInfoChart")
    public ApiResponse<Map<String,Object>> createPlatInfoChart() {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			//查询实时的用户在线数量统计最近的30分钟之内的即可
			Map<String, Object> map = initializeDate(-30, 2);
			Long count = userLoginInfoService.getOnlineUsersCount(map);
			
			if (null == count) {
				count = 0L;
			}
			
			//查询当天的平台使用情况
			Map<String, Object> param = new HashMap<String, Object>();
			Calendar cal = Calendar.getInstance();
			String currentDate = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
			param.put("currentDate", currentDate);
			
			List<Map<String, Object>> list = platformInfoService.getPlatInfoUseList(param);
			
			List<String> categories = new ArrayList<String>();
			List<Long> data = new ArrayList<Long>();
			
			categories.add("在线用户");
			data.add(count);
			
			if (null != list && list.size() > 0) {
				
				//获取操作类型
				for (Map<String, Object> mp : list) {
					
					categories.add(mp.get("operate_type_name").toString());
					data.add(Long.parseLong(mp.get("sum_amount").toString()));
				}
			} else {
				
				categories.add("实时监控视频");
				categories.add("录像回放视频");
				categories.add("录像下载的摄像机");
				
				data.add(0L);
				data.add(0L);
				data.add(0L);
			}
			
			params.put("categories", categories);
			params.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="统计平台历史使用情况(按最近1小时、最近8小时、最近24小时统计)")
    @PostMapping(value = "/createHisPlatInfoLineChart")
    public ApiResponse<Map<String,Object>> createHisPlatInfoLineChart() {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			List<Map<String, Object>> list = null;
			
			//获取两日期之间的所有日期
			List<Integer> hours = new ArrayList<Integer>();
			hours.add(1);
			hours.add(8);
			hours.add(24);
			
			List<String> categories = new ArrayList<String>();
			categories.add("最近1小时");
			categories.add("最近8小时");
			categories.add("最近24小时");
			
			List<String> items = new ArrayList<String>();
			List<Long> data = new ArrayList<Long>();
			
			Long count = null;
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			
			items.add("在线用户");
			list = platformInfoService.getPlatInfoUseList(map);
			
			if (null != list && list.size() > 0) {
				
				//获取操作类型
				for (Map<String, Object> mp : list) {
					items.add(mp.get("operate_type_name").toString());
				}
			} else {
				
				items.add("实时监控视频");
				items.add("录像回放视频");
				items.add("录像下载的摄像机");
			}
			
			
			//遍历数据项
			for (int index = 0; index < items.size(); index++) {
				
				data = new ArrayList<Long>();
				params = new HashMap<String,Object>();
				params.put("name", items.get(index));
				
				//遍历最近1小时、最近8小时、最近24小时
	        	for (int idx = 0; idx < hours.size(); idx++) {
	        		
	        		map = initializeDate(-hours.get(idx), 1);
	        		
	        		if ("在线用户".equals(items.get(index))) {
	        			
	        			count = userLoginInfoService.getOnlineUsersCount(map);
		    			
		    			if (null == count) {
		    				count = 0L;
		    			}
		    			
		    			data.add(count);
	        		} else {
	        			
	        			list = platformInfoService.getPlatInfoUseList(map);
		    			
		    			if (null != list && list.size() > 0) {
		    				//获取对应的数据项的值
		    				for (Map<String, Object> mp : list) {
		    					
		    					if (items.get(index).equals(mp.get("operate_type_name").toString())) {
		    						data.add(Long.parseLong(mp.get("sum_amount").toString()));
		    					}
		    				}
		    			} else {
		    				data.add(0L);
		    			}
	        		}
	    			
	        	}
	        	
	        	params.put("data", data);
	        	mapList.add(params);
			}
			
			params = new HashMap<String,Object>();
			
			//添加目录项即X轴所需的数据
			params.put("categories", categories);
			
			//添加数据项对应的数据列表
			params.put("mapList", mapList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="查询摄像机的实时监控连接数排名")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="rank",value="10：前10名； 50：前50名； 100：前100名",required=false)
	})
    @PostMapping(value = "/queryMonitorConnectionsRank")
    public ApiResponse<Map<String,Object>> queryMonitorConnectionsRank(Integer rank) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			if (null == rank) {
				rank = 10;
			}
			
			List<String> categories = new ArrayList<String>();
			List<Long> data = new ArrayList<Long>();
			
			List<Map<String, Object>> list = cameraConnectionsService.getMonitorConnectionsRank(rank);
			if (null != list && list.size() > 0) {
				
				for (int index = 0; index < list.size(); index++) {
					
					categories.add(list.get(index).get("device_name").toString());
					data.add(Long.parseLong(list.get(index).get("monitor_connections").toString()));
				}
			}
			
			Collections.reverse(categories);
			Collections.reverse(data);
			
			params.put("categories", categories);
			params.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="查询摄像机的录像回放连接数排名")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="rank",value="10：前10名； 50：前50名； 100：前100名",required=false)
	})
    @PostMapping(value = "/queryReplayConnectionsRank")
    public ApiResponse<Map<String,Object>> queryReplayConnectionsRank(Integer rank) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			if (null == rank) {
				rank = 10;
			}
			
			List<String> categories = new ArrayList<String>();
			List<Long> data = new ArrayList<Long>();
			
			List<Map<String, Object>> list = cameraConnectionsService.getReplayConnectionsRank(rank);
			if (null != list && list.size() > 0) {
				
				for (int index = 0; index < list.size(); index++) {
					
					categories.add(list.get(index).get("device_name").toString());
					data.add(Long.parseLong(list.get(index).get("replay_connections").toString()));
				}
			}
			
			Collections.reverse(categories);
			Collections.reverse(data);
			
			params.put("categories", categories);
			params.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@ApiOperation(value="查询摄像机的录像下载连接数排名")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="rank",value="10：前10名； 50：前50名； 100：前100名",required=false)
	})
    @PostMapping(value = "/queryDownloadConnectionsRank")
    public ApiResponse<Map<String,Object>> queryDownloadConnectionsRank(Integer rank) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			if (null == rank) {
				rank = 10;
			}
			
			List<String> categories = new ArrayList<String>();
			List<Long> data = new ArrayList<Long>();
			
			List<Map<String, Object>> list = cameraConnectionsService.getDownloadConnectionsRank(rank);
			if (null != list && list.size() > 0) {
				
				for (int index = 0; index < list.size(); index++) {
					
					categories.add(list.get(index).get("device_name").toString());
					data.add(Long.parseLong(list.get(index).get("download_connections").toString()));
				}
			}
			
			Collections.reverse(categories);
			Collections.reverse(data);
			
			params.put("categories", categories);
			params.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value="实时在线用户排行列表")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="rank",value="10：前10名； 50：前50名； 100：前100名",required=false)
	})
	@PostMapping(value = "/getRealtimeOnlineUsersRankList")
    public ApiResponse<PageInfo<Map<String, Object>>> getRealtimeOnlineUsersRankList(Integer rank, PageParam pageParam) {
		
		if (null == rank) {
			rank = 10;
		}
		
		//查询当天的平台使用情况
		Map<String, Object> param = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		String currentDate = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
		param.put("currentDate", currentDate);
		param.put("rank", rank);
		
		PageInfo<Map<String, Object>> page = userLoginInfoService.getOnlineUsersPage(param, pageParam);
		
    	return new ApiResponse().ok().setData(page);
    }
	
	
	@ApiOperation(value="查询实时在线用户排名")
	@ApiImplicitParams({
		 @ApiImplicitParam(name="rank",value="10：前10名； 50：前50名； 100：前100名",required=false)
	})
    @PostMapping(value = "/queryRealtimeOnlineUsersRank")
    public ApiResponse<Map<String,Object>> queryRealtimeOnlineUsersRank(Integer rank) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			
			if (null == rank) {
				rank = 10;
			}
			
			List<String> categories = new ArrayList<String>();
			List<Long> data = new ArrayList<Long>();
			
			//查询当天的平台使用情况
			Map<String, Object> param = new HashMap<String, Object>();
			Calendar cal = Calendar.getInstance();
			String currentDate = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
			param.put("currentDate", currentDate);
			param.put("rank", rank);
			
			List<Map<String, Object>> list = userLoginInfoService.getOnlineUsersList(param);
			
			if (null != list && list.size() > 0) {
				
				for (int index = 0; index < list.size(); index++) {
					
					categories.add(list.get(index).get("name").toString());
					data.add(Long.parseLong(list.get(index).get("amount").toString()));
				}
			}
			
			Collections.reverse(categories);
			Collections.reverse(data);
			
			params.put("categories", categories);
			params.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return new ApiResponse().ok().setData(params);
    }
	
}
