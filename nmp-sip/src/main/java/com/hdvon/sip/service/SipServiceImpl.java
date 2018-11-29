package com.hdvon.sip.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hdvon.sip.app.CloudControlClient;
import com.hdvon.sip.app.CruiseClient;
import com.hdvon.sip.app.DeviceStatusClient;
import com.hdvon.sip.app.HearbeatClient;
import com.hdvon.sip.app.MediaClient;
import com.hdvon.sip.app.MediaDownloadClient;
import com.hdvon.sip.app.MediaPlaybackClient;
import com.hdvon.sip.app.MediaSearchClient;
import com.hdvon.sip.app.PresetClient;
import com.hdvon.sip.app.RegisterClient;
import com.hdvon.sip.app.VideotapeClient;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.config.redis.BaseRedisDao;
import com.hdvon.sip.entity.HearbeatBean;
import com.hdvon.sip.entity.RegisterBean;
import com.hdvon.sip.enums.MediaOperationType;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.snowflake.IdGenerator;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.CloudControlQuery;
import com.hdvon.sip.vo.CruiseQuery;
import com.hdvon.sip.vo.CruiseVo;
import com.hdvon.sip.vo.DeviceStatusVo;
import com.hdvon.sip.vo.MediaDirectoryVo;
import com.hdvon.sip.vo.MediaDownloadQuery;
import com.hdvon.sip.vo.MediaDownloadVo;
import com.hdvon.sip.vo.MediaItemVo;
import com.hdvon.sip.vo.MediaPlayQuery;
import com.hdvon.sip.vo.MediaPlayVo;
import com.hdvon.sip.vo.MediaQuery;
import com.hdvon.sip.vo.PresetQuery;
import com.hdvon.sip.vo.PresetVo;
import com.hdvon.sip.vo.VideoControlType.ScaleEnum;
import com.hdvon.sip.vo.VideoControlType.VideoMethodEnum;
import com.hdvon.sip.vo.VideotapeQuery;
import com.hdvon.sip.vo.VideotapeVo;

/**
 * sip服务实现
 * @author waoshaojian
 *
 */
@Service
public class SipServiceImpl implements SipService{
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SipServiceImpl.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	
	
	
	@Resource
	BaseRedisDao<String, Object> redisDao;
	
