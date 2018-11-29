package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>权限预案关联资源角色表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_permissionplan_resourcerole")
public class PermissionplanResourcerole implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联权限预案表id db_column: permissionplan_id 
     */
    @Column(name = "permissionplan_id")
	private java.lang.String permissionplanId;

    /**
     * 关联资源角色表id db_column: resourcerole_id 
     */
    @Column(name = "resourcerole_id")
	private java.lang.String resourceroleId;


}

