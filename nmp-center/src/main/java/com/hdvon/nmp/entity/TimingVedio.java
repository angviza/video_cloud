package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>定时录像表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_timing_vedio")
public class TimingVedio implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联id db_column: store_camera_id 
     */
    @Column(name = "store_camera_id")
	private java.lang.String storeCameraId;

    /**
     * 星期几（星期一：1；星期二：2；星期三：3；星期四：4；星期五：5；星期六：6；星期日：7） db_column: day_of_week 
     */
    @Column(name = "day_of_week")
	private java.lang.Integer dayOfWeek;

    /**
     * 一天中某一段录像时间段的开始时间 db_column: bgn_time 
     */
    @Column(name = "bgn_time")
	private java.util.Date bgnTime;

    /**
     * 一天中某一段录像时间段的结束时间 db_column: end_time 
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

