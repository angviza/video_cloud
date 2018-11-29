package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户头像信息表 实体类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_picture")
public class UserPicture implements Serializable{

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
     * 图片保存地址 db_column: img_path 
     */
    @Column(name = "img_path")
	private java.lang.String imgPath;

    /**
     * 图片名称 db_column: img_name 
     */
    @Column(name = "img_name")
	private java.lang.String imgName;

    /**
     * 图片特征值 db_column: features 
     */
	private java.lang.String features;

    /**
     * 上传图片时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 修改头像时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;


}

