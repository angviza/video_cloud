package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>预案关联预置位表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_plan_present")
public class PlanPresent implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联预案id db_column: plan_id 
     */
    @Column(name = "plan_id")
	private java.lang.String planId;

    /**
     * 关联预置位id db_column: present_id 
     */
    @Column(name = "present_id")
	private java.lang.String presentId;

    /**
     * 间隔时间（单位：秒） db_column: time_interval 
     */
    @Column(name = "time_interval")
	private java.lang.Integer timeInterval;

    /**
     * 预案中的预置位排序 db_column: sort 
     */
	private java.lang.Integer sort;


}

