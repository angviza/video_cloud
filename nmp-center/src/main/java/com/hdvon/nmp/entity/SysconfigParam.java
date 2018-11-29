package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>系统配置参数 实体类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_sysconfig_param")
public class SysconfigParam implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键 db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 配置项名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 英文名称(标识) db_column: en_name 
     */
    @Column(name = "en_name")
	private java.lang.String enName;

    /**
     * 配置项值 db_column: value 
     */
	private java.lang.String value;

    /**
     * 是否开启（1.开启，0.关闭） db_column: state 
     */
	private java.lang.String state;

    /**
     * 描述信息 db_column: description 
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


}

