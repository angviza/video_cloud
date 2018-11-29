package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户操作类型 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class UserOperationtype implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */	
	private java.lang.String id;
    /**
     * 操作类型名称 db_column: name 
     */	
	private java.lang.String name;
    /**
     * 备注 db_column: remark 
     */	
	private java.lang.String remark;
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
	private java.lang.String createUser;
    /**
     * 修改人 db_column: update_user 
     */	
	private java.lang.String updateUser;

}

