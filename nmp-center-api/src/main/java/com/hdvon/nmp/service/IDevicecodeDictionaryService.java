package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.DevicecodeDictionaryVo;

/**
 * <br>
 * <b>功能：</b>设备编码生成字典表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IDevicecodeDictionaryService{
	
	/**
	 * 批量添加设备编码字典
	 * @param list
	 * @param titles
	 */
	void batchInsertDevicecodeDicts(List<Map<String,Object>> list, String[] titles);
	
	/**
	 * @param position
	 * @param pCode
	 * @return
	 */
	List<DevicecodeDictionaryVo> getDevicecodeDictByPosition(String position, String pCode);

	/**
	 * 获取父子节点下的子节点
	 * @param vo请求对象
	 * @return
	 */
	List<DevicecodeDictionaryVo> getChildrens(DevicecodeDictionaryVo vo);

	List<DevicecodeDictionaryVo> getList();

	List<DevicecodeDictionaryVo> getAddrList(List<String> list);


}
