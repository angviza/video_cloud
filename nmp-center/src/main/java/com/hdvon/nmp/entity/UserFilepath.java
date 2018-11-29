package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>个人信息设置表 实体类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_filepath")
public class UserFilepath implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 用户id db_column: user_id 
     */
    @Column(name = "user_id")
	private java.lang.String userId;

    /**
     * 客户端mac地址 db_column: mac_id 
     */
    @Column(name = "mac_id")
	private java.lang.String macId;

    /**
     * 文件储存路径路径 db_column: file_path 
     */
    @Column(name = "file_path")
	private java.lang.String filePath;

    /**
     * 图片存储路径 db_column: img_path 
     */
    @Column(name = "img_path")
	private java.lang.String imgPath;

    /**
     * 视频协议方式，(TCP,UDP)默认为TCP db_column: protocol 
     */
	private java.lang.String protocol;

    /**
     * 创建时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 修改时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * 创建人 db_column: create_user 
     */
    @Column(name = "create_user")
	private java.lang.String createUser;

    /**
     * 修改人 db_column: update_user 
     */
    @Column(name = "update_user")
	private java.lang.String updateUser;


}

