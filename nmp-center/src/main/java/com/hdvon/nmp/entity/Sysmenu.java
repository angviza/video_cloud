package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>系统功能表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_sysmenu")
public class Sysmenu implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 功能名称 db_column: name 
     */
	private String name;

    /**
     * 父id db_column: pid 
     */
	private String pid;

    /**
     * 请求地址 db_column: url 
     */
	private String url;

    /**
     * 图标 db_column: icon 
     */
	private String icon;

    /**
     * 类型，1.菜单、2.操作 db_column: type 
     */
	private Integer type;

    /**
     * 提交方式:POST、GET、PUT、DELETE db_column: method 
     */
	private String method;

    /**
     * 编码 db_column: code 
     */
	private String code;

    /**
     * 描述 db_column: description 
     */
	private String description;
	
	/**
     * 编码 db_column: enabled 
     */
	private Integer enabled;

    /**
     * 颜色 db_column: color 
     */
	private String color;

    /**
     * 同级排序 db_column: orderby 
     */
	private Integer orderby;


}

