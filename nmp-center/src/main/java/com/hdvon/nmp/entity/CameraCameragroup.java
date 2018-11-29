package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>摄像机表与摄像机分组的关联表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_camera_cameragroup")
public class CameraCameragroup implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 摄像机id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

    /**
     * 自定义摄像机分组id db_column: cameragroup_id 
     */
    @Column(name = "cameragroup_id")
	private java.lang.String cameragroupId;

    /**
     * 设备名称 db_column: camera_name 
     */
    @Column(name = "camera_name")
	private java.lang.String cameraName;

    /**
     * 设备编码 db_column: camera_code 
     */
    @Column(name = "camera_code")
	private java.lang.String cameraCode;
    
    /**
     * 组内摄像机排序序号 db_column: sort 
     */
	private java.lang.Integer sort;


}

