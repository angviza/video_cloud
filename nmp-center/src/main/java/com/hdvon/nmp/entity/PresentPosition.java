package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>预置位表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_present_position")
public class PresentPosition implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 预置位名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 预置位编号 db_column: present_no 
     */
    @Column(name = "present_no")
	private java.lang.String presentNo;

    /**
     * 是否守望位（1：是；0：否） db_column: is_keepwatch 
     */
    @Column(name = "is_keepwatch")
	private java.lang.Integer isKeepwatch;

    /**
     * 排序序号 db_column: sort 
     */
	private java.lang.Integer sort;

    /**
     * 关联摄像机id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

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

    @Column(name = "camera_code")
    private String cameraCode;

}

