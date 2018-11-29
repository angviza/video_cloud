package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>上墙预案关联中间表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_wallplan_camera")
public class WallplanCamera implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联上墙预案表id db_column: wallplan_id 
     */
    @Column(name = "wallplan_id")
	private java.lang.String wallplanId;

    /**
     * 矩阵通道id db_column: matrixchannel_id 
     */
    @Column(name = "matrixchannel_id")
	private java.lang.String matrixchannelId;

    /**
     * 关联摄像机表id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

    /**
     * 业务分组id（冗余字段） db_column: cameragroup_id 
     */
    @Column(name = "cameragroup_id")
	private java.lang.String cameragroupId;

    /**
     * 业务分组名称（冗余字段） db_column: cameragroup_name 
     */
    @Column(name = "cameragroup_name")
	private java.lang.String cameragroupName;

    /**
     * 关联类型：1.地址树选择摄像机、2.摄像机组选择摄像机、3.地图选择摄像机 db_column: type 
     */
    @Column
	private Integer type;

    /**
     * 上墙预案单个通道关联摄像机排序序号
     */
    @Column
	private Integer sort;
}

