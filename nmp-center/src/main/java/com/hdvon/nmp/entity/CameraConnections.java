package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b> 实体类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_camera_connections")
public class CameraConnections implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.Long id;

    /**
     * deviceCode db_column: device_code 
     */
    @Column(name = "device_code")
	private java.lang.String deviceCode;

    /**
     * monitorConnections db_column: monitor_connections 
     */
    @Column(name = "monitor_connections")
	private java.lang.Long monitorConnections;

    /**
     * replayConnections db_column: replay_connections 
     */
    @Column(name = "replay_connections")
	private java.lang.Long replayConnections;

    /**
     * downloadConnections db_column: download_connections 
     */
    @Column(name = "download_connections")
	private java.lang.Long downloadConnections;


}

