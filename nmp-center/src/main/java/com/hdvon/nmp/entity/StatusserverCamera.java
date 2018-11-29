package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>状态服务器关联摄像机 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_statusserver_camera")
public class StatusserverCamera implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联摄像机id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

    /**
     * 状态服务器id db_column: statusserver_id 
     */
    @Column(name = "statusserver_id")
	private java.lang.String statusserverId;

    /**
     * 摄像机名称 db_column: camera_name 
     */
    @Column(name = "camera_name")
	private java.lang.String cameraName;

    /**
     * 摄像机编号 db_column: camera_no 
     */
    @Column(name = "camera_no")
	private java.lang.String cameraNo;

    /**
     * 摄像机类型 db_column: camera_type 
     */
    @Column(name = "camera_type")
	private java.lang.Integer cameraType;

    /**
     * 订阅状态（1在线；0离线） db_column: status 
     */
	private java.lang.Integer status;


}

