package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>公告类型表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_notice_type")
public class NoticeType implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键 db_column: id 
     */
    @Id
	private String id;

    /**
     * 公告类型名 db_column: name 
     */
	private String name;

    /**
     * 公告图标 db_column: icon 
     */
	private String icon;

    /**
     * 描述 db_column: description 
     */
	private String description;


}

