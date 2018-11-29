package com.hdvon.nmp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DevicecodeOptionVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>设备编码生成器选项 服务类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IDevicecodeOptionService{

	void saveDeviceCode(List<BigDecimal> list, DevicecodeOptionVo param,UserVo userVo,String isUser);

	PageInfo<DevicecodeOptionVo> getOptionPage(PageParam pageParam, Map<String, String> param);

	DevicecodeOptionVo getInfo(String devieCodeId);

}
