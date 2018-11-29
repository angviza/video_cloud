package com.hdvon.client.exception;

/**
 * 服务调用异常
 * @author wanshaojian
 */
public class EsException extends RuntimeException  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EsException(String message) {
        super(message);
    }
}
