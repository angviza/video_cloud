package com.hdvon.quartz.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>摄像机访问历史报表 实体类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_camera_report")
public class CameraReport implements Serializable{

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
     * 设备编码 db_column: sbbm 
     */
	private java.lang.String sbbm;

    /**
     * 设备名称 db_column: sbmc 
     */
	private java.lang.String sbmc;

    /**
     * 该时间段访问总数 db_column: total 
     */
	private java.lang.Integer total;

    /**
     * 统计时间 db_column: creat_time 
     */
    @Column(name = "creat_time")
	private java.util.Date creatTime;


}

