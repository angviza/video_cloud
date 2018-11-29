package com.hdvon.client.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 摄像机权限实体
 * @author Administrator
 *
 */
@Data
@Table(name = "t_Permission")
public class Permission {
	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 操作权限名称 db_column: name 
     */
	private String name;

    /**
     * 权限值，按2进制进行计算，1、2、4、8、16.... db_column: value 
     */
	private Integer value;

    /**
     * 创建时间 db_column: create_time 
     */
	private java.util.Date createTime;

    /**
     * 修改时间 db_column: update_time 
     */
	private java.util.Date updateTime;

    /**
     * 创建人 db_column: create_user 
     */
	private String createUser;

    /**
     * 修改人 db_column: update_user 
     */
	private String updateUser;
}
