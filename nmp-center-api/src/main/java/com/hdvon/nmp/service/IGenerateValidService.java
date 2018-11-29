package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.ValidAttrVo;

public interface IGenerateValidService {
	
	Map<String, List<String>> checkTreeNodeExists(Map<String,List<String>> treeData, String treeType, ValidAttrVo treeAttr, Integer cursRow, String isVirtual);
	
	Map<String, List<String>> checkCameraAttrExists(List<Map<String,String>> cameraData, ValidAttrVo treeAttr, Integer cursRow);
}
