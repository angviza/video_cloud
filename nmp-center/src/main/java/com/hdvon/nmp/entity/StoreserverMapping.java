package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>存储服务器关联项目中间表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_storeserver_mapping")
public class StoreserverMapping implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联项目id              db_column: project_id 
     */
    @Column(name = "project_id")
	private java.lang.String projectId;

    /**
     * 关联存储服务器id db_column: storeserver_id 
     */
    @Column(name = "storeserver_id")
	private java.lang.String storeserverId;

    /**
     * 关联项目名称 db_column: project_name 
     */
    @Column(name = "project_name")
	private java.lang.String projectName;

    /**
     * projectCode db_column: project_code 
     */
    @Column(name = "project_code")
	private java.lang.String projectCode;


}

