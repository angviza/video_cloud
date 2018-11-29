package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Collect;
import com.hdvon.nmp.entity.CollectMapping;
import com.hdvon.nmp.entity.Device;
import com.hdvon.nmp.mapper.CollectMapper;
import com.hdvon.nmp.mapper.CollectMappingMapper;
import com.hdvon.nmp.mapper.DeviceMapper;
import com.hdvon.nmp.service.ICollectMappingService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CollectMappingVo;

import tk.mybatis.mapper.entity.Example;



/**
 * <br>
 * <b>功能：</b>收藏夹关联表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CollectMappingServiceImpl implements ICollectMappingService {
	@Autowired
	private CollectMapper collectMapper;
	
	@Autowired
	private CollectMappingMapper collectMappingMapper;

	@Autowired
	private DeviceMapper deviceMapper;
	/**
	 * 获取用户收藏摄像机列表
	 * @param account 用户账号
	 * @param collectId 收藏夹id
	 * @param 分页对象
	 * @return
	 */
	public PageInfo<CollectMappingVo> getListByPage(String account,String collectId, PageParam pageParam){
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		
		Map<String, String> params = new HashMap<>();
		params.put("account", account);
		params.put("collectId", collectId);
		List<CollectMappingVo> list = collectMappingMapper.getListBySub(params);
		if(list.isEmpty() || list.size() == 0) {
			return new PageInfo<>(Collections.emptyList());
		}
		
		return new PageInfo<>(list);
	}
	/**
	 * 获取收藏涉嫌机列表
	 * @param account 用户账号
	 * @param collectId 收藏夹id
	 */
	public List<CollectMappingVo> getCollectCameraList(String collectId){
		Example example = new Example(CollectMapping.class);
		example.and().andEqualTo("collectId", collectId);
		
		List<CollectMapping> list = collectMappingMapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<CollectMappingVo> resultList = new ArrayList<>();
		list.stream().forEach(collectMapping->{
			CollectMappingVo mappingVo = new CollectMappingVo();
			BeanUtils.copyProperties(collectMapping, mappingVo);
			
			resultList.add(mappingVo);
		});
		return resultList;
	}
	
	/**
	 * 收藏摄像机
	 * @param record
	 */
	public void saveMapping(CollectMappingVo record){
		CollectMapping model = new CollectMapping();
		BeanUtils.copyProperties(record, model);
		
		model.setId(IdGenerator.nextId());
		
		collectMappingMapper.insertSelective(model);
	}
	
	/**
	 * 收藏摄像机
	 * @param collectId 收藏夹id
	 * @param deviceIds 摄像机id数组
	 * @return 添加成功 返回 true 失败返回false
	 */
	public boolean saveMapping(String collectId,String [] deviceIds) {
		if(StringUtils.isEmpty(deviceIds) || (deviceIds == null || deviceIds.length == 0)) {
			return false;
		}
		boolean flag = false;
		//判断文件夹是否存在
		Collect collect = collectMapper.selectByPrimaryKey(collectId);
		if(collect == null) {
			return false;
		}
		
		List<CollectMapping> list = new ArrayList<>();
		for(int i=0;i<deviceIds.length;i++) {
			//根据设备id获取设备信息
			String deviceId = deviceIds[i];
			Device device = deviceMapper.selectByPrimaryKey(deviceId);
			if(device == null) {
				flag = true;
				break;
			}
			CollectMapping record = new CollectMapping();
			record.setCollectId(collectId);
			record.setDeviceId(deviceId);
			record.setDeviceName(device.getSbmc());
			record.setDeviceSbbm(device.getSbbm());
			
			record.setId(IdGenerator.nextId());
			list.add(record);
			
		}
		if(flag) {
			return false;
		}
		list.stream().forEach(collectMapping->{
			collectMappingMapper.insertSelective(collectMapping);
		});
		return true;
		
	}
	/**
	 * 删除收藏摄像机
	 * @param deviceIds 摄像机id数组
	 */
	public void deleteMapping(String [] deviceIds){
		if(deviceIds == null || deviceIds.length == 0) {
			return;
		}
		for(int i=0;i<deviceIds.length;i++) {
			String deviceId = deviceIds[i];
			Example example = new Example(CollectMapping.class);
			example.createCriteria().andEqualTo("deviceId",deviceId);
		}
	}
}
