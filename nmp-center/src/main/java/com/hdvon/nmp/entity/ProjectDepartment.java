package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>项目与部门关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_project_department")
public class ProjectDepartment implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 项目id db_column: project_id 
     */
    @Column(name = "project_id")
	private String projectId;

    /**
     * 建设部门id db_column: constructor_dep_id 
     */
    @Column(name = "constructor_dep_id")
	private String constructorDepId;

    /**
     * 承建部门id db_column: builder_dep_id 
     */
    @Column(name = "builder_dep_id")
	private String builderDepId;


}

