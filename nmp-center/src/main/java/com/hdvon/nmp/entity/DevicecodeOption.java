package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>设备编码生成器选项 实体类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_devicecode_option")
public class DevicecodeOption implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 省 db_column: province 
     */
	private java.lang.String province;

    /**
     * 城市 db_column: city 
     */
	private java.lang.String city;

    /**
     * 地区 db_column: area 
     */
	private java.lang.String area;

    /**
     * 基层单位 db_column: basic_nnit 
     */
    @Column(name = "basic_nnit")
	private java.lang.String basicNnit;

    /**
     * 行业 当code为08,09,13,14,15时，industryName必须有值 db_column: industry 
     */
	private java.lang.String industry;

    /**
     * 行业名称 db_column: industry_name 
     */
    @Column(name = "industry_name")
	private java.lang.String industryName;

    /**
     * 类型 db_column: type 
     */
	private java.lang.String type;

    /**
     * 网络标识 db_column: internet 
     */
	private java.lang.String internet;

    /**
     * 类型为5时-公安机关监控类型 db_column: contorl_type 
     */
    @Column(name = "contorl_type")
	private java.lang.String contorlType;

    /**
     * region db_column: region 
     */
	private java.lang.String region;

    /**
     * 类型为不为5,或者行业 当code为08,09,13,14,15时-投资单位  db_column: investment 
     */
	private java.lang.String investment;

    /**
     * 类型为不为5时-承建单位单位 db_column: construction 
     */
	private java.lang.String construction;

    /**
     * number db_column: number 
     */
	private java.lang.Integer number;

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

