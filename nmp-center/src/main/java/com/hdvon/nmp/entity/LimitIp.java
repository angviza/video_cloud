package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户登录限制IP表 实体类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_limit_ip")
public class LimitIp implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * ip限制类型（1.允许的IP，0.限制的IP） db_column: type 
     */
	private java.lang.String type;

    /**
     * 是否是区域IP（1.是，0.否） db_column: is_regasion 
     */
    @Column(name = "is_regasion")
	private java.lang.String isRegasion;

    /**
     * 是否开启（1.开启，0.关闭） db_column: state 
     */
	private java.lang.String state;

    /**
     * ip开始区域 db_column: start_regaion 
     */
    @Column(name = "start_regaion")
	private java.lang.String startRegaion;

    /**
     * ip结束区域 db_column: end_regaion 
     */
    @Column(name = "end_regaion")
	private java.lang.String endRegaion;

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

