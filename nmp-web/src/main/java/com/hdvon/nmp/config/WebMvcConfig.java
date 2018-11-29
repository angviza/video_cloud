package com.hdvon.nmp.config;

import com.hdvon.nmp.common.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MVC配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Autowired
    private MvcInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                //登录、注销、忘记密码、重置密码、swagger等操作无需拦截,首页功能操作页面,视频监控初始化界面
                .excludePathPatterns(
                        "/user/login",
                        "/user/logout",
                        "/user/faceLogin",//人脸登录
                        "/user/modifyPassword",//修改密码
                        "/user/dragOptionMenu",//在自定义菜单区域内的拖动
                        "/user/uploadHeadPortrait",//上传用户头像
                        "/user/resetPassword",
                        "/user/forgotPassword",
                        "/user/findOptionMenu",//查询自定义菜单列表
                        "/user/editOptionMenu",//修改自定义菜单项
                        "/user/lockScreen",//用户锁屏
                        "/user/unLockScreenPasswd",//用户密码解锁
                        "/user/unLockScreenFace",//用户人脸解锁
                        "/user/saveValidType",//保存用户验证类型设置
                        "/user/userState",// 用户在线状态
                        "/user/getValidTypeList",//获取用户验证类型列表
                        "/user/settingValidType",//获取用户设置验证类型
                        "/userSetting/userFilepath",//获取用户个人设置信息
                        "/userSetting/saveUserFilepath",//保存用户个人设置信息
                        "/collect/**",//收藏夹
                        "/sysmenu/getSysmenusList",// 查询菜单
                       /* "/collect/",//获取所有收藏夹列表
                        "/collect/saveCollect",//保存收藏夹
                        "/collect/deleteCollect",//批量删除收藏夹
                        "/collect/getCollectMapping",//获取分页收藏摄像机列表信息
                        "/collect/saveCollectMapping",//收藏摄像机
                        "/collect/deleteCollectMapping",//批量删除收藏摄像机
*/                        "/notice/**",//通知公告模块
                        /*"/notice/queryNoticeType",//查找全部公告类型
                        "/notice/getNoticePage",//公告信息（发件箱）
                        "/notice/getReceiveMessagePage",//公告信息（收件箱）
                        "/notice/uploadFiles",//上传文件
                        "/notice/saveNotice",//保存公告信息
                        "/notice/updateNoticeFlag",//更新公告信息状态
                        "/notice/queryMessageByNoticeType",//当前登录的用户公告信息提示
                        "/notice/queryNoticeUnRead",//当前登录的未读数及最新信息
                        "/notice/getRemindMessage",//
*/                       "/swagger-resources/**",
                        "/deviceExport/**",//案事件管理模块
                        "/templates/**",
                        "/error"
                );
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/META-INF/resources/templates/");
        super.addResourceHandlers(registry);
    }
}
