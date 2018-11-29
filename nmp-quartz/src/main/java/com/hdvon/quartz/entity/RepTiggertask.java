package com.hdvon.quartz.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>定时任务配置表 实体类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_rep_tiggertask")
public class RepTiggertask implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键 db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 定时任务名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 定时任务执行规则 db_column: rules 
     */
	private java.lang.String rules;

    /**
     * 定时任务执行类型 1 统计用户报表 2 统计摄像机报表.... db_column: type 
     */
	private java.lang.String type;

    /**
     * 描述信息 db_column: description 
     */
	private java.lang.String description;

    /**
     * creatTime db_column: creat_time 
     */
    @Column(name = "creat_time")
	private java.util.Date creatTime;

    /**
     * 上一次定时器执行时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;


}

