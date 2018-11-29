package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import lombok.Data;

@Data
public class MediaDirectoryVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 接受端ip
	 */
	private String receiveIp;

	/**
	 * 接受端口
	 */
	private Integer receivePort;
	/**
	 * 设备编码
	 */
	private String deviceCode;
	
	/**
	 * 视频开始时间
	 */
	private Date startTime;
	
	/**
	 * 视频结束时间
	 */
	private Date endTime;
	
	/**
     * 视频段数
     */
	private Integer itemSize;
	
    /**
     * 播放总时长
     */
	private Long sumPlayTime;
	

    /**
     * 返回状态
     */
	private Integer status;
    
    /**
     * 对应请求编码
     */
	private Long sn;
	
    /**
     * 查询返回结果
     */
	private List<MediaItemVo> dataList;
	
    /**
     * 当前播放的媒体对象
     */
	private MediaItemVo currItem;
    
    /**
     * 当前播放的下一媒体对象
     */
	private MediaItemVo nextItem;
    
    /**
     * 会话id
     */
	private String callId;
    
    /**
     * 翻查对象的主键id，使用雪花生成
     */
	private String pkId;
    


}
