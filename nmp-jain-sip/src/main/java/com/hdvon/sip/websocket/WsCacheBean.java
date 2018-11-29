package com.hdvon.sip.websocket;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能：
 * 作者：chenjiefeng
 * 日期：2018/10/19
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsCacheBean implements Serializable {

    private String token;

    private String userId;

    private long connectTime;

    private long lastTime;
    //请求地址
    private String reqIp;
    
    private Map<String, Object> sipMap = new HashMap<String, Object>();//<callId, xxxx>
    
    
	/**
	 *  连接池存放
	 */
    private AtomicInteger portCounter;
	

	
}
