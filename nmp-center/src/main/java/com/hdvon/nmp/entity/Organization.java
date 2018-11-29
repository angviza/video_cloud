package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>组织机构表/行政区划(国标) 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_organization")
public class Organization implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
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
     * 机构代码 db_column: org_code 
     */
    @Column(name = "org_code")
	private String orgCode;

    /**
     * 父级机构代码 db_column: parent_org_code 
     */
    @Column(name = "parent_org_code")
	private String parentOrgCode;
    
    /**
     * 根节点到当前节点code拼接 db_column: org_code_split 
     */
    @Column(name = "org_code_split")
	private String orgCodeSplit;

    /**
     * 是否虚拟组织，1.是,0.否 db_column: is_virtual 
     */
    @Column(name = "is_virtual")
	private Integer isVirtual;

    /**
     * 机构类型 db_column: org_type 
     */
    @Column(name = "org_type")
	private Integer orgType;

    /**
     * 业务分组id db_column: buss_group_id 
     */
    @Column(name = "buss_group_id")
	private String bussGroupId;

    /**
     * 节点层级，区分是否虚拟组织，例如：3级行政区划下的虚拟组织为1级虚拟组织（用于限制可添加虚拟组织的最多层级） db_column: level 
     */
	private Integer level;

    /**
     * 描述 db_column: description 
     */
	private String description;

    /**
     * 同级排序 db_column: orderby 
     */
	private Integer orderby;

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

