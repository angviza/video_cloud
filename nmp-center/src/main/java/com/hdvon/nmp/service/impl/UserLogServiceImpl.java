package com.hdvon.nmp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Cameragrouop;
import com.hdvon.nmp.entity.UserLog;
import com.hdvon.nmp.mapper.CameragrouopMapper;
import com.hdvon.nmp.mapper.UserLogMapper;
import com.hdvon.nmp.service.IUserLogService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.ReportResponseVo;
import com.hdvon.nmp.vo.CameraGroupReportVo;
import com.hdvon.nmp.vo.UserLogVo;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>用户行为日志表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class UserLogServiceImpl implements IUserLogService {

	@Autowired
	private UserLogMapper userLogMapper;
	
	@Autowired
	private CameragrouopMapper cameragrouopMapper;

	@Override
	public PageInfo<UserLogVo> getUserLogPage(Map<String, String> map, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
		List<UserLogVo> list =userLogMapper.selectUserLogPage(map);
		return new PageInfo<>(list);
	}

	@Override
	public List<Map<String,Object>> getUserLogList(Map<String, String> map) {
		List<UserLogVo> list =userLogMapper.selectUserLogPage(map);
		List<Map<String,Object>> resMap=new ArrayList<Map<String,Object>>();
		for(UserLogVo vo :list) {
			Map<String,Object> logMap=new HashMap<String,Object>();
			logMap.put("name", vo.getName());
			logMap.put("account", vo.getAccount());
			logMap.put("menuName", vo.getMenuName());
			logMap.put("typeName", vo.getTypeName());
			if(vo.getOperationTime() !=null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				logMap.put("operation_time", format.format(vo.getOperationTime()));
			}else {
				logMap.put("operation_time", "");
			}
			logMap.put("content", vo.getContent());
			resMap.add(logMap);
		}
		return resMap;
	}

	@Override
	public void saveUserLog(UserLogVo logVO){
		UserLog userLog = new UserLog();
		if (null == logVO.getId()) {
			userLog.setId(IdGenerator.nextId());
			userLog.setAccount(logVO.getAccount());
			userLog.setMenuId(logVO.getMenuId());
			userLog.setContent(logVO.getContent());
			userLog.setName(logVO.getName());
			userLog.setType(logVO.getType());
			userLog.setTokenId(logVO.getTokenId());
			if(StrUtil.isNotBlank(logVO.getTokenId())) {
				userLog.setUpdateTime(new Date());
			}
			userLog.setOperationObject(logVO.getOperationObject());
			userLog.setOperationTime(logVO.getOperationTime());
			userLog.setResponseTime(logVO.getResponseTime());
			userLogMapper.insertSelective(userLog);
		}
	}

	/**
	 * 历史访问热点
	 */
	@Override
	public List<ReportResponseVo> historyCamara(Map<String, Object> param) {
		return userLogMapper.selectHistoryCamara(param);
	}
	
	@Override
	public PageInfo<ReportResponseVo> getHistoryCamaraPage(Map<String, Object> map, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
		List<ReportResponseVo> list = userLogMapper.selectHistoryCamara(map);
		return new PageInfo<>(list);
	}

	@Override
	public List<ReportResponseVo> getCameraLog(Map<String, Object> param) {
		List<ReportResponseVo> list = userLogMapper.selectHistoryCamara(param);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getHistoryHotPoints(Map<String, Object> map) {
		return userLogMapper.selectHistoryHotPoints(map);
	}

	@Override
	public List<Map<String, Object>> getGroupHistoryHotPoints(Map<String, Object> map) {
		return userLogMapper.selectGroupHistoryHotPoints(map);
	}

	@Override
	public PageInfo<CameraGroupReportVo> getGroupHistoryPage(Map<String, Object> map, PageParam pageParam) {
		
		/*
		List<String> groupIdList = (List<String>) map.get("groupIdList");
		
		if (null == groupIdList || groupIdList.size() == 0) {
			
			Cameragrouop cameragrouop = new Cameragrouop();
			List<CameraGroupReportVo> list = userLogMapper.selectGroupHistoryList(map);
			List<CameraGroupReportVo> lists = new ArrayList<CameraGroupReportVo>();
			
			if (null != list && list.size() > 0) {
				
				for (int index = 0; index < list.size(); index++) {
				    cameragrouop.setPid(list.get(index).getId());
					int count = cameragrouopMapper.selectCount(cameragrouop);
					if (count == 0) {
						lists.add(list.get(index));
					}
				}
			}
			
			System.out.println("============= getGroupHistorySubList lists.size() ======== "+lists.size());
			if (lists.size() <= pageParam.getPageSize()) {
				lists = lists.subList(0, lists.size());
			} else {
				lists = lists.subList((pageParam.getPageNo()-1)*pageParam.getPageSize(), pageParam.getPageNo()*pageParam.getPageSize());
			}
			
			return new PageInfo<>(lists);
		}
		*/
		
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
		List<CameraGroupReportVo> list = userLogMapper.selectGroupHistoryList(map);
		return new PageInfo<>(list);
	}
	
	@Override
	public List<CameraGroupReportVo> getGroupHistorySubList(Map<String, Object> map, PageParam pageParam) {
		
		List<CameraGroupReportVo> lists = new ArrayList<CameraGroupReportVo>();
		Cameragrouop cameragrouop = new Cameragrouop();
		List<CameraGroupReportVo> list = userLogMapper.selectGroupHistoryList(map);
		
		if (null != list && list.size() > 0) {
			
			for (int index = 0; index < list.size(); index++) {
			    cameragrouop.setPid(list.get(index).getId());
				int count = cameragrouopMapper.selectCount(cameragrouop);
				if (count == 0) {
					lists.add(list.get(index));
				}
			}
		}
		
		System.out.println("============= getGroupHistorySubList lists.size() ======== "+lists.size());
		
		if (lists.size() <= pageParam.getPageSize()) {
			lists = lists.subList(0, lists.size());
		} else {
			lists = lists.subList((pageParam.getPageNo()-1)*pageParam.getPageSize(), pageParam.getPageNo()*pageParam.getPageSize());
		}
		
		return lists;
	}

	@Override
	public List<Map<String, Object>> getCameraPeriodHotPoints(Map<String, Object> map) {
		return userLogMapper.selectCameraPeriodHotPoints(map);
	}

	@Override
	public List<Map<String, Object>> getCameraGroupPeriodHotPoints(Map<String, Object> map) {
		return userLogMapper.selectCameraGroupPeriodHotPoints(map);
	}

	//@SuppressWarnings("unchecked")
	@Override
	public PageInfo<ReportResponseVo> getRealTimeCameraPage(Map<String, Object> map, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
		List<ReportResponseVo> list = userLogMapper.selectRealTimeCameraList(map);
		
		/*
		List<String> deviceIdList = (List<String>) map.get("deviceIdList");
		ReportResponseVo rec = new ReportResponseVo();
		Device vo = null;
		
		if (null == list || list.size() == 0) {
			
			list = new ArrayList<ReportResponseVo>();
			for (int index = 0;index < deviceIdList.size();index++) {
				vo = deviceMapper.selectByPrimaryKey(deviceIdList.get(index));
				if (null != vo) {
					rec = new ReportResponseVo();
					rec.setId(vo.getId());
					rec.setDeviceCode(vo.getSbbm());
					rec.setName(vo.getSbmc());
					rec.setHotPoints(0);
					list.add(rec);
				}
			}
		} else {
			
			for (int index = 0;index < deviceIdList.size();index++) {
				vo = deviceMapper.selectByPrimaryKey(deviceIdList.get(index));
				if (null != vo) {
					rec = new ReportResponseVo();
					map.put("deviceCode", vo.getSbbm());
					Long count = this.getRealTimeCameraCount(map);
					if (count == 0) {
						rec.setId(vo.getId());
						rec.setDeviceCode(vo.getSbbm());
						rec.setName(vo.getSbmc());
						rec.setHotPoints(0);
						list.add(rec);
					}
				}
			}
		}
		*/
		return new PageInfo<>(list);
	}

	//@SuppressWarnings("unchecked")
	@Override
	public PageInfo<CameraGroupReportVo> getRealTimeCameraGroupPage(Map<String, Object> map, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
		List<CameraGroupReportVo> list = userLogMapper.selectRealTimeCameraGroupList(map);
		
		/*
		List<String> groupIdList = (List<String>) map.get("groupIdList");
		List<String> deviceIdList = (List<String>) map.get("deviceIdList");
		CameraGroupReportVo rec = new CameraGroupReportVo();
		Cameragrouop vo = null;
		Device device = null;
		
		if (null == list || list.size() == 0 || 
			null == deviceIdList || deviceIdList.size() == 0) {
			
			list = new ArrayList<CameraGroupReportVo>();
			for (int index = 0;index < groupIdList.size();index++) {
				vo = cameragrouopMapper.selectByPrimaryKey(groupIdList.get(index));
				if (null != vo) {
					rec = new CameraGroupReportVo();
					rec.setId(vo.getId());
					rec.setName(vo.getName());
					rec.setHotPoints(0);
					list.add(rec);
				}
			}
		} else {
			
			List<String> deviceCodeList = new ArrayList<String>();
			for (int index = 0;index < deviceIdList.size();index++) {
				device = deviceMapper.selectByPrimaryKey(deviceIdList.get(index));
				if (null != device) {
					deviceCodeList.add(device.getSbbm());
				}
			}
			
			map.put("deviceCodeList", deviceCodeList);
			
			for (int index = 0;index < groupIdList.size();index++) {
				vo = cameragrouopMapper.selectByPrimaryKey(groupIdList.get(index));
				if (null != vo) {
					rec = new CameraGroupReportVo();
					
					Long count = this.getRealTimeCameraCount(map);
					if (count == 0) {
						rec.setId(vo.getId());
						rec.setName(vo.getName());
						rec.setHotPoints(0);
						list.add(rec);
					}
				}
			}
		}
		*/
		
		return new PageInfo<>(list);
	}

	@Override
	public Long getRealTimeCameraCount(Map<String, Object> map) {
		return userLogMapper.selectRealTimeCameraCount(map);
	}

	@Override
	public List<ReportResponseVo> getDefaultHistoryCamara(Map<String, Object> param) {
		List<ReportResponseVo> list = userLogMapper.selectDefaultHistoryCamara(param);
		return list;
	}

	@Override
	public List<CameraGroupReportVo> getDefaultGroupHistoryList(Map<String, Object> param) {
		List<CameraGroupReportVo> list = userLogMapper.selectDefaultGroupHistoryList(param);
		return list;
	}

	@Override
	public List<ReportResponseVo> getDefaultRealTimeCamera(Map<String, Object> param) {
		List<ReportResponseVo> list = userLogMapper.selectDefaultRealTimeCamera(param);
		return list;
	}

	@Override
	public List<CameraGroupReportVo> getDefaultRealTimeCameraGroup(Map<String, Object> param) {
		List<CameraGroupReportVo> list = userLogMapper.selectDefaultRealTimeCameraGroup(param);
		return list;
	}

}
