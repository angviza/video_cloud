package com.hdvon.nmp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.CameraLog;
import com.hdvon.nmp.enums.DevcOperTypeEnums;
import com.hdvon.nmp.enums.MethodEum;
import com.hdvon.nmp.enums.PlayStatusEnums;
import com.hdvon.nmp.mapper.CameraLogMapper;
import com.hdvon.nmp.service.ICameraLogService;
import com.hdvon.nmp.service.ISysmenuService;
import com.hdvon.nmp.service.IUserLogService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraLogVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.SysmenuVo;
import com.hdvon.nmp.vo.UserLogVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.video.UserOperLogVo;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>设备播放记录（临时）表Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraLogServiceImpl implements ICameraLogService {

	@Autowired
	private CameraLogMapper cameraLogMapper;
	
	@Autowired
	private ISysmenuService sysmenuService;
	
	@Autowired
	private IUserLogService userLogService;
	
	@Override
	public void saveLog(CameraLogVo log) {
		
		CameraLog cameraLog = Convert.convert(CameraLog.class, log);
		if(StrUtil.isBlank(cameraLog.getId())) {
			cameraLog.setId(IdGenerator.nextId());
			cameraLog.setState("1");
			cameraLog.setCreateTime(new Date());
			cameraLogMapper.insert(cameraLog);
		}else {
			cameraLog.setUpdateTime(new Date());
			cameraLogMapper.updateByPrimaryKeySelective(cameraLog);
		}
		
	}

	@Override
	public void deleteLog(List<String> callIdList) {
		 Example example = new Example(CameraLog.class);
		 example.createCriteria().andIn("callId",callIdList);
		 cameraLogMapper.deleteByExample(example);
		
	}

	@Override
	public List<CameraLogVo> deleteByParam(Map<String, String> param) {
		List<CameraLogVo> list=cameraLogMapper.seleteByParam(param);
		Example example = new Example(CameraLog.class);
		example.createCriteria().andEqualTo("userId", param.get("userId")).andEqualTo("userIp", param.get("userIp"));
		cameraLogMapper.deleteByExample(example);
		
		return list;
	}

	@Override
	public PageInfo<CameraVo> getCameraLogByPage(Map<String, Object> param, PageParam pageParam) {
		 PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		 List<CameraVo> list=cameraLogMapper.selectgCameraLogByPage(param);
		 return new PageInfo<>(list);
	}

	@Override
	public PageInfo<UserVo> getOnlineUsersByPage(PageParam pageParam, Map<String, Object> param) {
		 PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		 List<UserVo> list=cameraLogMapper.selectOnlineUsersByPage(param);
		 return new PageInfo<>(list);
	}

	
	@Override
	public void saveCameraLog(UserOperLogVo vo, UserVo userVo) {
		
		UserLogVo logVO = new UserLogVo();
        long end = Calendar.getInstance().getTime().getTime();
        
        if (null != vo.getMenuType()) {
        	
        	if (vo.getMenuType().intValue() == 0) {	//视频监控模块
        		vo.setUrl("/videoMonitoring/");
        	}
        	
        	if (vo.getMenuType().intValue() == 1) {	//线索翻查模块
        		vo.setUrl("/videoReplay/");
        	}
        	
        	if (vo.getMenuType().intValue() == 2) {	//案事件管理模块
        		vo.setUrl("/deviceExport/");
        	}
        	
        }
        
		//系统菜单
        String urlLike = vo.getUrl().substring(0, vo.getUrl().lastIndexOf("/")+1);
    	Map<String, Object> param = new HashMap<>();
        List<SysmenuVo> menuList = sysmenuService.getMenuFunctionByParam(param);
        // jdk8 以上版本才能支持
        List<SysmenuVo> sysmenu = menuList.stream().filter(menu-> menu.getUrl().contains(urlLike)).collect(Collectors.toList());
        if(sysmenu.size() > 0) {
        	logVO.setMenuId(sysmenu.get(0).getPid());
        }
        
        logVO.setAccount(userVo.getAccount());
        String content = userVo.getAccount()+"在操作设备->"+vo.getContent();
        logVO.setType(vo.getOperType());//操作类型
        logVO.setContent(content);
        logVO.setName(userVo.getName());
        logVO.setTokenId(vo.getToken());
        logVO.setOperationObject(vo.getDeviceCode());
        logVO.setOperationTime(Calendar.getInstance().getTime());
        logVO.setResponseTime(end - vo.getStart());
        userLogService.saveUserLog(logVO);
        //修改访问热度值
        //cameraService.updateHosts(deviceCode,userVo);
	}

	/**
	@Override
	public void syncCameraSipLog(SipLogVo vo, SipLogBean bean) {
		
		UserVo userVo = userService.getUserById(vo.getUserId());
		
		if (null == userVo) {
			
			return;
		}
		
		if (MethodEum.ATTACH.getValue().equals(vo.getMethod()) || MethodEum.DETACH.getValue().equals(vo.getMethod()) ||
			MethodEum.TERMINATE.getValue().equals(vo.getMethod()) || MethodEum.CANCEL.getValue().equals(vo.getMethod()) ||
			MethodEum.KEEPLIVE.getValue().equals(vo.getMethod()) || MethodEum.QUERYPRESET.getValue().equals(vo.getMethod()) ||
			MethodEum.QUERYRECORD.getValue().equals(vo.getMethod())) {
			
			return;
		}
		
		long start = Calendar.getInstance().getTime().getTime();
		UserOperLogVo log = new UserOperLogVo();
		
		if (MethodEum.PLAY.getValue().equals(vo.getMethod()) || MethodEum.CLOUD.getValue().equals(vo.getMethod()) ||
			MethodEum.CRUISE.getValue().equals(vo.getMethod()) || MethodEum.PRESET.getValue().equals(vo.getMethod()) ||
			MethodEum.WIPER.getValue().equals(vo.getMethod()) || MethodEum.RECORD.getValue().equals(vo.getMethod())) {
			
			log.setMenuType(MenuTypeEnums.视频监控.getValue());
			
			if (MethodEum.PLAY.getValue().equals(vo.getMethod()) || MethodEum.RECORD.getValue().equals(vo.getMethod())) {
				
				log.setOperType(DevcOperTypeEnums.实时播放.getValue());
				log.setPlayType(DevcOperTypeEnums.实时播放.getValue());
				
				if (MethodEum.PLAY.getValue().equals(vo.getMethod())) {
					
					log.setContent("视频实时播放");
				} else {
					
					log.setContent("实时录像控制");
				}
			} else {
				
				log.setOperType(DevcOperTypeEnums.云台控制.getValue());
				
				if (MethodEum.CLOUD.getValue().equals(vo.getMethod())) {
					
					log.setContent("摄像头方向控制");
				}
				
				if (MethodEum.CRUISE.getValue().equals(vo.getMethod())) {
					
					log.setContent("球机预置位巡航预案控制");
				}

				if (MethodEum.PRESET.getValue().equals(vo.getMethod())) {
					
					log.setContent("预置位设置控制");
				}
				
				if (MethodEum.WIPER.getValue().equals(vo.getMethod())) {
					
					log.setContent("雨刷开关控制");
				}
				
			}
			
		}
		
		if (MethodEum.PLAYBACK.getValue().equals(vo.getMethod()) || MethodEum.PLAYBACK_CONTROL.getValue().equals(vo.getMethod()) ||
			MethodEum.DOWNLOAD.getValue().equals(vo.getMethod()) || MethodEum.SINGLEROADMANYHOUR.getValue().equals(vo.getMethod())) {
			
			log.setMenuType(MenuTypeEnums.线索翻查.getValue());
			
			if (MethodEum.PLAYBACK.getValue().equals(vo.getMethod()) || MethodEum.PLAYBACK_CONTROL.getValue().equals(vo.getMethod()) ||
				MethodEum.SINGLEROADMANYHOUR.getValue().equals(vo.getMethod())) {
				
				log.setOperType(DevcOperTypeEnums.录像回放.getValue());
				log.setPlayType(DevcOperTypeEnums.录像回放.getValue());
				
				if (MethodEum.PLAYBACK.getValue().equals(vo.getMethod())) {
					
					log.setContent("视频录像回放");
				}
				
				if (MethodEum.PLAYBACK_CONTROL.getValue().equals(vo.getMethod())) {
					
					log.setContent("录像回放控制");
				}
				
				if (MethodEum.SINGLEROADMANYHOUR.getValue().equals(vo.getMethod())) {
					
					log.setContent("视频录像单路多时回放");
				}
				
			} else {
				
				log.setOperType(DevcOperTypeEnums.录像下载.getValue());
				log.setPlayType(DevcOperTypeEnums.录像下载.getValue());
				log.setContent("录像文件下载");
			}
			
		}
		
		if (DevcOperTypeEnums.实时播放.getValue().equals(log.getOperType()) || DevcOperTypeEnums.录像回放.getValue().equals(log.getOperType()) ||
			DevcOperTypeEnums.录像下载.getValue().equals(log.getOperType()) || DevcOperTypeEnums.云台控制.getValue().equals(log.getOperType())) {
			
			log.setToken(bean.getToken());
		}
		
		log.setUserId(userVo.getId());
		log.setCallId(vo.getCallId());
		log.setLocalIp(vo.getReqIp());
		log.setDeviceCode(vo.getDeviceId());
		log.setStart(start);
		this.saveCameraLog(log, userVo);
		
	}
	**/
	
	@Override
	public void syncCameraSipLog(CameraLogVo vo) {
		
		if (null != vo.getCallId()) {
			
			CameraLog log = Convert.convert(CameraLog.class, vo);
			
			if (MethodEum.PLAY.getValue().equals(vo.getPlayType()) || MethodEum.PLAYBACK.getValue().equals(vo.getPlayType()) ||
				MethodEum.DOWNLOAD.getValue().equals(vo.getPlayType())) {//在线播放
				
				if (MethodEum.PLAY.getValue().equals(vo.getPlayType())) {
					
					log.setPlayType(DevcOperTypeEnums.实时播放.getValue());
				}
				
				if (MethodEum.PLAYBACK.getValue().equals(vo.getPlayType())) {
					
					log.setPlayType(DevcOperTypeEnums.录像回放.getValue());
				}
				
				if (MethodEum.DOWNLOAD.getValue().equals(vo.getPlayType())) {
					
					log.setPlayType(DevcOperTypeEnums.录像下载.getValue());
				}
				
				log.setState(PlayStatusEnums.在线.getValue());
				log.setId(IdGenerator.nextId());
				log.setCreateTime(new Date());
				
				//将播放记录插入到摄像机临时日志表中
				cameraLogMapper.insertSelective(log);
			}
			
			if (MethodEum.TERMINATE.getValue().equals(vo.getPlayType())) {//停止播放
				
				//根据会话ID查询对应的播放记录
				Example example = new Example(CameraLog.class);
				example.createCriteria().andEqualTo("callId", log.getCallId());
				log = cameraLogMapper.selectOneByExample(example);
				
				if (null != log) {
					
					//将对应会话的播放记录从摄像机临时日志表中删除
					cameraLogMapper.deleteByPrimaryKey(log.getId());
				}
			}
		}
		
	}

}
