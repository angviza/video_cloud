package com.hdvon.client.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CameraMapVo implements Serializable{
	private String account;
	
	private List<Long> userList;
	
	private List<Long> deviceList;
	
	private String groupId;
	/**
	 * 权限预案id
	 */
	private String planId;
	/**
	 * 资源角色id
	 */
	private String resRoleId;
	
	private List<String> devices;// 设备id
	
	private String noEqRoleId;// 不等于
}
