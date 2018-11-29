package com.hdvon.sip.video.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class RecordCtrlOptionSipVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
//	public int cmdType = 3;
//	public double scale = 0.25;
//	public int range = 100;
	
	/**
	 * 回放控制：播放1    暂停2   快进/慢进 3    随机拖放4
	 */
	private int cmdType;
	
	/**
	 * 播放速度，基本取值：0.25、0.5、1、2、4
	 */
	private double scale;
	
	/**
	 * 播放录像起点的相对值，取值范围为0到播放录像的终点时间，参数以s为单位，不能为负值。
	 * 如Range 头的值为0，则表示从起点开始播放，Range头的值为100，则表示从录像起点后的100s处开始播放，
	 * Range 头的取值为now表示从当前位置开始播放
	 * 
	 */
	private int range;
}