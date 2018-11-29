package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>字典类型表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_dictionary_type")
public class DictionaryType implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 字典类型中文名 db_column: ch_name 
     */
    @Column(name = "ch_name")
	private String chName;

    /**
     * 字典类型英文名 db_column: en_name 
     */
    @Column(name = "en_name")
	private String enName;

    /**
     * 字典类型说明 db_column: remark 
     */
	private String remark;

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

