package com.hdvon.client.exception;

/**
 * 服务调用异常
 */
public class ServiceException extends RuntimeException  {
    public ServiceException(String message) {
        super(message);
    }
}
