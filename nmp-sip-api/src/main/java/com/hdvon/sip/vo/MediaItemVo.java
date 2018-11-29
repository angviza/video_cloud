package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 录像查询结果
 * @author wanshaojian
 *
 */
@Data
public class MediaItemVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 设备编码
     */
	String deviceCode;
	
	/**
	 * 接受流地址ip
	 */
	String receiveIp;
	
	/**
	 * 接受端口
	 */
	int receivePort;
	/**
     * 名称
     */
	String name;
	
	/**
     * 查询起始时间
     */
	Date startDate;
	
	/**
     * 查询结束时间
     */
	Date endDate;
	
	/**
     * 视音频文件的URI
     */
	String uri;
	
    /**
     * 类型
     */
	String type;
    
    /**
     * 播放时长
     */
    Long playTime;
    
    /**
     * 事物id
     */
	String callId;
    
    /**
     * 返回状态
     */
    Integer status;
}
