package com.hdvon.quartz.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户行为日志表 实体类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_log")
public class UserLog implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 操作人姓名 db_column: name 
     */
	private java.lang.String name;

    /**
     * 操作人账号 db_column: account 
     */
	private java.lang.String account;

    /**
     * 操作对象 db_column: operation_object 
     */
    @Column(name = "operation_object")
	private java.lang.String operationObject;

    /**
     * 视频操作类型 1 实时播放 ;2  录像回放;3 录像下载；4 云台控制 db_column: type 
     */
	private java.lang.String type;

    /**
     * 操作类型 db_column: operation_type 
     */
    @Column(name = "operation_type")
	private java.lang.String operationType;

    /**
     * 日志类型（菜单） db_column: menu_id 
     */
    @Column(name = "menu_id")
	private java.lang.String menuId;

    /**
     * 用户token db_column: token_id 
     */
    @Column(name = "token_id")
	private java.lang.String tokenId;

    /**
     * content db_column: content 
     */
	private java.lang.String content;

    /**
     * 操作时间 db_column: operation_time 
     */
    @Column(name = "operation_time")
	private java.util.Date operationTime;

    /**
     * 更新时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * responseTime db_column: response_time 
     */
    @Column(name = "response_time")
	private java.lang.Long responseTime;

    /**
     * 是否同步 db_column: is_sync 
     */
    @Column(name = "is_sync")
	private java.lang.String isSync;

}

