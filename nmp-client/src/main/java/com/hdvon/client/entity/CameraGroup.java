package com.hdvon.client.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>摄像机分组表实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_cameragrouop")
public class CameraGroup implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 分组名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 分组父id db_column: pid 
     */
	private java.lang.String pid;

    /**
     * code db_column: code 
     */
	private java.lang.String code;

    /**
     * 描述 db_column: description 
     */
	private java.lang.String description;

    /**
     * 是否私有(1.是，0.否) db_column: is_private 
     */
    @Column(name = "is_private")
	private java.lang.Integer isPrivate;

    /**
     * 分组创建人 db_column: creator_id 
     */
    @Column(name = "creator_id")
	private java.lang.String creatorId;

    /**
     * 同级排序 db_column: orderby 
     */
	private java.lang.Integer orderby;

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

