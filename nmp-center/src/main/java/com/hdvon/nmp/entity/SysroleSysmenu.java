package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>系统角色功能关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_sysrole_sysmenu")
public class SysroleSysmenu implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 系统角色表id db_column: sysrole_id 
     */
    @Column(name = "sysrole_id")
	private String sysroleId;

    /**
     * 系统功能表id db_column: sysmenu_id 
     */
    @Column(name = "sysmenu_id")
	private String sysmenuId;


}

