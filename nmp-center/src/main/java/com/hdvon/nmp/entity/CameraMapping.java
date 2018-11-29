package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>摄像机关联中间表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_camera_mapping")
public class CameraMapping implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联摄像机表的id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private String cameraId;

    /**
     * 关联地址表的id db_column: address_id 
     */
    @Column(name = "address_id")
	private String addressId;

    /**
     * 关联编码器表的id db_column: encoder_server_id 
     */
    @Column(name = "encoder_server_id")
	private String encoderServerId;

    /**
     * 项目id db_column: project_id 
     */
    @Column(name = "project_id")
	private String projectId;

    /**
     * 组织机构id db_column: org_id 
     */
    @Column(name = "org_id")
	private String orgId;


}

