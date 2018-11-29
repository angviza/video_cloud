package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;

/**
 * 回看控制请求参数
 * @author Administrator
 *
 */
@Data
public class PlaybackControlParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备编码
	 */
	private String deviceID;
	/**
	 * 会话id
	 */
	private String callId;
	/**
	 * 回看控制类型
	 */
	private String type;
	/**
	 * 播放速度
	 * 基本取值：1表示0.25倍、2表示0.5倍、3表示1倍、4表示2倍、5表示4倍
	 */
	private String scale;
	/**
	 * 播放录像起点的相对值，取值范围为0到播放录像的终点时间，参数以s为单位，不能为负值。
	 * 如Range 头的值为0，则表示从起点开始播放，Range头的值为100，
	 * 则表示从录像起点后的100s处开始播放，Range 头的取值为now表示从当前位置开始播放。
	 */
	private Integer range;
}
