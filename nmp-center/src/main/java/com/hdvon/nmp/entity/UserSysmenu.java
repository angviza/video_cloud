package com.hdvon.nmp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户自定义菜单关联表 实体类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_sysmenu")
public class UserSysmenu implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */	
	@Id
	private String id;
	
    /**
     * 用户id db_column: user_id 
     */	
	@Column(nullable = false)
	private String userId;
	
    /**
     * 系统功能表id db_column: sysmenu_id 
     */	
	@Column(nullable = false)
	private String sysmenuId;
	
    /**
     * 自定义菜单父节点id db_column: sysmenu_pid 
     */	
	@Column(nullable = false)
	private String sysmenuPid;
	
    /**
     * 自定义菜单名称 db_column: sysmenu_name 
     */	
	@Column(nullable = false)
	private String sysmenuName;
	
    /**
     * 是否隐藏 db_column: hide 
     */	
	@Column(nullable = false)
	private Integer hide;

	/**
     * 同级排序 db_column: orderby 
	 */
	private Integer orderby;

}

