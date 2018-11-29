package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>摄像头录像标签表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_camera_label")
public class CameraLabel implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 标签名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 摄像机id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

    /**
     * 录像开始时间 db_column: start_time 
     */
    @Column(name = "start_time")
	private java.util.Date startTime;

    /**
     * 录像结束时间 db_column: end_time 
     */
    @Column(name = "end_time")
	private java.util.Date endTime;

    /**
     * 创建时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 修改时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * 创建人 db_column: create_user 
     */
    @Column(name = "create_user")
	private java.lang.String createUser;

    /**
     * 修改人 db_column: update_user 
     */
    @Column(name = "update_user")
	private java.lang.String updateUser;


}

