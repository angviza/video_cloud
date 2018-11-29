package com.hdvon.nmp.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;
@Data
public class CheckAttributeVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8525133525171060064L;
	private String code;
	private String name;
	private String attr;
	private boolean isMapping;
	private JSONObject valid;
}
