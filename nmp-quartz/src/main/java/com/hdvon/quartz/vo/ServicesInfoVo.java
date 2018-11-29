package com.hdvon.quartz.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class ServicesInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

    private java.lang.Long id;

    private java.lang.String ipAddress;

    private java.lang.Integer port;

    private java.lang.String name;

    private java.lang.String code;
    
    private java.lang.String type;

    private java.lang.Integer enabled;

    private java.lang.Integer serverStatus;

    private java.lang.String userName;

    private java.lang.String password;

    private java.lang.String cpuUseRate;

    private java.lang.String memoryUseRate;

    private java.lang.String networkUseRate;

    private java.lang.String diskUseRate;

    private java.lang.String connectivity;

    private java.lang.String packetLostRate;

    private java.lang.Long averageDelay;

    private java.lang.Integer onlineStatus;
    
    private java.lang.String onlineState;

    private java.lang.Long concurrentRequest;

    private java.lang.Long connections;

    private java.lang.Long requestFailure;

    private java.util.Date createTime;

    private java.util.Date updateTime;

    private java.lang.String createUser;

    private java.lang.String updateUser;

}

