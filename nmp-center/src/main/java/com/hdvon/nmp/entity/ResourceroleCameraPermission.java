package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>资源角色与摄像头权限关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_resourcerole_camera_permission")
public class ResourceroleCameraPermission implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * resouceroleId db_column: resoucerole_id 
     */
    @Column(name = "resoucerole_id")
	private String resouceroleId;

    /**
     * 摄像机id db_column: camera_id
     */
    @Column(name = "camera_id")
	private String cameraId;

    /**
     * 摄像机权限值[1,2,4,8] db_column: permission_value 
     */
    @Column(name = "permission_value")
	private String permissionValue;


}

