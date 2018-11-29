package com.hdvon.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hdvon.client.config.redis.BaseRedisDao;
import com.hdvon.client.mapper.TreeMapper;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.nmp.common.SystemConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 摄像机树节点对应关系
 * @author wanshaojian
 *
 */
@Component
@Slf4j
public class TreeNodeService {
	@Autowired
	TreeMapper treeMapper;
	
	@Resource
	BaseRedisDao<String, Object> redisDao;
	
	public void getAddressTree() {
		//获取所有地址树列表
		List<CameraPermissionVo> dataList = treeMapper.findAddressTree();
		if(dataList==null || dataList.isEmpty()) {
			return;
		}
		//获取顶级目录
		List<CameraPermissionVo> topList = dataList.stream().filter(t->(t.getPid()==null || "0".equals(t.getPid()))).collect(Collectors.toList());
		Map<String,Object> treeMap = new HashMap<>();
		topList.stream().forEach(t->{
			treeMap.put(t.getId(), t.getName());
			
			getChilderNode(dataList, t, treeMap);
		});
		
		log.info(">>>>>>>>>>>>>>>>"+JSON.toJSON(treeMap));
		//将treeMap数据保存在redis中
		String key = SystemConstant.TREENODE_ADDRESSNODE_KEY ;
		long expireTime=SystemConstant.TREENODE_EXPIRE_SECONDS;
		
		redisDao.addMap(key, treeMap);
		redisDao.setExpireTime(key, expireTime);
		
	}
	
	public void getOrgTree() {
		//获取所有地址行政区划
		List<CameraPermissionVo> dataList = treeMapper.findOrganizationTree();
		if(dataList==null || dataList.isEmpty()) {
			return;
		}
		//获取顶级目录
		List<CameraPermissionVo> topList = dataList.stream().filter(t->(t.getPid()==null || "0".equals(t.getPid()))).collect(Collectors.toList());
		Map<String,Object> treeMap = new HashMap<>();
		topList.stream().forEach(t->{
			treeMap.put(t.getId(), t.getName());
			
			getChilderNode(dataList, t, treeMap);
		});
		
		log.info(">>>>>>>>>>>>>>>>"+JSON.toJSON(treeMap));
		//将treeMap数据保存在redis中
		String key = SystemConstant.TREENODE_ORGNODE_KEY ;
		long expireTime=SystemConstant.TREENODE_EXPIRE_SECONDS;
		
		redisDao.addMap(key, treeMap);
		redisDao.setExpireTime(key, expireTime);
		
	}
	
	
	public void getPojectTree() {
		//获取所有项目分组树
		List<CameraPermissionVo> dataList = treeMapper.findProjectTree();
		if(dataList==null || dataList.isEmpty()) {
			return;
		}
		//获取顶级目录
		List<CameraPermissionVo> topList = dataList.stream().filter(t->(t.getPid()==null || "0".equals(t.getPid()))).collect(Collectors.toList());
		Map<String,Object> treeMap = new HashMap<>();
		topList.stream().forEach(t->{
			treeMap.put(t.getId(), t.getName());
			
			getChilderNode(dataList, t, treeMap);
		});
		
		log.info(">>>>>>>>>>>>>>>>"+JSON.toJSON(treeMap));
		//将treeMap数据保存在redis中
		String key = SystemConstant.TREENODE_PROJECTNODE_KEY ;
		long expireTime=SystemConstant.TREENODE_EXPIRE_SECONDS;
		
		redisDao.addMap(key, treeMap);
		redisDao.setExpireTime(key, expireTime);
		
	}
	
	
	public void getGrroupTree() {
		//获取所有自定义分组树
		Map<String,Object> map=new HashMap<>();
		List<CameraPermissionVo> dataList = treeMapper.findCameraGroupTree(map);
		if(dataList==null || dataList.isEmpty()) {
			return;
		}
		//获取顶级目录
		List<CameraPermissionVo> topList = dataList.stream().filter(t->(t.getPid()==null || "0".equals(t.getPid()))).collect(Collectors.toList());
		Map<String,Object> treeMap = new HashMap<>();
		topList.stream().forEach(t->{
			treeMap.put(t.getId(), t.getName());
			
			getChilderNode(dataList, t, treeMap);
		});
		
		log.info(">>>>>>>>>>>>>>>>"+JSON.toJSON(treeMap));
		//将treeMap数据保存在redis中
		String key = SystemConstant.TREENODE_GROUPNODE_KEY ;
		long expireTime=SystemConstant.TREENODE_EXPIRE_SECONDS;
		
		redisDao.addMap(key, treeMap);
		redisDao.setExpireTime(key, expireTime);
		
	}
	

	/**
	 * 根据当前节点递归获取下级节点
	 * @param dataList 数据集合
	 * @param node 当前节点
	 * @param treeMap 保存集合
	 */
	public void getChilderNode(List<CameraPermissionVo> dataList,CameraPermissionVo node,Map<String,Object> treeMap) {
		//获取下级节点
		List<CameraPermissionVo> childerList = dataList.stream().filter(t->node.getId().equals(t.getPid())).collect(Collectors.toList());
		if(childerList==null || childerList.isEmpty()) {
			return;
		}
		childerList.stream().forEach(t->{
			treeMap.put(t.getId(), treeMap.get(t.getPid())+";"+t.getName());
			
			getChilderNode(dataList, t, treeMap);
		});
	}
}
