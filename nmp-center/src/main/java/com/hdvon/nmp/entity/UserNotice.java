package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户公告映射表 实体类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user_notice")
public class UserNotice implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 用户ID db_column: user_Id 
     */
    @Column(name = "user_Id")
	private java.lang.String userId;

    /**
     * 公告ID db_column: notice_Id 
     */
    @Column(name = "notice_Id")
	private java.lang.String noticeId;

    /**
     * 是否已读，1为已读，0为未读 db_column: flag 
     */
	private java.lang.Integer flag;


}

