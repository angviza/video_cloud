package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>设备播放记录（临时）表 实体类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_camera_log")
public class CameraLog implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键 db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 用户id db_column: user_id 
     */
    @Column(name = "user_id")
	private java.lang.String userId;

    /**
     * 设备编码 db_column: sbbm 
     */
	private java.lang.String sbbm;

    /**
     * 播放返回的id db_column: call_id 
     */
    @Column(name = "call_id")
	private java.lang.String callId;

    /**
     * 用户登录ip db_column: user_ip 
     */
    @Column(name = "user_ip")
	private java.lang.String userIp;

    /**
     * 播放方式 1 实时；2 录像，3 下载 db_column: play_type 
     */
    @Column(name = "play_type")
	private java.lang.String playType;

    /**
     * 状态： 1 在线  0 离线 db_column: state 
     */
	private java.lang.String state;

    /**
     * 播放时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 更新时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;


}

