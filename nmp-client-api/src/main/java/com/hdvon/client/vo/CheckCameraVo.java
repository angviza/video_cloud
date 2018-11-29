package com.hdvon.client.vo;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户摄像机权限 VO类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class CheckCameraVo {
	private  String camearId;//摄像机id
	private String  deviceId;//设备id
	private Boolean isRights;//是否有权限
	
}
