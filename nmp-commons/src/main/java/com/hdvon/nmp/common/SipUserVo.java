package com.hdvon.nmp.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class SipUserVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private java.lang.String userName;

    private java.lang.String receiverIp;
    
}
