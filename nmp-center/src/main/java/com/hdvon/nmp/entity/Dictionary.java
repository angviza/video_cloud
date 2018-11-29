package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>数据字典表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_dictionary")
public class Dictionary implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联字典类型表id db_column: dictionary_type_id 
     */
    @Column(name = "dictionary_type_id")
	private String dictionaryTypeId;

    /**
     * 字典中文名 db_column: ch_name 
     */
    @Column(name = "ch_name")
	private String chName;

    /**
     * 字典英文名 db_column: en_name 
     */
    @Column(name = "en_name")
	private String enName;

    /**
     * 字段值 db_column: value 
     */
	private String value;

    /**
     * 是否有效标志：1表示有效，0表示无效 db_column: enable 
     */
	private Integer enable;

    /**
     * 备注 db_column: remark 
     */
	private String remark;

    /**
     * 排序 db_column: orderby 
     */
	private Integer orderby;

    /**
     * createUser db_column: create_user 
     */
    @Column(name = "create_user")
	private String createUser;

    /**
     * createTime db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * updateUser db_column: update_user 
     */
    @Column(name = "update_user")
	private String updateUser;

    /**
     * updateTime db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;


}

