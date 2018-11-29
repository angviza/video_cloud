package com.hdvon.nmp.generate.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.util.OrderedProperties;
import com.hdvon.nmp.util.OrderedPropertiesUtils;
import com.hdvon.nmp.vo.CheckAttributeVo;

public class PropertyUtil {

	/**
	 * 获取配置属性信息
	 * @param propertyType
	 * @return
	 */
	public static List<CheckAttributeVo> getCheckAttributes(String propertyType){
		List<CheckAttributeVo> checkAttrs = new ArrayList<CheckAttributeVo>();
		OrderedProperties preperties = OrderedPropertiesUtils.getProperties(propertyType);
		Set<String> en = preperties.stringPropertyNames();
		Iterator<String> it = en.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = preperties.getProperty(key);
			JSONObject json = (JSONObject) JSONObject.parse(value);
			String name = json.getString("name");
			String attr = json.getString("attr");
			boolean isMapping = json.getBoolean("isMapping") == null ? false : json.getBoolean("isMapping");
			String valid = json.getString("valid");
			JSONObject validObj = (JSONObject) JSONObject.parse(valid);
			
			CheckAttributeVo checkVo = new CheckAttributeVo();
			checkVo.setCode(key);
			checkVo.setAttr(attr);
			checkVo.setName(name);
			checkVo.setMapping(isMapping);
			checkVo.setValid(validObj);
			checkAttrs.add(checkVo);
		}
		return checkAttrs;
	}
	
	/**
	 * @param propertyType
	 * @return
	 */
	public static OrderedProperties getProperties(String propertyType) {
		OrderedProperties properties = OrderedPropertiesUtils.getProperties(propertyType);
		return properties;
	}
}
