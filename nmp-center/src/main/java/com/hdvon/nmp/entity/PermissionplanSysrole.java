package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>权限预案与系统角色关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_permissionplan_sysrole")
public class PermissionplanSysrole implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联权限预案id db_column: permissionplan_id 
     */
    @Column(name = "permissionplan_id")
	private java.lang.String permissionplanId;

    /**
     * 关联系统角色id db_column: sysrole_id 
     */
    @Column(name = "sysrole_id")
	private java.lang.String sysroleId;


}

