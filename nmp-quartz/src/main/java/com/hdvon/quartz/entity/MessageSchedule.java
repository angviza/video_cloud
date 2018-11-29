package com.hdvon.quartz.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b> 实体类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_inbound_message")
public class MessageSchedule implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 消息ID db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 用户ID(多个用逗号分隔) db_column: user_id 
     */
    @Column(name = "user_id")
	private java.lang.String userId;

    /**
     * 设备ID(多个用逗号分隔) db_column: device_ids 
     */
    @Column(name = "device_ids")
	private java.lang.String deviceIds;

    /**
     * 更新ID(多个用逗号分隔) db_column: update_ids 
     */
    @Column(name = "update_ids")
	private java.lang.String updateIds;

    /**
     * 删除ID(多个用逗号分隔) db_column: delete_ids 
     */
    @Column(name = "delete_ids")
	private java.lang.String deleteIds;

    /**
     * 关联ID(多个用逗号分隔) db_column: relation_ids 
     */
    @Column(name = "relation_ids")
	private java.lang.String relationIds;

    /**
     * 业务类型 1：资源角色关联用户 2：权限预案用户授权 3：摄像机分组 4：用户分配资源角色 5：更新设备 6：预置位 7：预置位查询 8：球机巡航预案   db_column: type 
     */
	private java.lang.Integer type;

    /**
     * 操作类型 db_column: operate_type 
     */
    @Column(name = "operate_type")
	private java.lang.String operateType;

    /**
     * 消息内容 db_column: content 
     */
	private java.lang.String content;

    /**
     * 消息状态 0：未消费 1：已消费 db_column: status 
     */
	private java.lang.Integer status;

    /**
     * 接受时间 db_column: recieve_time 
     */
    @Column(name = "recieve_time")
	private java.util.Date recieveTime;

    /**
     * 发送时间 db_column: send_time 
     */
    @Column(name = "send_time")
	private java.util.Date sendTime;

}
