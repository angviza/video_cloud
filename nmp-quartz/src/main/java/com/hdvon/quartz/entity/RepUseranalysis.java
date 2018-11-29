package com.hdvon.quartz.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户行为统计 实体类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_rep_useranalysis")
public class RepUseranalysis implements Serializable{

	private static final long serialVersionUID = 1L;

	 /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 用户账号 db_column: account 
     */
	private java.lang.String account;

    /**
     * 用户在线时长 db_column: online_total 
     */
    @Column(name = "online_total")
	private java.lang.Integer onlineTotal;

    /**
     * 用户登录次数 db_column: loigin_total 
     */
    @Column(name = "loigin_total")
	private java.lang.Integer loiginTotal;

    /**
     * 实时监控次数 db_column: invite_total 
     */
    @Column(name = "invite_total")
	private java.lang.Integer inviteTotal;

    /**
     * 线索翻查次数 db_column: replay_total 
     */
    @Column(name = "replay_total")
	private java.lang.Integer replayTotal;

    /**
     * 录像下载次数 db_column: download_total 
     */
    @Column(name = "download_total")
	private java.lang.Integer downloadTotal;

    /**
     * controlTotal db_column: control_total 
     */
    @Column(name = "control_total")
	private java.lang.Integer controlTotal;

    /**
     * 预留 db_column: other_total 
     */
    @Column(name = "other_total")
	private java.lang.String otherTotal;

    /**
     * 统计最小时间 db_column: start_time 
     */
    @Column(name = "start_time")
	private java.util.Date startTime;

    /**
     * 用户行为统计时间 db_column: creat_time 
     */
    @Column(name = "creat_time")
	private java.util.Date creatTime;


}

