package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.RepUseranalysisVo;

/**
 * <br>
 * <b>功能：</b>用户操作行为统计表 服务类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IRepUseranalysisService{

	PageInfo<RepUseranalysisVo> getUseranalysisByadmin(Map<String, Object> param, PageParam pageParam);

	List<Map<String, Object>> exportUserAnalysis(Map<String, Object> param);

}
