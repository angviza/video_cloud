package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 3D拖放请求对象
 * @author wanshaojian
 *
 */
@Data
public class DragZoomParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备编号
	 */
	private String deviceID;
	/**
	 * 类型 amplify 放大 narrow 缩小
	 */
	private String type;
	/**
	 * 播放窗口长度像素值
	 */
	private Integer length;
	
	/**
	 * 播放窗口宽度像素值
	 */
	private Integer width;
	
	/**
	 * 拉框中心的横轴坐标像素值
	 */
	private Float midPointX;
	
	/**
	 * 拉框中心的纵轴坐标像素值
	 */
	private Float midPointY;
	
	/**
	 * 拉框长度像素值
	 */
	private Integer lengthX;
	
	/**
	 * 拉框宽度像素值
	 */
	private Integer lengthY;
	
	
	public enum DragZoomType{
		DRAGZOOMIN("amplify"),DRAGZOOMOUT("narrow");
		private String key;
		private DragZoomType(String key) {
			this.key = key;
		}
		public String getKey() {
			return key;
		}
		
	}
}
