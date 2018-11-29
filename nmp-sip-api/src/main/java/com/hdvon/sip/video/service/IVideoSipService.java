package com.hdvon.sip.video.service;

import java.util.List;

import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.ControlOptionInputVo;
import com.hdvon.sip.video.vo.ControlOptionSipVo;
import com.hdvon.sip.video.vo.CruiseOptionInputVo;
import com.hdvon.sip.video.vo.CruiseOptionSipVo;
import com.hdvon.sip.video.vo.FileResponseVo;
import com.hdvon.sip.video.vo.InviteOptionInputVo;
import com.hdvon.sip.video.vo.InviteOptionSipVo;
import com.hdvon.sip.video.vo.PlayDownloadInputVo;
import com.hdvon.sip.video.vo.PresetOptionInputVo;
import com.hdvon.sip.video.vo.PresetOptionSipVo;
import com.hdvon.sip.video.vo.QueryPreOptionInputVo;
import com.hdvon.sip.video.vo.QueryPreOptionSipVo;
import com.hdvon.sip.video.vo.QueryRecOptionInputVo;
import com.hdvon.sip.video.vo.QueryRecOptionSipVo;
import com.hdvon.sip.video.vo.RecordCtrlOptionSipVo;
import com.hdvon.sip.video.vo.RecordOptionSipVo;
import com.hdvon.sip.video.vo.RegisterCallback;
import com.hdvon.sip.video.vo.RegisterCredSipVo;
import com.hdvon.sip.video.vo.RegisterOptionSipVo;
import com.hdvon.sip.video.vo.ResponseVideoVo;
import com.hdvon.sip.video.vo.WiperOptionInputVo;
import com.hdvon.sip.video.vo.WiperOptionSipVo;

/**
 * <br>
 * <b>功能：</b>摄像机监控服务接口<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/5/21<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IVideoSipService {
	
	/** 启动
	 * @return boolean true:成功，false:失败
	 */
	public boolean startUp(String ip, int port);
	
	/** 注册相关
	 * @param option	视频监控注册参数
	 * @param cred		视频监控凭据参数
	 * @return	callId	返回的注册ID
	 */
	public RegisterCallback callRegister(RegisterOptionSipVo option, RegisterCredSipVo cred);
	
	/** 注销
	 * @param registerId	callRegister方法返回的注册ID
	 */
	public void callUnRegister(String registerId);
	
	/** invite相关
	 * @param option	视频监控Invite参数
	 * @return	invite请求的ID
	 */
	public CallbackResponseVo callInvite(InviteOptionSipVo option);
	
	/** invite请求终止
	 * @param inviteId	invite请求的ID
	 */
	public CallbackResponseVo callTerminate(String inviteId);
	
	/**
	 * @param inviteId	invite请求的ID	
	 */
	public void callACK(String inviteId);
	
	/**	云台控制 上下左右
	 * @param ctrOption	云台控制参数
	 * @return
	 */
	public CallbackResponseVo callCloudCmd(ControlOptionSipVo ctrOption);
	
	/**
	 * 回看控制
	 * @param callId
	 * @param vo
	 * @return
	 */
	public CallbackResponseVo playBackCtrl(String callId, RecordCtrlOptionSipVo vo);
	/**
	 * 录像控制
	 * @param param
	 * @return
	 */
	public CallbackResponseVo palyDownload(RecordOptionSipVo param);
	
	/**	预置位设置控制
	 * @param ctrOption	云台控制参数
	 * @return
	 */
	public CallbackResponseVo callPresetCmd(PresetOptionSipVo presetOption);
	

	/**
	 * 雨刷控制
	 * @param param
	 * @return
	 */
	public CallbackResponseVo wiperControl(WiperOptionSipVo param);

	/**
	 * 预置位查询
	 * @param paramVo
	 * @return
	 */
	public CallbackResponseVo queryPreset(QueryPreOptionSipVo paramVo);
	
	/**
	 * 巡航预案控制
	 * @param cruiseOption
	 * @return
	 */
	public CallbackResponseVo callCruiseCmd(CruiseOptionSipVo cruiseOption);

	/**
	 * 录像查询
	 * @param param
	 * @return
	 */
	public CallbackResponseVo getVideoFile(QueryRecOptionSipVo param);

	public CallbackResponseVo invite(InviteOptionInputVo vo);
	
	public void videoStop(String inviteIds);
	
	public ControlOptionSipVo directionControl(ControlOptionInputVo vo);
	
	public PresetOptionSipVo presetControl(PresetOptionInputVo vo);
	
	public CallbackResponseVo queryPreset(QueryPreOptionInputVo vo);
	
	public ControlOptionSipVo cloudControl(ControlOptionInputVo vo);
	
	public String playDownload(PlayDownloadInputVo vo);
	
	public void wiperControl(WiperOptionInputVo vo);
	
	public CruiseOptionSipVo cruiseControl(CruiseOptionInputVo vo);
	
	public List<CallbackResponseVo> cameraMutiChannelPlayback(InviteOptionInputVo vo, List<Integer> prots);
	
	public List<String> playbackCtrl(RecordCtrlOptionSipVo vo, String callId);
	
	public String playbackCtrlInvite(String callId, RecordCtrlOptionSipVo vo);
	
	public List<ResponseVideoVo> getDownloadVideo(QueryRecOptionInputVo vo);
	
	public List<FileResponseVo> responseStatus(String callIds);
	
	/**
	 * 所有的关于请求信令都经过这里
	 * 用户注册请求注册参数
	 * 
	 * @return
	 */
	public RegisterCallback register(String type);
}
