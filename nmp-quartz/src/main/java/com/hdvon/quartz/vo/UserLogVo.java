package com.hdvon.quartz.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户行为日志表 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class UserLogVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户操作日志ID
	 */
    private java.lang.String id;

    /**
     * 用户姓名
     */
    private java.lang.String name;

    /**
     * 用户账号
     */
    private java.lang.String account;

    /**
     * 操作对象
     */
    private java.lang.String operationObject;

    /**
     * 操作类型	1：视频播放  2：录像回放  3：录像下载  4：云台控制
     */
    private java.lang.String type;

    /**
     * 暂时未使用
     */
    private java.lang.String operationType;

    /**
     * 菜单ID
     */
    private java.lang.String menuId;

    /**
     * 登录token
     */
    private java.lang.String tokenId;

    /**
     * 操作内容
     */
    private java.lang.String content;

    /**
     * 操作时间
     */
    private java.util.Date operationTime;

    /**
     * 菜单名称
     */
    private java.lang.String menuName;
    
    /**
     * 更新时间
     */
    private java.util.Date updateTime;
    
    private java.lang.String typeName;

    /**
     * 响应时间
     */
    private java.lang.Long responseTime;
    
    /**
     * 是否同步
     */
    private java.lang.String isSync;

}

