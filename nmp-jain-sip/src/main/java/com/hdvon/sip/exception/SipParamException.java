package com.hdvon.sip.exception;

/**
 * 参数异常类
 * @author wanshaojian 
 *
 */
@SuppressWarnings("serial")
public class SipParamException extends RuntimeException {
	public SipParamException(String message) {
        super(message);
    }
	
	public SipParamException(Exception e) {
        super(e);
    }
}
