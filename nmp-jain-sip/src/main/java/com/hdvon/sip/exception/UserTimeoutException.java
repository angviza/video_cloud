package com.hdvon.sip.exception;

/**
 * 用户超时异常
 */
public class UserTimeoutException extends RuntimeException  {
    public UserTimeoutException(String message) {
        super(message);
    }
}
