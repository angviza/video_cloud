package com.hdvon.sip.exception;


@SuppressWarnings("serial")
public class SipSystemException extends RuntimeException {
	public SipSystemException(String message) {
        super(message);
    }
	
	public SipSystemException(Exception e) {
        super(e);
    }
}
