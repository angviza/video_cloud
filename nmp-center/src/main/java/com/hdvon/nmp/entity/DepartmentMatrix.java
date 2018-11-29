package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>部门关联矩阵中间表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_department_matrix")
public class DepartmentMatrix implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联部门id db_column: department_id 
     */
    @Column(name = "department_id")
	private String departmentId;

    /**
     * 关联矩阵id db_column: matrix_id 
     */
    @Column(name = "matrix_id")
	private String matrixId;


}

