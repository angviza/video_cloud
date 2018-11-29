package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>上墙轮巡的摄像机表 实体类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_wall_camera")
public class WallCamera implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 上墙轮巡主表id db_column: rotate_id 
     */
    @Column(name = "rotate_id")
	private java.lang.String rotateId;

    /**
     * 摄像机id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

    /**
     * 顺序 db_column: queue 
     */
    @Column(name = "queue")
	private java.lang.Integer queue;


}