	@Resource
	SipConfig sipConfig;
	
	
	/**
	 * 用户注册
	 */
	public void register() throws SipSystemException {
		String[] userArr = sipConfig.getUserlist().split(",");
		List<String> userList = new ArrayList<>(Arrays.asList(userArr));
		for(String registerCode:userList) {			
			registerProcess(registerCode);
		}
		
	}
	/**
	 * 用户注册处理
	 * @param client client对象
	 * @param registerCode 注册账号
	 */
	private void registerProcess(String registerCode) {
		try(RegisterClient client = RegisterClient.getInstance(sipConfig)) {
			//获取账号每小时连续发送心跳失败次数
			int failCount = 0;
			String redisKey = genRedisKey(registerCode);
			if(redisDao.exists(redisKey)) {
				failCount = (int) redisDao.get(redisKey);
			}
			/**
			 * 连续发送心跳失败次数超过最高限制，则直接返回
			 */
			if(failCount > SipConstants.HEARBEAT_FAIL_SUM) {
				LOGGER.info("账号{}在时间{}连续发送心跳失败次数{}大于限制次数{},停止账号注册,请检测账号在sip信令服务器是否存在",registerCode,sdf.format(new Date()),failCount,SipConstants.HEARBEAT_FAIL_SUM);
				return;
			}
			
			RegisterBean registerBean = new RegisterBean(registerCode, sipConfig.getExpire());
			client.registerProcess(registerBean);
			
			String regKey = genRegisterKey();
			
			HashMap<String, Object> userMap = new HashMap<>();
			if(redisDao.exists(regKey)) {
				userMap = (HashMap<String, Object>) redisDao.getMap(regKey);
			}
			//注册用户的有效时间
			int userExpire = sipConfig.getExpire();
			if(SipConstants.SUCCESS == registerBean.getStatus()) {
				if(userMap.size() == 0) {
					userMap.put(registerCode, registerCode);
					redisDao.addMap(regKey, userMap);
				}else {
					redisDao.addMap(regKey, registerCode, registerCode, userExpire);
				}
			}else {
				//判断当前用户存在与redis且账号注册失败，则删除当前用户
				if(userMap.containsKey(regKey) ) {
					userMap.remove(regKey);
					redisDao.addMap(regKey, userMap);
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOGGER.error("用户{}注册失败，错误信息：{}",registerCode,e.getMessage());
		}

		
	}
	/**
	 * 用户在信令服务器注销
	 * @param userName 注销用户
	 * @exception SipSystemException
	 */
	@Override
	public void logout(String userName) throws SipSystemException {
		// TODO Auto-generated method stub
		try(RegisterClient client = RegisterClient.getInstance(sipConfig)){
			RegisterBean registerBean = new RegisterBean(userName, 0);
			client.registerProcess(registerBean);
			
			String regKey = genRegisterKey();
			//判断当前用户存在与redis且账号注销成功，则删除当前用户
			if(SipConstants.SUCCESS == registerBean.getStatus()) {
				HashMap<String, Object> userMap = (HashMap<String, Object>) redisDao.getMap(regKey);
				if(userMap.containsKey(regKey)) {
					userMap.remove(regKey);
					redisDao.addMap(regKey, userMap);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}

	}

	/**
	 * 注册用户向信令服务器发送心跳
	 * @throws SipSystemException
	 */
	@Override
	public void checkHeartbeat() throws SipSystemException{
		String[] userArr = sipConfig.getUserlist().split(",");
		List<String> userList = new ArrayList<>(Arrays.asList(userArr));
		
		for(String checkUserCode:userList) {
			//获取账号心跳发送状态，发送失败则重新注册
			int count = 0;
			HearbeatBean bean = new HearbeatBean(checkUserCode);
			
			try(HearbeatClient hearbeatClient = HearbeatClient.getInstance(sipConfig)){
				hearbeatClient.sendHeartbeat(bean);
			}catch (Exception e) {
				// TODO: handle exception
				throw new SipSystemException(e);
			}
			if(bean == null) {
				continue;
			}
			boolean status = (bean.getStatus() == SipConstants.SUCCESS ? true : false);
			if(!status) {
				count += 1;
			}
			//心跳发送失败，则重新注册改用户
			if(SipConstants.FAIL == bean.getStatus()) {
				registerProcess(checkUserCode);	
			}
			//获取账号每小时连续发送心跳失败次数,保存在redis中
			String redisKey = genRedisKey(checkUserCode);
			int failSum = 0;
			if(redisDao.exists(redisKey)) {
				failSum = (int) redisDao.get(redisKey);
				//心跳发送成功，则重新开启计数
				redisDao.set(redisKey, status ? 0 : failSum);
			}else {
				failSum += count;
				redisDao.set(redisKey, failSum, sipConfig.getExpire().longValue());
			}
			if(LOGGER.isDebugEnabled()) {
				LOGGER.info("账号{}在时间{}连续发送心跳失败次数：{}",checkUserCode,sdf.format(new Date()),failSum);
			}
		}
		
		
		
	}
	
	/**
	 * 视频点播
	 * @param model
	 * @throws SipSystemException
	 */
	@Override
	public MediaPlayVo mediaPlay(MediaPlayQuery model) throws SipSystemException {
		// TODO Auto-generated method stub	
		String registerCode = getRegisterCode();

		try {
			MediaClient client = MediaClient.getInstance(sipConfig);
			
			MediaPlayVo bean = client.videoPlay(model,registerCode);

			if(bean.getStatus() == null) {
				throw new SipSystemException(">视频"+JSON.toJSONString(model)+"点播失败！");
			}
			
			//存在在redis缓存中
			String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.PLAY, bean.getCallId());
			redisDao.set(redisKey, bean, 24*60*60*1000L);
			
			return bean;
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}

	}

	/**
	 * 视频下载
	 * @param model
	 * @throws SipSystemException
	 */
	@Override
	public MediaDownloadVo mediaDownload(MediaDownloadQuery model) throws SipSystemException {
		// TODO Auto-generated method stub		
		String registerCode = getRegisterCode();
		MediaDownloadClient client = MediaDownloadClient.getInstance(sipConfig);
		MediaDownloadVo bean = client.videoDownlaod(model,registerCode);
		if(bean.getStatus() == null) {
			throw new SipSystemException(">视频"+JSON.toJSONString(model)+"下载失败！");
		}
		//保存缓存
		String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.DOWNLAOD, bean.getCallId());
		redisDao.set(redisKey, bean, 24*60*60*1000L);
		
		return bean;
	}

	/**
	 * 停止实时播放
	 * @param callId 会话id
	 * @param em 请求媒体流的操作类型
	 * @throws SipSystemException
	 */
	@Override
	public boolean stopMedia(String callId,MediaOperationType em) throws SipSystemException {
		// TODO Auto-generated method stub
		if(MediaOperationType.PLAYBACK.equals(em)) {
			return stopMediaPlayback(callId);
		}else if(MediaOperationType.DOWNLAOD.equals(em)){
			return stopMediaDownload(callId);
		}else {
			return stopMediaPlay(callId);
		}
		
	}
	
	/**
	 * 停止播放的媒体
	 * @param callId
	 * @return
	 */
	private boolean stopMediaPlay(String callId) {
		boolean status = false;
		try {
			//获取对应的客户端
			MediaClient client = MediaClient.getInstance(sipConfig);
			
			//存在在redis缓存中
			String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.PLAY, callId);
			//判断该视频资源在缓存中不存在
			if(!redisDao.exists(redisKey)) {
				LOGGER.info("视频点播callId:{}资源在缓存中不存在",callId);
				return true;
			}
			//根据会话id获取缓存对象
			MediaPlayVo model= (MediaPlayVo) redisDao.get(redisKey);
			//发送bye命令
			client.sendBye(model);
			
			//清除缓存
			redisDao.remove(redisKey);
			status = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOGGER.error(">>>停止视频点播callId:{}失败：{}",callId,e.getMessage());
		}
		return status;
	}
	/**
	 * 停止播放的媒体
	 * @param callId
	 * @return
	 */
	private boolean stopMediaDownload(String callId) {
		boolean status = false;
		try {
			//获取对应的客户端
			MediaDownloadClient client = MediaDownloadClient.getInstance(sipConfig);
			
			//存在在redis缓存中
			String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.DOWNLAOD, callId);
			//判断该视频资源在缓存中不存在
			if(!redisDao.exists(redisKey)) {
				
				LOGGER.info("视频点播callId:{}资源在缓存中不存在",callId);
				return true;
			}
			//根据会话id获取缓存对象
			MediaDownloadVo model= (MediaDownloadVo) redisDao.get(redisKey);
			//发送bye命令
			client.sendBye(model);
			
			//清除缓存
			redisDao.remove(redisKey);
			status = true;

		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(">>>停止视频点播callId:{}失败：{}",callId,e.getMessage());
		}
		return status;
	}
	/**
	 * 停止回看播放的媒体
	 * @param callId
	 * @return
	 */
	private boolean stopMediaPlayback(String pkId) {
		boolean status = false;
		try {
			//存在在redis缓存中
			String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.PLAYBACK, pkId);
			//判断该视频资源在缓存中不存在
			if(!redisDao.exists(redisKey)) {
				LOGGER.info("视频回看pkId:{}资源在缓存中不存在",pkId);
				return true;
			}
			
			//根据会话id获取缓存对象
			MediaDirectoryVo model = (MediaDirectoryVo) redisDao.get(redisKey);
			
			MediaPlaybackClient client = MediaPlaybackClient.getInstance(sipConfig);
			client.sendBye(model);
			
			//清除缓存
			redisDao.remove(redisKey);
			
			status = true;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(">>>pkId:{}失败：{}",pkId,e.getMessage());
		}
		return status;
	}
	/**
	 * 录像翻查
	 * @param model
	 */
	@Override
	public MediaDirectoryVo searchMedia(MediaQuery model) throws SipSystemException {
		// TODO Auto-generated method stub
		String registerCode = getRegisterCode();
		try (MediaSearchClient client = MediaSearchClient.getInstance(sipConfig)) {
			return client.queryVideo(model, registerCode);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}
	}

	
	/**
	 * 录像翻查
	 * @param model
	 */
	@Override
	public MediaDirectoryVo searchMediaAndPlay(MediaQuery model) throws SipSystemException {
		// TODO Auto-generated method stub
		//获取录像结构
		MediaDirectoryVo record = searchMedia(model);
		if(record.getDataList() == null || record.getDataList().isEmpty()) {
			return record;
		}
		List<MediaItemVo> dataList = record.getDataList();
		//存在录像，则自动播放第一段视频
		MediaItemVo mediaItem = dataList.get(0);
		
		//录像回看
		record = mediaPlayback(record);
		
		/**
		 * 计算起始时间是否大于第一段视频开始时间
		 * 	则从查询位置开始播放
		 */
		if(model.getStartTime().getTime()>mediaItem.getStartDate().getTime()) {
			long randomTime = model.getStartTime().getTime() - mediaItem.getStartDate().getTime();
			playbackControlProcess(record, VideoMethodEnum.RANDOM_PLAY, randomTime);
		}

		return record;

	}
	/**
	 * 录像回看
	 * @param model
	 */
	@Override
	public MediaDirectoryVo mediaPlayback(MediaDirectoryVo model) throws SipSystemException {
		if(model.getDataList() == null || model.getDataList().isEmpty()) {
			throw new SipSystemException("对象【"+JSON.toJSONString(model)+"】不存在录像") ;
		}
		List<MediaItemVo> dataList = model.getDataList();
		//存在录像，则自动播放第一段视频
		MediaItemVo mediaItem = dataList.get(0);
		
		String registerCode = getRegisterCode();
		MediaPlaybackClient playbackClient = MediaPlaybackClient.getInstance(sipConfig);
		MediaItemVo mediaRecord = playbackClient.videoPlayback(mediaItem, registerCode);
		if(mediaRecord.getStatus() == null) {
			throw new SipSystemException("录像["+JSON.toJSONString(model)+"]回看失败！") ;
		}
		
		model.setCurrItem(mediaRecord);
		if(dataList.size()>1) {
			model.setNextItem(dataList.get(1));
		}
		//将录像翻查结构保存到redis中
		String pkId = IdGenerator.nextId();
		model.setPkId(pkId);
		
		String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.PLAYBACK, pkId);
		redisDao.set(redisKey, model, 24*60*60*1000L);
		
		return model;
	}
	
	/**
	 * 回看控制 包括：暂停和播放和随机下载
	 * @param pkId 翻查主键id
	 * @param methodEm 控制类型
	 * @param randomTime 随机播放时长 (VideoMethodEnum为RANDOM_PLAY 必填)
	 * @throws SipSystemException
	 */
	@Override
	public MediaDirectoryVo playbackControl(String pkId,VideoMethodEnum methodEm,Long randomTime) throws SipSystemException{
		// TODO Auto-generated method stub
		//根据查询条件获取当前录像翻查对象
		String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.PLAYBACK, pkId);
		//判断该视频资源在缓存中不存在
		if(!redisDao.exists(redisKey)) {
			LOGGER.info("时间：{} pkId:{}回看控制失败",CommonUtil.formatDate(new Date()),pkId);
			return null;
		}
		MediaDirectoryVo model = (MediaDirectoryVo) redisDao.get(redisKey);
		//根据clientId获取当前客户端对象
		MediaItemVo currItemVo = model.getCurrItem();
		String callID = currItemVo.getCallId();
		MediaPlaybackClient client = MediaPlaybackClient.getInstance(sipConfig);
		if(client == null ) {
			LOGGER.info("时间：{} callId:{}回看控制失败，client is null",CommonUtil.formatDate(new Date()),callID);
			return null;
		}
		if(methodEm.equals(VideoMethodEnum.RANDOM_PLAY)) {
			client.playbackRandomBroadcast(model, randomTime);
		}else {
			client.playbackPauseOrBroadcast(model, methodEm);
		}
		return model;
	}
	/**
	 * 回看控制 包括：暂停和播放和随机下载
	 * @param model 对象
	 * @param methodEm 控制类型
	 * @param randomTime 随机播放时长 (VideoMethodEnum为RANDOM_PLAY 必填)
	 * @throws SipSystemException
	 */
	private MediaDirectoryVo playbackControlProcess(MediaDirectoryVo model,VideoMethodEnum methodEm,Long randomTime) throws SipSystemException{
		// TODO Auto-generated method stub
		//根据clientId获取当前客户端对象
		MediaItemVo currItemVo = model.getCurrItem();
		String callID = currItemVo.getCallId();
		MediaPlaybackClient client = MediaPlaybackClient.getInstance(sipConfig);
		if(client == null ) {
			LOGGER.info("时间：{} callId:{}回看控制失败，client is null",CommonUtil.formatDate(new Date()),callID);
			return null;
		}
		if(methodEm.equals(VideoMethodEnum.RANDOM_PLAY)) {
			client.playbackRandomBroadcast(model, randomTime);
		}else {
			client.playbackPauseOrBroadcast(model, methodEm);
		}
		return model;
	}
	/**
	 * 回看控制 快进处理
	 * @param pkId 翻查主键id
	 * @param em 快进倍数枚举
	 * @throws SipSystemException
	 */
	@Override
	public MediaDirectoryVo playbackFastForward(String pkId,ScaleEnum em) throws SipSystemException{
		// TODO Auto-generated method stub

		//根据查询条件获取当前录像翻查对象
		String redisKey = SipConstants.getMediaRedisKey(MediaOperationType.PLAYBACK, pkId);
		//判断该视频资源在缓存中不存在
		if(!redisDao.exists(redisKey)) {
			LOGGER.info("时间：{} pkId:{}回看控制失败",CommonUtil.formatDate(new Date()),pkId);
			return null;
		}
		MediaDirectoryVo model = (MediaDirectoryVo) redisDao.get(redisKey);
		//根据clientId获取当前客户端对象
		MediaItemVo currItemVo = model.getCurrItem();
		String callID = currItemVo.getCallId();
		MediaPlaybackClient client = MediaPlaybackClient.getInstance(sipConfig);
		if(client == null ) {
			LOGGER.info("时间：{} callId:{}回看控制失败，client is null",CommonUtil.formatDate(new Date()),callID);
			return null;
		}
		
		client.playbackFastForward(model, em);
		
		return model;
	}
	
	/**
	 * 视频点播 球机 云台控制
	 * @param model 请求对象
	 * @throws SipSystemException
	 */
	@Override
	public void cloudControl(CloudControlQuery model) throws SipSystemException{
		String registerCode = getRegisterCode();
		try (CloudControlClient client = CloudControlClient.getInstance(sipConfig)) {
			client.cloudControl(model, registerCode);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}
	}
	

	/**
	 * 预置位设置
	 * @param model
	 * @return
	 * @throws SipSystemException
	 */
	@Override
	public PresetVo presetCmd(PresetQuery model) throws SipSystemException {
		// TODO Auto-generated method stub
		String registerCode = getRegisterCode();
		try (PresetClient client = PresetClient.getInstance(sipConfig)) {
			return client.preset(model, registerCode);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}
		
	}
	/**
	 * 录像控制 【包括开始录像  结束录像】
	 * @param model
	 * @return
	 * @throws SipSystemException
	 */
	@Override
	public VideotapeVo mediaTapeControl(VideotapeQuery model) throws SipSystemException {
		// TODO Auto-generated method stub
		String registerCode = getRegisterCode();
		try (VideotapeClient client = VideotapeClient.getInstance(sipConfig)) {
			return  client.videotapeProcess(model, registerCode);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}
		
	}
	
	/**
	 * 巡航控制
	 * @param model
	 * @return
	 * @throws SipSystemException
	 */
	@Override
	public CruiseVo CruiseControl(CruiseQuery model)throws SipSystemException{
		String registerCode = getRegisterCode();
		try (CruiseClient client = CruiseClient.getInstance(sipConfig)) {
			return client.cruiseProcess(model, registerCode);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}
		
	}
	/**
	 * 设备状态查询
	 * @param deviceCode 设备编码
	 * @return
	 * @throws SipSystemException
	 */
	@Override
	public DeviceStatusVo searchDeviceStatus(String deviceCode) throws SipSystemException {
		// TODO Auto-generated method stub
		String registerCode = getRegisterCode();
		try (DeviceStatusClient client = DeviceStatusClient.getInstance(sipConfig)) {
			return client.searchDeviceStatus(deviceCode, registerCode);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}
	}
	/**
	 * 获取注册用户
	 * @return
	 */
	private String getRegisterCode() {
		//从缓存中获取注册用户，随机选择一个
		String key = genRegisterKey();
		Map<String, Object> userMap = redisDao.getMap(key);
		Set<String> keySet = userMap.keySet();
		if(keySet.size() == 0) {
			return sipConfig.getDefaultUserName();
		}
		
		String[] arr = new String[keySet.size()];  
		//Set-->数组  
		keySet.toArray(arr);
		
		if(keySet.size() == 1) {
			return arr[0];
		}
		
		Random rd = new Random();
		int next = rd.nextInt(keySet.size()-1);
		String registerCode = arr[next];
		LOGGER.info("随机返回的用户{}",registerCode);
		return registerCode;
	}
	/**
	 * 生成redis数据key
	 * @return
	 */
	private String genRegisterKey() {
		return SipConstants.REDIS_USER_KYE;
	}
	/**
	 * 生成redis数据key
	 * @return
	 */
	private String genRedisKey(String registerUser) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(SipConstants.REDIS_HEARBEAT_PREFIX);
		strBuf.append(registerUser).append(SipConstants.SPLIT_CHAR).append(sdf.format(new Date()));
		return strBuf.toString();
	}


	
	


	

}
