package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Collect;
import com.hdvon.nmp.entity.CollectMapping;
import com.hdvon.nmp.entity.Device;
import com.hdvon.nmp.mapper.CollectMapper;
import com.hdvon.nmp.mapper.CollectMappingMapper;
import com.hdvon.nmp.mapper.DeviceMapper;
import com.hdvon.nmp.service.ICollectService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CollectMappingVo;
import com.hdvon.nmp.vo.CollectVo;
import com.hdvon.nmp.vo.UserVo;

import tk.mybatis.mapper.entity.Example;


/**
 * <br>
 * <b>功能：</b>收藏表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CollectServiceImpl implements ICollectService {

	@Autowired
	private CollectMapper collectMapper;
	
	@Autowired
	private CollectMappingMapper collectMappingMapper;
	
	@Autowired
	private DeviceMapper deviceMapper;
	
	
	private static final String URL ="./index/videoMonitoring?deviceCode=";
	/**
	 * 获取用户收藏列表
	 * @param account 用户账号
	 * @return
	 */
	@Override
	public List<CollectVo> getList(UserVo user) {
		// TODO Auto-generated method stub
		Example example = new Example(Collect.class);
		example.createCriteria().andEqualTo("createUser",user.getAccount());
		List<Collect> collectList = collectMapper.selectByExample(example);
		if(collectList.isEmpty() || collectList.size() == 0) {
			return Collections.emptyList();
		}
		List<CollectVo> list = new ArrayList<>();
		collectList.stream().forEach(collect->{
			CollectVo vo = new CollectVo();
			BeanUtils.copyProperties(collect, vo);
			
			List<CollectMappingVo> dataList = getCollectCameraList(collect.getId());
			vo.setData(dataList);
    		
			list.add(vo);
		});
		return list;
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
			mappingVo.setDeviceCode(collectMapping.getDeviceSbbm());
			//根据摄像机id获取摄像机列表
			Device device = deviceMapper.selectByPrimaryKey(collectMapping.getDeviceId());
			if(device != null) {
				mappingVo.setDeviceType(device.getSxjlx());
			}
			String targetUrl = URL + collectMapping.getDeviceSbbm();
			mappingVo.setUrl(targetUrl);
			
			resultList.add(mappingVo);
		});
		return resultList;
	}
	/**
	 * 获取用户收藏列表
	 * @param String account
	 * @return
	 */
	@Override
	public PageInfo<CollectVo> getCollectByPage(UserVo user, PageParam pageParam) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		
		List<CollectVo> list = getList(user);
		return new PageInfo<>(list);
		
	}

	/**
	 * 判断收藏夹是否存在
	 * @param name 收藏夹名称
	 * @param account 用户账号
	 */
	public boolean existsCollect(String name,UserVo user) {
		Example example = new Example(Collect.class);
		example.createCriteria().andEqualTo("name", name).andEqualTo("createUser",user.getAccount());
		
		Collect record = collectMapper.selectOneByExample(example);
		if(record != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 新建收藏夹
	 * @param record
	 * @param account 用户账号
	 */
	@Override
	public void saveCollect(CollectVo record,UserVo user) {
		// TODO Auto-generated method stub
		String id = IdGenerator.nextId();
		
		Collect model = new Collect();
		model.setId(id);
		model.setName(record.getName());
		model.setCreateTime(new Date());
		model.setCreateUser(user.getAccount());
		
		collectMapper.insertSelective(model);
	}
	/**
	 * 删除收藏夹
	 * @param record
	 */
	@Override
	public void deleteCollect(CollectVo record) {
		// TODO Auto-generated method stub
		
		delete(record.getId());
	}

	
	private void delete(String collectId) {
		//根据收藏夹id删除该收藏夹中的所有收藏列表
		Example example = new Example(CollectMapping.class);
		example.createCriteria().andEqualTo("collectId",collectId);
		collectMappingMapper.deleteByExample(example);
		
		//删除收藏夹
		collectMapper.deleteByPrimaryKey(collectId);
	}
	/**
	 * 删除收藏夹
	 * @param record
	 */
	public void batchDeleteCollect(String[] ids) {
		if(ids == null || ids.length == 0) {
			return;
		}
		for(int i=0;i<ids.length;i++) {
			delete(ids[i]);
		}
	}
	

}
