package com.hdvon.sip.service;

import com.hdvon.sip.enums.MediaOperationType;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.vo.CloudControlQuery;
import com.hdvon.sip.vo.CruiseQuery;
import com.hdvon.sip.vo.CruiseVo;
import com.hdvon.sip.vo.DeviceStatusVo;
import com.hdvon.sip.vo.MediaDirectoryVo;
import com.hdvon.sip.vo.MediaDownloadQuery;
import com.hdvon.sip.vo.MediaDownloadVo;
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
 * sip服务接口
 * @author waoshaojian
 *
 */
public interface SipService {
	/**
	 * 用户注册到信令服务器
	 * @throws SipSystemException
	 */
	void register() throws SipSystemException;
	
	/**
	 * 用户在信令服务器注销
	 * @throws SipSystemException
	 */
	void logout(String userName) throws SipSystemException;
	

	/**
	 * 注册用户向信令服务器发送心跳
	 * @throws SipSystemException
	 */
	void checkHeartbeat() throws SipSystemException;
	
	/**
	 * 视频点播
	 * @param model
	 * @throws SipSystemException
	 */
	MediaPlayVo mediaPlay(MediaPlayQuery model) throws SipSystemException;
	
	/**
	 * 视频点播 球机 云台控制
	 * @param model 请求对象
	 * @throws SipSystemException
	 */
	void cloudControl(CloudControlQuery model) throws SipSystemException;

	/**
	 * 停止播放
	 * @param callId 会话id
	 * @param em 请求媒体流的操作类型
	 * @throws SipSystemException
	 */
	boolean stopMedia(String callId,MediaOperationType em) throws SipSystemException;
	
	/**
	 * 录像查询
	 * @param model
	 * @throws SipSystemException
	 */
	MediaDirectoryVo searchMedia(MediaQuery model)throws SipSystemException;
	
	/**
	 * 录像回看
	 * @param model
	 * @throws SipSystemException
	 */
	MediaDirectoryVo mediaPlayback(MediaDirectoryVo model)throws SipSystemException;
	
	
	/**
	 * 录像查询并播放
	 * 		步骤 1：查询录像列表
	 *         2：自动播放第一段录像
	 * @param model
	 * @throws SipSystemException
	 */
	MediaDirectoryVo searchMediaAndPlay(MediaQuery model)throws SipSystemException;


	
	/**
	 * 回看控制 包括：暂停和播放 和随机播放
	 * @param pkId 翻查主键id
	 * @param methodEm 控制类型
	 * @param randomTime 随机播放时长 (VideoMethodEnum为RANDOM_PLAY 必填)
	 * @throws SipSystemException
	 */
	MediaDirectoryVo playbackControl(String pkId,VideoMethodEnum methodEm,Long randomTime) throws SipSystemException;
	
	/**
	 * 回看控制 快进处理
	 * @param pkId 翻查主键id
	 * @param em 快进倍数枚举
	 * @throws SipSystemException
	 */
	MediaDirectoryVo playbackFastForward(String pkId,ScaleEnum em) throws SipSystemException;

	/**
	 * 视频下载
	 * @param model
	 * @throws SipSystemException
	 */
	MediaDownloadVo mediaDownload(MediaDownloadQuery model) throws SipSystemException;

	/**
	 * 预置位设置
	 * @param model
	 * @return
	 * @throws SipSystemException
	 */
	PresetVo presetCmd(PresetQuery model)throws SipSystemException;
	
	
	/**
	 * 录像控制 【包括开始录像  结束录像】
	 * @param model
	 * @return
	 * @throws SipSystemException
	 */
	VideotapeVo mediaTapeControl(VideotapeQuery model)throws SipSystemException;
	
	/**
	 * 巡航控制
	 * @param model
	 * @return
	 * @throws SipSystemException
	 */
	CruiseVo CruiseControl(CruiseQuery model)throws SipSystemException;
	/**
	 * 设备状态查询
	 * @param deviceCode 设备编码
	 * @return
	 * @throws SipSystemException
	 */
	DeviceStatusVo searchDeviceStatus(String deviceCode)throws SipSystemException;

}
