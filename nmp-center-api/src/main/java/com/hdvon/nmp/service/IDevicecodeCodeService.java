package com.hdvon.nmp.service;

import java.util.List;

import com.hdvon.nmp.vo.DevicecodeOptionVo;

/**
 * <br>
 * <b>功能：</b>设备编码器生成编码 服务类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IDevicecodeCodeService{

	String getMaxCodeBycode(String baseCode);

	void deleteCode(List<String> deviceCodeList);

}
