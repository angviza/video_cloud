package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户组织机构关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_orgnization")
public class UserOrgnization implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联用户表的id db_column: user_id 
     */
    @Column(name = "user_id")
	private String userId;

    /**
     * 关联组织结构表的id db_column: organization_id 
     */
    @Column(name = "organization_id")
	private String organizationId;


}

