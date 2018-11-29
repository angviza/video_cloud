package com.hdvon.nmp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>设备编码生成字典表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_devicecode_dictionary")
public class DevicecodeDictionary implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;
    
    private String pid;

    /**
     * 编码位置 db_column: position 
     */
	private String position;

    /**
     * 对应位置的字典编码 db_column: code 
     */
	private String code;

    /**
     * 字典编码名称 db_column: name 
     */
	private String name;

    /**
     * 父字典编码 db_column: p_code 
     */
    @Column(name = "p_code")
	private String pcode;

    /**
     * 是否预留（1：是，0：否） db_column: is_reserved 
     */
    @Column(name = "is_reserved")
	private Integer isReserved;

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
    
    /**
     * 节点等级
     */
    @Column(name = "level")
    private Integer level;


}

