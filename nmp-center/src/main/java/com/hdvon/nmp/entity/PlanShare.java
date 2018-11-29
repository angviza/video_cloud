package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>预案共享设置表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_plan_share")
public class PlanShare implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联预案id（预案包括轮询预案、上墙预案、球机预案） db_column: plan_id 
     */
    @Column(name = "plan_id")
	private java.lang.String planId;

    /**
     * 关联部门id db_column: department_id 
     */
    @Column(name = "department_id")
	private java.lang.String departmentId;


}

