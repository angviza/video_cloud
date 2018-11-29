package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.BallPlan;
import com.hdvon.nmp.entity.PlanShare;
import com.hdvon.nmp.entity.PollingPlan;
import com.hdvon.nmp.entity.WallPlan;
import com.hdvon.nmp.mapper.BallPlanMapper;
import com.hdvon.nmp.mapper.PlanShareMapper;
import com.hdvon.nmp.mapper.PollingPlanMapper;
import com.hdvon.nmp.mapper.WallPlanMapper;
import com.hdvon.nmp.service.IPlanShareService;
import com.hdvon.nmp.util.snowflake.IdGenerator;

import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>预案共享设置表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PlanShareServiceImpl implements IPlanShareService {

	@Autowired
	private PlanShareMapper planShareMapper;
	
	@Autowired
	private WallPlanMapper wallPlanMapper;
	
	@Autowired
	private PollingPlanMapper pollingPlanMapper;
	
	@Autowired
	private BallPlanMapper ballPlanMapper;
	
	@Override
	public void savePlanShares(String planId, Integer shareStatus, List<String> departmentIdList, String flag) {
		if("wallplan".equals(flag)) {
			WallPlan wallplan = new WallPlan();
			wallplan.setId(planId);
			wallplan.setShareStatus(shareStatus);
			wallPlanMapper.updateByPrimaryKeySelective(wallplan);
		}else if("pollingplan".equals(flag)) {
			PollingPlan pollingplan = new PollingPlan();
			pollingplan.setId(planId);
			pollingplan.setShareStatus(shareStatus);
			pollingPlanMapper.updateByPrimaryKeySelective(pollingplan);
		}else if("ballplan".equals(flag)) {
			BallPlan ballplan = new BallPlan();
			ballplan.setId(planId);
			ballplan.setShareStatus(shareStatus);
			ballPlanMapper.updateByPrimaryKeySelective(ballplan);
		}
		
		Example e = new Example(PlanShare.class);
		e.createCriteria().andEqualTo("planId", planId);
		planShareMapper.deleteByExample(e);//删除未修改前的共享设置
		
		List<PlanShare> list = new ArrayList<PlanShare>();
		for(String deptId : departmentIdList) {
			PlanShare ws = new PlanShare();
			ws.setId(IdGenerator.nextId());
			ws.setPlanId(planId);
			ws.setDepartmentId(deptId);
			list.add(ws);
		}
		if(list.size()>0) {
			planShareMapper.insertList(list);//保存修改后的共享设置
		}
	}

}
