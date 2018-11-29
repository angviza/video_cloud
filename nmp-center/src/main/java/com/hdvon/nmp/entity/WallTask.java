package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>上墙主表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_wall_task")
public class WallTask implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;
    
    @Column(name = "name")
	private java.lang.String name;

    /**
     * 用户表id db_column: user_id 
     */
    @Column(name = "user_id")
	private java.lang.String userId;

    /**
     * 关联矩阵 db_column: matrix_id 
     */
    @Column(name = "matrix_id")
   	private java.lang.String matrixId;


}

