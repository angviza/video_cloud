package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>系统角色表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_sysrole")
public class Sysrole implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 角色名称 db_column: name 
     */
	private String name;

    /**
     * 是否已授权(1.是，0.否) db_column: is_menu_assigned 
     */
    @Column(name = "is_menu_assigned")
	private Integer isMenuAssigned;

    /**
     * 是否权限配置(1.是，0.否) db_column: is_perm_assigned
     */
    @Column(name = "is_perm_assigned")
    private Integer isPermAssigned;

    /**
     * 同级排序 db_column: orderby 
     */
	private Integer orderby;

    /**
     * 描述 db_column: description 
     */
	private String description;

    /**
     * 父id db_column: pid 
     */
	private String pid;

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

