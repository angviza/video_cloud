package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>轮询预案 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_polling_plan")
public class PollingPlan implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 轮询预案名称 db_column: name 
     */
	private String name;

    /**
     * 状态（是否启用：1启用，0停止） db_column: status 
     */
	private Integer status;

    /**
     * 共享状态（0:私有，仅自己可见；1全局共享，所有人可见；2部门共享，本部门用户可见；3指定部门共享，对指定的部门用户可见） db_column: share_status 
     */
    @Column(name = "share_status")
	private Integer shareStatus;

    /**
     * bgnTime db_column: bgn_time 
     */
    @Column(name = "bgn_time")
	private java.util.Date bgnTime;

    /**
     * endTime db_column: end_time 
     */
    @Column(name = "end_time")
	private java.util.Date endTime;

    /**
     * description db_column: description 
     */
	private String description;

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
	private String createUser;

    /**
     * 修改人 db_column: update_user 
     */
    @Column(name = "update_user")
	private String updateUser;


}

