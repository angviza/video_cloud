package com.hdvon.nmp.config;

import com.alibaba.dubbo.rpc.RpcException;
import com.hdvon.nmp.enums.JniCodeError;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.exception.SipServiceException;
import com.hdvon.nmp.exception.UserTimeoutException;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 功能：系统全局异常处理类
 * 作者：guoweijun
 * 日期：2018/04/15
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@ControllerAdvice
@Slf4j
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        log.error("系统异常：", e);
        ApiResponse apiResponse = new ApiResponse();
        if (e instanceof RpcException) {//dubbo后台异常
            return apiResponse.setResponseCode(ResponseCode.RPC_ERROR);
        } else if (e instanceof UserTimeoutException) {//用户超时
            return apiResponse.setResponseCode(ResponseCode.LOGIN_EXPIRED);
        } else if (e instanceof ServiceException) {//调用后台service出错
            return apiResponse.setResponseCode(ResponseCode.BAD_REQUEST).setMessage(e.getMessage());
        } else if (e instanceof NoHandlerFoundException) {//地址不存在
            return apiResponse.setResponseCode(ResponseCode.NOT_FOUND);
        } else if (e instanceof MethodArgumentNotValidException) {//参数请求不正确
            return apiResponse.setResponseCode(ResponseCode.BAD_REQUEST);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {//不支持的请求方式
            return apiResponse.setResponseCode(ResponseCode.HttpRequestMethodNotSupported_ERROR);
        } else if ((e instanceof SipServiceException)) {//JNI后台接口异常
            JniCodeError error = JniCodeError.getEnumByCode(Integer.parseInt(e.getMessage()));
            if (null != error) {
                String messge = null != error.getMsg() && !"".equals(error.getMsg()) ? "请求失败：" + error.getMsg() : "请求失败";
                return apiResponse.setCode(String.valueOf(error.getCode())).setMessage(messge);
            }

            return apiResponse.setResponseCode(ResponseCode.FAILURE);
        }else if (e instanceof RuntimeException) {//dubbo将所有异常都封装成RuntimeException，导致无法获取原来的异常类型
            if (e.getMessage() != null && e.getMessage().indexOf("Data too long for column") != -1) {
                return apiResponse.error("超出字段长度限制，请您重新输入");
            } else {
                return apiResponse.setResponseCode(ResponseCode.FAILURE);
            }
        }else {
            return apiResponse.setResponseCode(ResponseCode.FAILURE);
        }
    }
}