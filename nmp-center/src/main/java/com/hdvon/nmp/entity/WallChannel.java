package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>上墙轮巡的矩阵通道表 实体类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_wall_channel")
public class WallChannel implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 上墙轮巡主表id db_column: rotate_id 
     */
    @Column(name = "rotate_id")
	private java.lang.String rotateId;

    /**
     * 矩阵通道id db_column: channel_id 
     */
    @Column(name = "channel_id")
	private java.lang.String channelId;

    /**
     * 顺序 db_column: queue 
     */
    @Column(name = "queue")
	private java.lang.Integer queue;

    /**
     * 当前播放的摄像机id db_column: camera_id 
     */
    @Column(name = "camera_id")
	private java.lang.String cameraId;

    /**
     * 调用jni接口返回的callid db_column: call_id 
     */
    @Column(name = "call_id")
	private java.lang.String callId;

}

