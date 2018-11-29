package com.hdvon.sip.config.ws;

import java.security.Principal;

/**
 * 功能：
 * 作者：chenjiefeng
 * 日期：2018/10/17
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
public class MyPrincipal implements Principal {

    private String name;
    //请求的ip地址
    private String reqIp;


	public MyPrincipal(String name,String reqIp){
        this.name = name;
        this.reqIp = reqIp;
    }
    @Override
    public String getName() {
        return name;
    }
    public String getReqIp() {
		return reqIp;
	}
	
}
