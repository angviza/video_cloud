package com.hdvon.client.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 资源用户摄像机临时关系实体类
 * @author wanshaojian
 *
 */
@Data
@Table(name = "t_resource_user_camera_temp")
public class ResourceUserCameraTemp implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 类型1：资源角色授权 2：权限预案 3:自定义分组
     */
    @Column(name="type")
	private Integer type;

    /**
     * 关联id【资源角色id或权限预案id和自定义分组id】
     */
    @Column(name="relation_id")
	private String relationId;

    /**
     * 用户id
     */
    @Column(name="user_id")
	private String userId;


    /**
     * 摄像机id
     */
    @Column(name="device_id")
	private String deviceId;
	
    /**
     * 分组id
     */
    @Column(name="group_id")
   	private String groupId;
}

