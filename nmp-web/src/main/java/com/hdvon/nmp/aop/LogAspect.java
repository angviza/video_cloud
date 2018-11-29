package com.hdvon.nmp.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.ISysmenuService;
import com.hdvon.nmp.service.IUserLogService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.RedisHelper;
import com.hdvon.nmp.vo.SysmenuVo;
import com.hdvon.nmp.vo.UserLogVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>自定义日志AOP拦截器<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/5/15<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
@Aspect  
@Component
public class LogAspect {
	@Autowired 
	private RedisHelper redisHelper;
	
	@Reference
	private ISysmenuService sysmenuService;
	
	@Reference
	private IUserLogService userLogService;
	
    //Controller层切点    
    @Pointcut("@annotation(com.hdvon.nmp.aop.ControllerLog)") 
    public void webLog(){}  
  
//    @Before("webLog()")  
//    public void deBefore(JoinPoint joinPoint) throws Throwable {  
//        // 接收到请求，记录请求内容  
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();  
//        HttpServletRequest request = attributes.getRequest();  
//        // 记录下请求内容  
//        log.info("URL : " + request.getRequestURL().toString());  
//        log.info("HTTP_METHOD : " + request.getMethod());  
//        log.info("IP : " + request.getRemoteAddr());  
//        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());  
//        //log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));  
//    }  
  
//    @AfterReturning(returning = "ret", pointcut = "webLog()")  
//    public void doAfterReturning(Object ret) throws Throwable {  
//        // 处理完请求，返回内容  
//    	log.info("方法的返回值 : " + ret);  
//    }  
  
//    //后置异常通知  
//    @AfterThrowing("webLog()")  
//    public void throwss(JoinPoint jp){  
//    	log.info("方法异常时执行.....");  
//    }  
//  
    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行  
//    @After("webLog()")  
//    public void after(JoinPoint jp){  
//    	log.info("方法最后执行.....");  
//    }  
  
    /**
     * 环绕通知,环绕增强，相当于MethodInterceptor
     * 操作设备、用户登录、用户注销日志不在此处拦截
     * @param pjp
     * @return
     */
    @Around("webLog()")  
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
    	Calendar stCal = Calendar.getInstance();
    	long start = stCal.getTime().getTime();
    	log.info("日志拦截start.....");  
    	log.info("操作开始时间....."+start);  
        try {  
            ///////////////////////////执行controller的方法之前///////////////////////////////
            
            Object obj =  pjp.proceed();
            ///////////////////////////执行controller的方法之后///////////////////////////////
           
            ApiResponse response = (ApiResponse)obj;
            String code = response.getCode();
            
            if ("200".equals(code)) {	//返回成功
            	//接收到请求，记录请求内容  
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();  
                HttpServletRequest request = attributes.getRequest();
                String requestURI = request.getRequestURI();
                String method = request.getMethod();
                String url = request.getRequestURL().toString();
              
                String token = "";
                UserVo userVo = null;
            	String content = null;//内容
            	UserLogVo logVO = new UserLogVo();
                // 记录下请求内容  
                log.info("URL : " + url);  
                log.info("HTTP_METHOD : " + request.getMethod());
            	List<String> list = new ArrayList<String>();
            	
            	token = request.getHeader(WebConstant.TOKEN_HEADER);
                if(StrUtil.isBlank(token)) {
                    token = request.getParameter(WebConstant.TOKEN_HEADER);
                }
                if(token == null){
                    return obj;
                }
            
                userVo = (UserVo) redisHelper.getUserByToken(token);
                if (null != userVo) {
                	Calendar endCal = Calendar.getInstance();
                    long end = endCal.getTime().getTime();
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();
                    long responseTime = end -start;
                    
                    logVO = new UserLogVo();
                    logVO.setAccount(userVo.getAccount());
                    logVO.setName(userVo.getName());
                    // 为了不查询数据库，配置操作类型
                    if("POST".equals(method)) {
                    	 logVO.setType(WebConstant.USER_CONTORL_8);
                    }else if("GET".equals(method)) {
                    	 logVO.setType(WebConstant.USER_CONTORL_7);
                    }else if("DELETE".equals(method)) {
                    	 logVO.setType(WebConstant.USER_CONTORL_9);
                    }
                    logVO.setOperationTime(currentDate);
                    logVO.setResponseTime(responseTime);
                    content = getApiOperationValue(pjp);//操作模块内容
                    // 根据url 匹配对应带操作模块
                    String urlLike=requestURI.substring(0, requestURI.lastIndexOf("/")+1);
                	Map<String, Object> param = new HashMap<>();
                    List<SysmenuVo>  menuList = sysmenuService.getMenuFunctionByParam(param);
                	// jdk8新特性
                    List<SysmenuVo> sysmenu=menuList.stream().filter(vo-> vo.getUrl().contains(urlLike) ).collect(Collectors.toList());
                    if(sysmenu.size() >0) {
                    	logVO.setMenuId(sysmenu.get(0).getPid());
                    }

            		/******************************************** 处理界面操作的参数 **********************************/
            		if (!"GET".equals(method)) {
            			String value = "";
                        if (pjp.getArgs() !=  null && pjp.getArgs().length > 0) { 
                        	Object[] objs = pjp.getArgs();
                        	for ( int i = 0; i < pjp.getArgs().length; i++) {
                        		if (objs[i] != null) {
                            		Field[] field = objs[i].getClass().getDeclaredFields();
                            		value = "";
                            		
                            		// 遍历所有属性  
                                    for (int j = 0; j < field.length; j++) {
                                    	// 获取属性的名字  
                                        String name = field[j].getName();  
                                        if (!"serialVersionUID".equals(name) && !"id".equals(name)) {
                                        	if(field[j].isAnnotationPresent(ApiModelProperty.class)){  
                                                //获取字段注解  
                                        		ApiModelProperty columnConfig = field[j].getAnnotation(ApiModelProperty.class);
                                        		String column = columnConfig.value();
                                        		
                                        		// 将属性的首字符大写，方便构造get，set方法  
                                                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                                                Method m = objs[i].getClass().getMethod("get" + name);  
                                                // 调用getter方法获取属性值  
                                                value += column + ":" + m.invoke(objs[i]) + ",";
                                        	} 
                                        }
                                    }
                                    
                                    if (value.length()>=2) {
                                    	if (value.lastIndexOf(',')==value.length()-1) {
                                        	value = value.substring(0, value.lastIndexOf(','));
                                        }
                                    }
                                    content = content+"---" + value;
                                    list.add(content);
                        		} // if
                        	} // for
                        } // if
                		
                        for (int i=0;i<list.size();i++) {
                        	logVO.setContent(list.get(i));
                        	userLogService.saveUserLog(logVO);
                        }
            		} else {
            			logVO.setContent(content);
                		userLogService.saveUserLog(logVO);
            		}
                }
        	} 
            log.info("方法环绕proceed，结果是 :" + obj);
            return obj;
        } catch (Throwable e) {  
            throw e;
        }  
    }
    
    
    /**
     * 获取注解value值
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private String getApiOperationValue(JoinPoint joinPoint) throws Exception
      {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null)
        {
            return method.getAnnotation(ApiOperation.class).value();
        }
        return null;
    }
}
