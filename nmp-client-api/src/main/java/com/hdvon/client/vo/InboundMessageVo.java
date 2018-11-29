package com.hdvon.client.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class InboundMessageVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 消息ID
	 */
    private java.lang.String id;
    
    /**
     * 用户ID(多个用逗号分隔)
     */
    private java.lang.String userId;

    /**
     * 设备ID(多个用逗号分隔)
     */
    private java.lang.String deviceIds;

    /**
     * 更新ID(多个用逗号分隔)
     */
    private java.lang.String updateIds;

    /**
     * 删除ID(多个用逗号分隔)
     */
    private java.lang.String deleteIds;

    /**
     * 关联ID(多个用逗号分隔)
     */
    private java.lang.String relationIds;

    /**
     * 业务类型 1：资源角色关联用户 2：权限预案用户授权 3：摄像机分组 4：用户分配资源角色 5：更新设备 6：预置位 7：预置位查询 8：球机巡航预案
     */
    private java.lang.Integer type;
    
    /**
     * 操作类型
     */
    private java.lang.String operateType;

    /**
     * 消息内容
     */
    private java.lang.String content;

    /**
     * 消息状态 0：未消费 1：已消费
     */
    private java.lang.Integer status;

    /**
     * 接受时间
     */
    private java.util.Date recieveTime;
    
    /**
     * 发送时间
     */
    private java.util.Date sendTime;

}

