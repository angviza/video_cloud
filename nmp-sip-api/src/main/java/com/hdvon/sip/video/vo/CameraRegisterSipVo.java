package com.hdvon.sip.video.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>视频监控需要参数 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class CameraRegisterSipVo extends BaseOptionVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String deviceName;//设备名称
	
	private String registeredName;//注册用户名

	private String registeredPass;//注册用户密码

    private String registeredServerPort; //注册服务器端口号
    
    //中心信令服务器IP/网关服务器IP(注册服务器IP)
    private String gatewayip;

    //中心信令域/网关域(注册服务器域名)
    private String gatewaydomain;

    //中心信令ID/网关ID(注册服务器ID)
    private String gatewayid;
    
    
    private String clientIp;//点播客户端ip地址
    
    private String clientPort;//点播客户端插件播放端口
    
    private String type;//轮询使用不同的账号注册 : one ,two

}
