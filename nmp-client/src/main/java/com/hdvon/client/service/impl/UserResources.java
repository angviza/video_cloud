package com.hdvon.client.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hdvon.client.service.EsUpdateService;
import com.hdvon.client.service.IUserResources;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.nmp.enums.MsgTypeEnum;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserResources implements IUserResources {

	@Resource
	private EsUpdateService esUpdateIndexService;
	
	@Override
	public void sendSyncDeviceMappingPermission(CameraMappingMsg mapping) {
		if(mapping ==null ) {
			return ;
		}
		
	    MsgTypeEnum msgEm = MsgTypeEnum.getMsgTypeEnumByKey(mapping.getType());
	    
		if (MsgTypeEnum.ES_USER_MSG.equals(msgEm)) {
			//根据资源角色关联用户更新索引数据
			esUpdateIndexService.batchUpdateIndexByUser(mapping);
			log.info("资源授权更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
			
		} else if (MsgTypeEnum.ES_PLLAN_MSG.equals(msgEm)) {
			
			//根据权限预案对用户授权更新索引库
			esUpdateIndexService.batchUpdateIndexByPlan(mapping);
			log.info("权限预案更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
	        
		} else if (MsgTypeEnum.ES_GROUP_MSG.equals(msgEm)) {
			
			//根据分组更新索引库
			esUpdateIndexService.batchUpdateIndexByGroup(mapping);
			log.info("自定义分组更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
			
		} else if (MsgTypeEnum.ES_USERROLE_MSG.equals(msgEm)) {
			
			//根据用户分配资源角色更新ES用户摄像机资源数据
			esUpdateIndexService.updateByUserRole(mapping);
			log.info("根据用户分配资源角色更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
		} 
	   
   }

	/**
	 * 更新摄像机信息
	 */
	@Override
	public void sendSyncDevicePermission(CameraMsg msg) {
		if(msg ==null ) {
			return ;
		}
	    MsgTypeEnum msgEm = MsgTypeEnum.getMsgTypeEnumByKey(msg.getType());
	    if(MsgTypeEnum.ES_CAMERA_MSG.equals(msgEm)) {
	    	//更新设备的索引库
			//根据设备id集合更新摄像索引库【保护hdvon_camera_permission,hdvon_camera_group】
			esUpdateIndexService.updateIndexBydevId(msg);
			log.info("设备更新的消息  : msg-> {} ", JSON.toJSONString(msg));
	    }

	}
	   

	
	

}
