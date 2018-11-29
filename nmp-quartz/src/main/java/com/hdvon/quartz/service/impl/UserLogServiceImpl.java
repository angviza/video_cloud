package com.hdvon.quartz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.quartz.entity.ReportResponseVo;
import com.hdvon.quartz.entity.UserLog;
import com.hdvon.quartz.entity.RepUseranalysis;
import com.hdvon.quartz.mapper.UserLogMapper;
import com.hdvon.quartz.service.IUserLogService;
import com.hdvon.quartz.vo.UserLogVo;
import com.hdvon.quartz.vo.UserVo;

import cn.hutool.core.convert.Convert;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>用户行为日志表Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class UserLogServiceImpl implements IUserLogService {

	@Autowired
	private UserLogMapper userLogMapper;

	@Override
	public List<ReportResponseVo> getCameraLog(Map<String, Object> param) {
		List<ReportResponseVo> list = userLogMapper.selectHistoryCamara(param);
		return list;
	}

	@Override
	public List<RepUseranalysis> getCamearTotal(Map<String, Object> param) {
		List<RepUseranalysis> camearTotal=userLogMapper.selectUsertCamerTotal(param);
		return camearTotal;
	}

	@Override
	public List<RepUseranalysis> getOnlineTotal(Map<String, Object> param) {
		List<RepUseranalysis> onlineTotal=userLogMapper.selectUserOnlineTime(param);
		return onlineTotal;
	}

	@Override
	public List<RepUseranalysis> getLoiginTotal(Map<String, Object> param) {
		List<RepUseranalysis> loiginTotal=userLogMapper.selectUserLoginTotal(param);
		return loiginTotal;
	}

	@Override
	public List<UserVo> getCloseInviteUser(Map<String, Object> param) {
		List<UserVo> list=userLogMapper.selectColseInviteUser(param);
		return list;
	}

	@Override
	public List<UserLogVo> getCameraLogList() {
		
		Example example = new Example(UserLog.class);
		List<String> types = new ArrayList<String>();
		types.add("1");
		types.add("2");
		types.add("3");
		example.createCriteria().andIn("type", types).andIsNull("isSync");
		
		List<UserLog> logs = userLogMapper.selectByExample(example);
		List<UserLogVo> list = new ArrayList<UserLogVo>();
		
		if (null != logs && logs.size() > 0) {
			
			for (UserLog log : logs) {
				
				if (null != log.getOperationObject() && log.getOperationObject().length() == 20) {
					
					UserLogVo vo = Convert.convert(UserLogVo.class, log);
					list.add(vo);
				}
				
			}
			
		}
		
		return list;
	}

	@Override
	public void updateUserLogList(List<UserLogVo> list) {
		
		for (UserLogVo vo : list) {
			
			UserLog log = Convert.convert(UserLog.class, vo);
			log.setIsSync("1");
			userLogMapper.updateByPrimaryKey(log);
			
		}
		
	}

}
