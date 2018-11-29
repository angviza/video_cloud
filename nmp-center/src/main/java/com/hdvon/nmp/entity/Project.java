package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>项目信息 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_project")
public class Project implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 项目名称 db_column: name 
     */
	private String name;

    /**
     * 项目编号 db_column: code 
     */
	private String code;

    /**
     * 项目类型（1，新建项目；2：升级改造项目；9,其他项目'） db_column: type 
     */
	private String type;

    /**
     * 项目类点 db_column: class_type 
     */
    @Column(name = "class_type")
	private Integer classType;

    /**
     * 项目描述 db_column: description 
     */
	private String description;

    /**
     * 同级排序 db_column: orderby 
     */
	private Integer orderby;

    /**
     * 联系人 db_column: contactor 
     */
	private String contactor;

    /**
     * 电话 db_column: teleno 
     */
	private String teleno;

    /**
     * 手机 db_column: mobile 
     */
	private String mobile;

    /**
     * 邮箱 db_column: email 
     */
	private String email;

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
	private String createUser;

    /**
     * 修改人 db_column: update_user 
     */
    @Column(name = "update_user")
	private String updateUser;


}

