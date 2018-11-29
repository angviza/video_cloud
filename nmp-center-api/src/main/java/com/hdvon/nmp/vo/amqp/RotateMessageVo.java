package com.hdvon.nmp.vo.amqp;

import java.io.Serializable;

import com.hdvon.nmp.common.WebConstant;

import lombok.Data;

/**
 * 请求消息前，需先更新表 t_wall_rotate、t_wall_camera、t_wall_channel中的数据。
 * @see WebConstant#BATCH_QUEUE_ROTATE
 * 功能：上墙轮巡。
 * 作者：zhanqf
 * 日期：2018年6月7日
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 *
 */
@Data
public class RotateMessageVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * t_wall_rotate表 id
	 */
	private String id;
	
	/**
	 * 操作。
	 * @see EWallOperation
	 */
	private String operate;
	
}
