package com.hdvon.client.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>部门表 实体类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_department")
public class Department implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
    @Column(name = "Id")
	private String id;

    /**
     * 机构名称 db_column: name 
     */
	private String name;

    /**
     * 父id db_column: pid 
     */
	private String pid;

    /**
     * 部门代码 db_column: dep_code 
     */
	private String depCode;

    /**
     * 父级机构代码 db_column: parent_dep_code 
     */
	private String parentDepCode;

    /**
     * 是否建设部门(1.是，0.否) db_column: is_constructor 
     */
	private Integer isConstructor;

    /**
     * 是否承建单位(1.是，0.否) db_column: is_builder 
     */
	private Integer isBuilder;

    /**
     * 联系人 db_column: contactor 
     */
	private String contactor;

    /**
     * 电话号码 db_column: telelno 
     */
	private String telelno;

    /**
     * mobile db_column: mobile 
     */
	private String mobile;

    /**
     * 同级排序 db_column: orderby 
     */
	private Integer orderby;

    /**
     * 联系地址 db_column: address 
     */
	private String address;

    /**
     * 描述 db_column: description 
     */
	private String description;

    /**
     * 所属组织机构 db_column: org_id 
     */
	private String orgId;

    /**
     * 创建时间 db_column: create_time 
     */
	private Date createTime;

    /**
     * 修改时间 db_column: update_time 
     */
	private Date updateTime;

    /**
     * 创建人 db_column: create_user 
     */
	private String createUser;

    /**
     * 修改人 db_column: update_user 
     */
	private String updateUser;


}

