package com.hdvon.nmp.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>通知公告表 实体类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_notice")
public class Notice implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 主题 db_column: theme 
     */
	private String theme;

    /**
     * 公告类型Id db_column: notice_type_id 
     */
    @Column(name = "notice_type_id")
	private String noticeTypeId;

    /**
     * 正文 db_column: content 
     */
	private String content;

    /**
     * 显示方式 db_column: show_method 
     */
    @Column(name = "show_method")
	private Integer showMethod;

    /**
     * 设备树 db_column: device_tree
     */
    @Column(name = "device_tree")
    private java.lang.String deviceTree;

    /**
     * 设备ID db_column: device_id
     */
    @Column(name = "device_id")
    private String deviceId;

    /**
     * 设备编码 db_column: device_code 
     */
    @Column(name = "device_code")
	private String deviceCode;

    /**
     * 设备名称 db_column: device_name 
     */
    @Column(name = "device_name")
	private String deviceName;

    /**
     * 设备端口 db_column: device_port
     */
    @Column(name = "device_port")
    private String devicePort;

    /**
     * 发布时效 db_column: time_setting 
     */
    @Column(name = "time_setting")
	private String timeSetting;

    /**
     * 发布时效结束 db_column: time_setting_end 
     */
    @Column(name = "time_setting_end")
	private String timeSettingEnd;

    /**
     * 创建时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 更新时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * 创建人 db_column: create_user 
     */
    @Column(name = "create_user")
	private String createUser;

    /**
     * 更新人 db_column: update_user 
     */
    @Column(name = "update_user")
	private String updateUser;


}

