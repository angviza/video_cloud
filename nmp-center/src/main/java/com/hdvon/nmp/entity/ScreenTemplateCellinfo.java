package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>分屏管理单元屏信息表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_screen_template_cellinfo")
public class ScreenTemplateCellinfo implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联模板表id db_column: template_id 
     */
    @Column(name = "template_id")
	private String templateId;

    /**
     * 行号 db_column: row_no 
     */
    @Column(name = "row_no")
	private Integer rowNo;

    /**
     * 合并行数 db_column: row_span 
     */
    @Column(name = "row_span")
	private Integer rowSpan;

    /**
     * 列号 db_column: col_no 
     */
    @Column(name = "col_no")
	private Integer colNo;

    /**
     * 合并列数 db_column: col_span 
     */
    @Column(name = "col_span")
	private Integer colSpan;

    /**
     * createTime db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * updateTime db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * createUser db_column: create_user 
     */
    @Column(name = "create_user")
	private String createUser;

    /**
     * updateUser db_column: update_user 
     */
    @Column(name = "update_user")
	private String updateUser;


}

