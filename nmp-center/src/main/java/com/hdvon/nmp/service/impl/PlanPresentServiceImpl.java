package com.hdvon.nmp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.PlanPresent;
import com.hdvon.nmp.mapper.PlanPresentMapper;
import com.hdvon.nmp.service.IPlanPresentService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.PlanPresentVo;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>预案关联预置位表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
@Slf4j
public class PlanPresentServiceImpl implements IPlanPresentService {

	@Autowired
	private PlanPresentMapper planPresentMapper;

	@Override
	public void savePlanPresent(String type,PlanPresentVo planPresentVo) {
		PlanPresent planPresent = Convert.convert(PlanPresent.class, planPresentVo);
		Example exa = new Example(PlanPresent.class);
		exa.createCriteria().andEqualTo("planId", planPresentVo.getPlanId()).andEqualTo("presentId",planPresentVo.getPresentId());
		int count = planPresentMapper.selectCountByExample(exa);
		if(count == 0) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("planId", planPresentVo.getPlanId());
			map.put("cameraId", planPresentVo.getCameraId());
			List<PlanPresentVo> list = null; 
			if("wall".equals(type)) {
				list = planPresentMapper.selectWallPlanCameraPresent(map);//查询未修改前预案下某个摄像机的预置位
			}else if("polling".equals(type)){
				list = planPresentMapper.selectPollingPlanCameraPresent(map);
			}
			if(list.size()>0) {
				//删除修改前关联到预案的预置位
				PlanPresentVo oldPlanPresent = list.get(0);
				planPresentMapper.deleteByPrimaryKey(oldPlanPresent.getId());
				
				//添加修改后关联到预案的预置位
				planPresent.setId(IdGenerator.nextId());
				planPresentMapper.insertSelective(planPresent);//添加新选择的预置位
			}
		}else {
			log.info("未修改预置位！");
		}
	}

}
