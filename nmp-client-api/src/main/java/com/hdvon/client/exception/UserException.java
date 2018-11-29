package com.hdvon.client.exception;

/**
 * 服务调用异常
 * @author wanshaojian
 */
public class UserException extends RuntimeException  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserException(String message) {
        super(message);
    }
}
