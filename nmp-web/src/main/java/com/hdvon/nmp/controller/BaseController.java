package com.hdvon.nmp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.biz.UserBiz;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.util.ClientUtil;
import com.hdvon.nmp.util.RedisHelper;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.PlugntNeedInfoVo;
import com.hdvon.nmp.vo.sip.UserDeviceParamVo;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公共Controller
 */
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    @SuppressWarnings("rawtypes")
	@Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    public RedisHelper redisHelper;
      
	@Reference
	private ICameraService cameraService;

    /**
     * token
     */
    protected String token;
    
    @Autowired
    private UserBiz userBiz;

    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.token = request.getHeader(WebConstant.TOKEN_HEADER);
    }
    
    
    /**
	 * 用户是否拥有操作权限
	 * @param deviceCode 设备编码
	 * @param value  操作权限值
	 * @return
	 */
    public void getUserPermission(String deviceCode,String value){
		UserVo userVo = getLoginUser();
		Map<String ,Object> param =new HashMap<String ,Object>();
		if(!userVo.isAdmin()){//管理员返回所有
			param.put("userId", userVo.getId());
			param.put("deviceCode", deviceCode);
			List<UserDeviceParamVo> list= cameraService.getUserCameraPermission(param);
			if(list.size()>0) {
				if(StrUtil.isNotBlank(list.get(0).getPermissionVlaue())) {
					if( ! list.get(0).getPermissionVlaue().contains(value)) {
						throw new ServiceException("您没有操作该摄像机权限!");
					}
				}
			}else {
				throw new ServiceException("您没有操作该摄像机权限!");
			}
	     }
	}

    
    public UserVo getLoginUser() {
    	return this.userBiz.getLoginUser(this.token);
    }

    
    public Map<String, Object> setLock(UserVo vo, String lockKey, long time) {
    	
		long lockTime = time*60*1000;//锁时分钟
		
		Date afterDate = new Date(new Date().getTime() + lockTime);//锁时结束时间
		Map<String, Object> userLevelMap = new HashMap<String, Object>();
		userLevelMap.put("mapUserId", vo.getId());
		
		/************************************************ Added by huanhongliang 2018/9/25 ******************************************/
		userLevelMap.put("mapUserName", vo.getAccount());
		userLevelMap.put("mapUserDept", vo.getDepartmentName()==null?"":vo.getDepartmentName());
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		userLevelMap.put("mapUserLevel", vo.getLevel()==null?0:vo.getLevel());
		userLevelMap.put("mapLockTime", afterDate.getTime());
		
		redisTemplate.opsForValue().set(lockKey, userLevelMap);
		redisTemplate.expire(lockKey, time*60, TimeUnit.SECONDS);
		
		//this.saveUserLog(deviceCode, "摄像头锁时控制", "/videoMonitoring/cameraLock", start, "control", token);
		return userLevelMap;
	}
    
    
    public List<PlugntNeedInfoVo> creatPlugntNeedInfo(String clientIp, int number) {
		List<PlugntNeedInfoVo> listvo= new ArrayList<PlugntNeedInfoVo>();
		
		List<String> list = ClientUtil.getClientIpAndProt(clientIp, number);
		for(String prot :list) {
			PlugntNeedInfoVo vo = new PlugntNeedInfoVo ();
			vo.setClientIp(clientIp);
			vo.setClientProt(prot);
			listvo.add(vo);
		}
		return listvo;
	}
    
}
