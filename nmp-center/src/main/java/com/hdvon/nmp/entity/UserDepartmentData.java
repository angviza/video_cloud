package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户可选部门关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_department_data")
public class UserDepartmentData implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 用户id db_column: user_id 
     */
    @Column(name = "user_id")
	private java.lang.String userId;

    /**
     * 部门id db_column: department_id 
     */
    @Column(name = "department_id")
	private java.lang.String departmentId;



}

