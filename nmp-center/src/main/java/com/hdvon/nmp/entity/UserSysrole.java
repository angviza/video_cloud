package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户与系统角色关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_sysrole")
public class UserSysrole implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联用户表id db_column: user_id 
     */
    @Column(name = "user_id")
	private String userId;

    /**
     * 关联角色表id db_column: role_id 
     */
    @Column(name = "role_id")
	private String roleId;


}

