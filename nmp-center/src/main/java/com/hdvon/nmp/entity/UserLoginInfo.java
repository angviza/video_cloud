package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b> 实体类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_login_info")
public class UserLoginInfo implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.Long id;

    /**
     * userName db_column: user_name 
     */
    @Column(name = "user_name")
	private java.lang.String userName;

    /**
     * deptId db_column: dept_id 
     */
    @Column(name = "dept_id")
	private java.lang.String deptId;

    /**
     * loginTime db_column: login_time 
     */
    @Column(name = "login_time")
	private java.util.Date loginTime;


}

