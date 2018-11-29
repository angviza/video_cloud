package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>上传文件表 实体类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_attach_file")
public class AttachFile implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 功能类型（例如：公告模块可以设置为notice） db_column: type 
     */
	private java.lang.Integer type;

    /**
     * 文件名称 db_column: file_name 
     */
    @Column(name = "file_name")
	private java.lang.String fileName;

    /**
     * 附件路径 db_column: file_path 
     */
    @Column(name = "file_path")
	private java.lang.String filePath;

    /**
     * 创建时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 创建人 db_column: create_user 
     */
    @Column(name = "create_user")
	private java.lang.String createUser;

    /**
     * updateTime db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * updateUser db_column: update_user 
     */
    @Column(name = "update_user")
	private java.lang.String updateUser;


}

