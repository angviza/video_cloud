package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>轮询预案关联中间表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_pollingplan_camera")
public class PollingplanCamera implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联轮询预案表id db_column: pollingplan_id 
     */
    @Column(name = "pollingplan_id")
	private String pollingplanId;

    /**
     * cameraId db_column: camera_id 
     */
    @Column(name = "camera_id")
	private String cameraId;

    /**
     * 自定义分组id（冗余字段） db_column: cameragroup_id 
     */
    @Column(name = "cameragroup_id")
	private String cameragroupId;

    /**
     * 自定义分组名称（冗余字段） db_column: cameragroup_name 
     */
    @Column(name = "cameragroup_name")
	private String cameragroupName;

    /**
     * 关联类型：1.地址树选择摄像机、2.摄像机组选择摄像机、3.地图选择摄像机 db_column: type 
     */
    @Column
	private Integer type;

    /**
     * 排序序号
     */
    @Column
	private Integer sort;
}

