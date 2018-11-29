package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>文件关联表 实体类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_file_relate")
public class FileRelate implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键 db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * t_attach_file文件表Id db_column: file_Id 
     */
    @Column(name = "file_Id")
	private java.lang.String fileId;

    /**
     * 来源关联文件的表ID db_column: relate_Id 
     */
    @Column(name = "relate_Id")
	private java.lang.String relateId;


}

