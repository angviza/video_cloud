package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;

/**
 * <br>
 * <b>功能：</b>报警设备表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_alarm_server")
public class AlarmServer implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 设备名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 设备编码 db_column: code 
     */
	private java.lang.String code;

    /**
     * 设备ip db_column: ip 
     */
	private java.lang.String ip;

    /**
     * 经度 db_column: jd 
     */
	private Double jd;

    /**
     * 纬度 db_column: wd 
     */
	private Double wd;

    /**
     * 关联地址id db_column: address_id 
     */
    @Column(name = "address_id")
	private java.lang.String addressId;

    /**
     * 关联摄像机id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

    /**
     * 关联预置位id db_column: present_id 
     */
    @Column(name = "present_id")
	private java.lang.String presentId;

    /**
     * addressName db_column: address_name 
     */
    @Column(name = "address_name")
	private java.lang.String addressName;

    /**
     * cameraName db_column: camera_name 
     */
    @Column(name = "camera_name")
	private java.lang.String cameraName;

    /**
     * presentName db_column: present_name 
     */
    @Column(name = "present_name")
	private java.lang.String presentName;

    /**
     * description db_column: description 
     */
	private java.lang.String description;

    /**
     * 创建时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 修改时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * 创建人 db_column: create_user 
     */
    @Column(name = "create_user")
	private java.lang.String createUser;

    /**
     * 修改人 db_column: update_user 
     */
    @Column(name = "update_user")
	private java.lang.String updateUser;

    /**
     * 启用状态(1启用，0停用)，默认停用 db_column: enabled 
     */
	private java.lang.Integer enabled;
}

