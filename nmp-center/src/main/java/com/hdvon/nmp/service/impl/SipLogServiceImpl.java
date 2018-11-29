package com.hdvon.nmp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.SipLog;
import com.hdvon.nmp.enums.MethodDetailEnums;
import com.hdvon.nmp.enums.MethodEum;
import com.hdvon.nmp.mapper.SipLogMapper;
import com.hdvon.nmp.service.ISipLogService;
import com.hdvon.nmp.service.IUserService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.SipLogVo;
import cn.hutool.core.convert.Convert;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>sip操作日志服务实现类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class SipLogServiceImpl implements ISipLogService {

	@Autowired
	private SipLogMapper sipLogMapper;
	
	@Autowired
	private IUserService userService;

	@Override
	public void saveSipLog(SipLogVo vo) {
		
		SipLog log = Convert.convert(SipLog.class, vo);
		if (null == log.getId()) {
			
			String id = IdGenerator.nextId();
			log.setId(id);
			log.setUpdateTime(new Date());
			
			String callId = log.getCallId();
			MethodEum em = MethodEum.getObjectByKey(log.getMethod());
			
			/**
			if (MethodEum.TERMINATE.equals(em)) {	//停止播放
				
				//设置设备播放状态为离线
				log.setPlayStatus(PlayStatusEnums.离线.getValue());
			} else {	//非停止播放，暂时不考虑意外关闭客户端和注销等情况
				
				if (MethodEum.PLAY.equals(em) || MethodEum.PLAYBACK.equals(em) ||
					MethodEum.DOWNLOAD.equals(em) || MethodEum.SINGLEROADMANYHOUR.equals(em)) {
					
					if (null == callId) {//不存在会话ID，也需要将状态改为“离线”
						
						//设置设备播放状态为离线
						log.setPlayStatus(PlayStatusEnums.离线.getValue());
					} else {
						
						//设置设备播放状态为在线
						log.setPlayStatus(PlayStatusEnums.在线.getValue());
					}
				}
				
			}
			**/
			
			UserVo userVo = userService.getUserById(log.getUserId());
			String account = userVo==null?"":userVo.getAccount();
			
			//根据请求方法记录对应的操作内容
			MethodDetailEnums detail = MethodDetailEnums.getInfoByKey(log.getMethod());
			
			if (null != detail) {
				
				String content = account+"在操作设备->"+detail.getValue();
				log.setContent(content);
			}
			
			if (MethodEum.TERMINATE.equals(em)) {	//停止播放
				
				if (null == callId) {//不存在会话ID
					
					//此时必须解析请求的param参数并拿出对应的会话ID
					String param = log.getParam();
					if (null != param) {
						
						JSONObject obj = JSON.parseObject(param);
						callId = (String) obj.get("callId");
					}
				}
				
				if (null != callId) {
					
					//根据会话ID查询对应的播放记录
					Example example = new Example(SipLog.class);
					example.createCriteria().andEqualTo("callId", callId).andEqualTo("method", MethodEum.TERMINATE.getValue());
					int count = sipLogMapper.selectCountByExample(example);
					
					//没有重复的停止播放记录
					if (count <= 0) {
						
						sipLogMapper.insertSelective(log);
					}
				}
				
			} else {
				
				sipLogMapper.insertSelective(log);
			}
			
		}
		
	}
	

	@Override
	public List<Map<String, Object>> getSipLogMap(Map<String, String> map) {
		  List<SipLogVo> list = sipLogMapper.selectByParam(map);
		  List<Map<String, Object>> listMap=new ArrayList<Map<String, Object>>();
		  if(list.size() >0) {
			  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  for(SipLogVo vo :list) {
				  Map<String, Object> param =new HashMap<String, Object>();
				  param.put("account", vo.getAccount());
				  param.put("name", vo.getName());
				  param.put("deviceCode", vo.getDeviceCode());
				  param.put("deviceName", vo.getDeviceName());
				  param.put("content", vo.getContent());
				  if(vo.getReqTime() !=null) {
					  param.put("reqTime", format.format(vo.getReqTime()));
				  }else {
					  param.put("reqTime", null); 
				  }
				  listMap.add(param);
			  }
		  }
		return listMap;
	}

	@Override
	public PageInfo<SipLogVo> getSipLogPage(Map<String, String> map, PageParam pageParam) {
		 PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
         List<SipLogVo> list = sipLogMapper.selectByParam(map);
         return new PageInfo<>(list);
	}

}
