package com.hdvon.nmp.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.service.ISysmenuService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.RedisHelper;
import com.hdvon.nmp.util.ResponseCode;
import com.hdvon.nmp.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 功能：自定义拦截器
 * 作者：guoweijun
 * 日期：2018/04/12
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Slf4j
@Component
public class MvcInterceptor extends HandlerInterceptorAdapter {

    @Reference
    private ISysmenuService sysmenuService;

    @Autowired
    private RedisHelper redisHelper;

    /**
     * springMVC已经找到对应的处理方法，在支付controller方法前执行此过滤
     * 重写preHandle方法，在执行方法之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    	
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info("本次请求preHandle uri="+request.getRequestURI()+" ...请求函数："+request.getMethod()+" ...当前时间="+System.currentTimeMillis());

    	//检查token
    	String token = request.getHeader(WebConstant.TOKEN_HEADER);
        if (StrUtil.isBlank(token)) {
        	sendError(ResponseCode.TOKEN_NOT_EXIST,request,response);
            return false;
        }

        //检查账号
        UserVo uservo = redisHelper.getUserByToken(token);
        if (uservo == null) {
        	sendError(ResponseCode.LOGIN_EXPIRED,request,response);
            return false;
        }


//        //检查账号是否已经在其他地方登录
//        String currentToken = redisHelper.getCurrentToken(uservo.getId());
//        if(!token.equals(currentToken)){
//            sendError(ResponseCode.LOGIN_EXIST,request,response);
//            return false;
//        }

        //检查账号是否被锁屏
        if(!(request.getRequestURI().equals("/user/unLockScreenPasswd") ||
        		request.getRequestURI().equals("/user/unLockScreenFace") )) {
            if(redisHelper.existLockScerrn(uservo.getId(), token)) {
            	sendError(ResponseCode.LOCK_SCREEN,request,response);
                return false;
            }
        }
        
        //检查接口权限
        if(!uservo.isAdmin()){
            String menuPermission = uservo.getMenuPermission();
            if(menuPermission == null){
                menuPermission = "";
            }
            List<String> menus = Arrays.asList(menuPermission.split(","));
            String key1 = "";
            String key2 = "";
            if(requestURI.lastIndexOf("/") != requestURI.length()){
                requestURI += "/";
            }
            String[] splits = requestURI.split("/");
            if(splits.length >= 2){
                key1 = "/" +splits[1]+"/_"  +method;
            }
            if(splits.length == 3){
                key2 = "/" +splits[1]+"/" +splits[2] + "/_"+method;
            }
            if((menus.indexOf(key1) == -1 && menus.indexOf(key2) == -1)){
                sendError(ResponseCode.TOKEN_PERMISSION_DENIED,request,response);
                return false;
            }
        }

        return true;
    }

    /**
     * 重写postHandle方法，在执行方法完成之后执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("本次请求postHandle uri="+request.getRequestURI()+"...当前时间="+System.currentTimeMillis());
    }


    private void sendError(ResponseCode responseCode, HttpServletRequest request, HttpServletResponse response ){
        sendError(responseCode.getCode() , responseCode.getMessage() , request , response);
    }

    private void sendError( String errorMsg , HttpServletRequest request, HttpServletResponse response ){
        sendError(HttpServletResponse.SC_UNAUTHORIZED + "" , errorMsg , request , response);
    }

    private void sendError( String code , String errorMsg , HttpServletRequest request, HttpServletResponse response ){
        //设置编码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        //设置错误信息
        log.error("请求失败："+errorMsg);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.error(errorMsg);
        apiResponse.setCode(code);

        //输出
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSONObject.toJSONString(apiResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
