//package com.hdvon.nmp.aop;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.hdvon.nmp.common.WebConstant;
//import com.hdvon.nmp.service.IUserLogService;
//import com.hdvon.nmp.util.RedisHelper;
//import com.hdvon.nmp.vo.UserVo;
//
//import cn.hutool.core.util.StrUtil;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 
// * @author huanggx
// * 用户行为操作行为更新
// *
// */
//@Slf4j
//@Aspect  
//@Component
//public class AnalysisAspect {
//	
//	@Reference
//	private IUserLogService userLogService;
//	@Autowired
//	private RedisTemplate redisTemplate;
//	@Autowired 
//	private RedisHelper redisHelper;
//	
//	
//	//@Pointcut("execution(public * com.hdvon.nmp.controller.system.*.*(..))") 
//	@Pointcut("@annotation(io.swagger.annotations.ApiOperation)") 
//    public void analysisLog(){}  
//  
//    @Before("analysisLog()")  
//    public void deBefore(JoinPoint joinPoint) throws Throwable {  
//       //更新用户行为日志更新时间
//    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();  
//        HttpServletRequest request = attributes.getRequest();
//        String requestURI = request.getRequestURI();
//        if ( ! requestURI.contains("user")) {//用户模块不做处理
//        	String token = request.getHeader(WebConstant.TOKEN_HEADER);
//        	UserVo userVo = (UserVo) redisHelper.getUserByToken(token);
//        	if(userVo ==null) {
//        		return ;
//        	}
//        	if(! redisTemplate.hasKey(token) && StrUtil.isNotBlank(token)) {
//        		 Map<String,String > param = new HashMap<>();
//        		 param.put("token", token);
//        		 param.put("account", userVo.getAccount());
//        		 userLogService.updateLog(param);
//        		 redisTemplate.opsForValue().set(token, "1");
//    			 redisTemplate.expire(token, WebConstant.ASALYSIS_SECONDS, TimeUnit.SECONDS);
//    		}
//        }
//    } 
//
//}
