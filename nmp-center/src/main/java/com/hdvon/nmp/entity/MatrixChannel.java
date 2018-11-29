package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>数字矩阵通道 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_matrix_channel")
public class MatrixChannel implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 矩阵通道名称 db_column: name 
     */
	private String name;

    /**
     * 矩阵通道设备编号 db_column: devices_no 
     */
    @Column(name = "devices_no")
	private String devicesNo;

    /**
     * description db_column: description 
     */
	private String description;

    /**
     * matrixId db_column: matrix_id 
     */
    @Column(name = "matrix_id")
	private String matrixId;

    /**
     * createTime db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * updateTime db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * createUser db_column: create_user 
     */
    @Column(name = "create_user")
	private String createUser;

    /**
     * updateUser db_column: update_user 
     */
    @Column(name = "update_user")
	private String updateUser;


}

