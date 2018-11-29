package com.hdvon.nmp.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.mapper.RepUseranalysisMapper;
import com.hdvon.nmp.service.IRepUseranalysisService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.RepUseranalysisVo;

/**
 * <br>
 * <b>功能：</b>用户操作行为统计表Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class RepUseranalysisServiceImpl implements IRepUseranalysisService {

	@Autowired
	private RepUseranalysisMapper repUseranalysisMapper;

	@Override
	public PageInfo<RepUseranalysisVo> getUseranalysisByadmin(Map<String, Object> param, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		List<RepUseranalysisVo> list =repUseranalysisMapper.selectUseranalysisByadmin(param);
	    return new PageInfo<>(list);
	}

	@Override
	public List<Map<String, Object>> exportUserAnalysis(Map<String, Object> param) {
		List<Map<String, Object>> listMap=new ArrayList<Map<String, Object>>();
		List<RepUseranalysisVo> list =repUseranalysisMapper.selectUseranalysisByadmin(param);
		for(RepUseranalysisVo vo :list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("account", vo.getAccount());
			map.put("departmentName", vo.getDepartmentName());
			map.put("onlineTotal", vo.getOnlineTotal());
			int inviteTotal=vo.getInviteTotal() !=null ?vo.getInviteTotal():0;
			int replayTotal=vo.getReplayTotal() !=null ?vo.getReplayTotal():0;
			int downloadTotal=vo.getDownloadTotal() !=null ?vo.getDownloadTotal():0;
			map.put("loiginTotal", vo.getLoiginTotal());
			map.put("inviteTotal", vo.getInviteTotal());
			map.put("replayTotal", vo.getReplayTotal());
			map.put("downloadTotal", vo.getDownloadTotal());
			map.put("controlTotal", vo.getControlTotal());
			map.put("total", inviteTotal+replayTotal+downloadTotal);
			listMap.add(map);
			
		}
		return listMap;
	}

}
