package com.hdvon.sip.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 云台控制vo
 * @author wanshaojian
 *
 */
@Data
public class CloudControlQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		
	
	@ApiModelProperty(value = "设备Code")
	private String deviceCode;
	
	/**
	 * 云台控制类型
	 */
	@ApiModelProperty(value = "云台控制类型")
	private CloudControlTypeEnum typeEnum;
	/**
	 * 控制方向
	 */
	@ApiModelProperty(value = "控制方向")
	private DirectionEnum direction;
	
	/**
	 * 控制镜头变倍
	 */
	@ApiModelProperty(value = "控制镜头变倍")
	private ZoomEnum zoom;
	
	/**
	 * 控制光圈缩小放大
	 */
	@ApiModelProperty(value = "控制光圈缩小放大")
	private IrisEnum iris;
	
	
	/**
	 * 控制焦距近和远
	 */
	@ApiModelProperty(value = "控制焦距近和远")
	private FocusEnum focus;
	
	
	/**
	 * 步长
	 * 	1:类型为     方向速度
	 *  2:类型为    镜头变倍速度  
	 *  3:类型为    镜头变倍速度 
	 *  4:类型为    镜头变倍速度  
	 */
	@ApiModelProperty(value = "步长")
	private Integer stepSize;
	
	/**
	 * 控制方向枚举
	 * @author wanshaojian
	 *
	 */
	public enum CloudControlTypeEnum {
		/**
		 * DIRECTION  控制方向
		 * ZOOM  控制镜头变倍
		 * IRIS 控制光圈
		 * FOCUS 控制焦距
		 */
		DIRECTION("DIRECTION"),ZOOM("ZOOM"),IRIS("IRIS"),FOCUS("FOCUS");
		
		private CloudControlTypeEnum(String value) {
			this.value = value;
		}
		private String value;
		
		public String getValue() {
			return value;
		}
		
		
	}
	/**
	 * 控制方向枚举
	 * @author wanshaojian
	 *
	 */
	public enum DirectionEnum {
		/**
		 * 上：1   下：2   左：3   右：4   左上：5  左下：6  右上：7  右下：8
		 */
		STOP("stop","00"),UP("up","08"),DOWN("down","04"),LEFT("left","02"),RIGHT("right","01"),UPPER_LEFT("upper_left","0A"),LOWER_LEFT("lower_left","09"),UPPER_RIGHT("upper_right","06"),LOWER_RIGHT("lower_right","05");
		
		private DirectionEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		private String key;  
		private String value;
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
		
		
	}
	
	/**
	 * 控制镜头变倍枚举
	 * @author wanshaojian
	 *
	 */
	public enum ZoomEnum {
		/**
		 * 放大：1   缩小：2
		 * 【对应变焦bit4指令编码】
		 */
		AMPLIFY("amplify","10"),NARROW("narrow","20"),STOP("stop","00");
		
		private ZoomEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		private String key;
		private String value;
		
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
		
		
	}
	
	/**
	 * 控制光圈枚举
	 * 
	 * @author wanshaojian
	 *
	 */
	public enum IrisEnum {
		/**
		 * 放大：1   缩小：2  3：停止
		 * 【对应光圈bit4指令编码】
		 */
		AMPLIFY("amplify","48"),NARROW("narrow","44"),STOP("stop","40");
		
		private IrisEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		private String key;
		private String value;
		
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
	}
	/**
	 * 控制焦距枚举
	 * @author wanshaojian
	 *
	 */
	public enum FocusEnum {
		/**
		 * 近：1  远：2 
		 * 	【对应调焦bit4指令编码】
		 */
		NEAR("near","41"),FAR("far","42"),STOP("stop","40");
		
		private FocusEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		private String key;
		private String value;
		
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
	}
	
}
