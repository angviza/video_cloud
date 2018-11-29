/*package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.jni.CallbackResponseVo;
import com.hdvon.nmp.vo.jni.ControlOptionParamVo;
import com.hdvon.nmp.vo.jni.InviteOptionParamVo;
import com.hdvon.nmp.vo.jni.RegisterCredParamVo;
import com.hdvon.nmp.vo.jni.RegisterOptionParamVo;

*//**
 * <br>
 * <b>功能：</b>摄像机监控服务接口<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/5/21<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 *//*
public interface ICameraMonitorService {
	
	*//** 启动
	 * @return boolean true:成功，false:失败
	 *//*
	public boolean startUp(String ip, int port);
	*//** 注册相关
	 * @param option	视频监控注册参数
	 * @param cred		视频监控凭据参数
	 * @return	callId	返回的注册ID
	 *//*
	public String callRegister(RegisterOptionParamVo option, RegisterCredParamVo cred);
	*//** 注销
	 * @param registerId	callRegister方法返回的注册ID
	 *//*
	public void callUnRegister(String registerId);
	*//** invite相关
	 * @param option	视频监控Invite参数
	 * @return	invite请求的ID
	 *//*
	public CallbackResponseVo callInvite(InviteOptionParamVo option);
	
	*//** invite请求终止
	 * @param inviteId	invite请求的ID
	 *//*
	public void callTerminate(String inviteId);
	*//**
	 * @param inviteId	invite请求的ID	
	 *//*
	public void callACK(String inviteId);
	*//**	云台控制 上下左右
	 * @param ctrOption	云台控制参数
	 * @return
	 *//*
	public String callCloudCmd(ControlOptionParamVo ctrOption);
	*//** 云台控制 取消控制
	 * @param ctrOption	云台控制参数
	 *//*
	public void callCloudReset(ControlOptionParamVo ctrOption);
	*//** 云台控制 镜头变倍
	 * @param ctrOption	云台控制参数
	 * @return
	 *//*
	public String callCloudZoom(ControlOptionParamVo ctrOption);
	*//**	云台控制 镜头变焦
	 * @param ctrOption	云台控制参数
	 * @return
	 *//*
	public String callCloudIris(ControlOptionParamVo ctrOption);
	*//**	云台控制 焦距调整
	 * @param ctrOption	云台控制参数
	 * @return
	 *//*
	public String callCloudFocus(ControlOptionParamVo ctrOption);
	
	public CallbackResponseVo getAsncCallbackResult(String callId);
	
}
*/