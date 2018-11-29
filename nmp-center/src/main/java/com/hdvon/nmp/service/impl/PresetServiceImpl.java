package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.common.CruiseBean;
import com.hdvon.nmp.common.PresentBean;
import com.hdvon.nmp.common.PresentListBean;
import com.hdvon.nmp.entity.BallPlan;
import com.hdvon.nmp.entity.PlanPresent;
import com.hdvon.nmp.entity.PresentPosition;
import com.hdvon.nmp.entity.User;
import com.hdvon.nmp.mapper.BallPlanMapper;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.mapper.PlanPresentMapper;
import com.hdvon.nmp.mapper.PresentPositionMapper;
import com.hdvon.nmp.mapper.UserMapper;
import com.hdvon.nmp.service.IPresetService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraVo;

import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>预置位和巡航预案接口实现<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-11-13<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PresetServiceImpl implements IPresetService{

	@Resource
	PresentPositionMapper presentMapper;
	
	@Resource
	PlanPresentMapper planPresentMapper;
	
	@Resource
	CameraMapper cameraMapper;
	
	@Resource
	BallPlanMapper ballPlanMapper;
	
	@Resource
	UserMapper userMapper;
	/**
	 * 新增预置位
	 * @param vo
	 */
	@Override
	public void savePreset(PresentBean vo) {
		// TODO Auto-generated method stub
		//根据设备编码查询查询设备信息
		List<CameraVo> cameraList = cameraMapper.findCameraByDeviceCode(vo.getDeviceID());
		if(cameraList == null || cameraList.isEmpty()) {
			return;
		}
		CameraVo camera = cameraList.get(0);
		PresentPosition bean = new PresentPosition();
		bean.setPresentNo(String.valueOf(vo.getPresetNum()));
		bean.setName(vo.getPresetName());
		bean.setCameraId(camera.getId());
		bean.setCameraCode(vo.getDeviceID());
		
		String id = IdGenerator.nextId();

		bean.setCreateTime(new Date());
		String userAccount = null;
		if(!StringUtils.isEmpty(vo.getUserId())) {
			//根据用户id获取对应用户名称
			User user = userMapper.selectByPrimaryKey(vo.getUserId());
			userAccount = user.getAccount();
		}
		
		Example presentExample = new Example(PresentPosition.class);
		presentExample.createCriteria().andEqualTo("cameraCode",vo.getDeviceID()).andEqualTo("presentNo",String.valueOf(vo.getPresetNum()));
		
		List<PresentPosition> recordList = presentMapper.selectByExample(presentExample);
		if(recordList == null || recordList.isEmpty()) {
			bean.setId(id);
			bean.setCreateUser(userAccount);
			presentMapper.insertSelective(bean);
		}else {
			PresentPosition record = recordList.get(0);
			record.setName(vo.getPresetName());
			record.setUpdateTime(new Date());
			record.setUpdateUser(userAccount);
			presentMapper.updateByPrimaryKeySelective(record);
		}
		
		
		
	}
	/**
	 * 删除预置位
	 * @param vo
	 */
	@Override
	public void deletePreset(PresentBean vo) {
		// TODO Auto-generated method stub
		Example example = new Example(PresentPosition.class);
		example.createCriteria().andEqualTo("cameraCode",vo.getDeviceID()).andEqualTo("presentNo",String.valueOf(vo.getPresetNum()));
		
		presentMapper.deleteByExample(example);
	}
    /**
     * 批量更新预置位信息
     * @param vo
     */
    public void batchUpdatePresent(PresentListBean vo) {
    	String cameraCode = vo.getDeviceID();
    	vo.getItemList().stream().forEach(t->{
    		t.setDeviceID(cameraCode);
    		savePreset(t);
    	});
    }
	@Override
	public void savePlanPresent(CruiseBean vo) {
		// TODO Auto-generated method stub
		//根据设备编码查询查询设备信息
		String presentId = findPresentIdBySub(vo);
		String planId = findPlanIdBySub(vo);
		if(StringUtils.isEmpty(presentId) || StringUtils.isEmpty(planId)) {
			return;
		}
		PlanPresent bean = new PlanPresent();
		bean.setPlanId(planId);
		bean.setPresentId(presentId);
		
		Example example = new Example(PlanPresent.class);
		example.createCriteria().andEqualTo("planId",planId).andEqualTo("presentId",presentId);
		
		List<PlanPresent> recordList = planPresentMapper.selectByExample(example);
		if(recordList == null || recordList.isEmpty()) {
			String id = IdGenerator.nextId();
			bean.setId(id);
			planPresentMapper.insertSelective(bean);
		}else {
			PlanPresent record = recordList.get(0);
			record.setTimeInterval(vo.getSpeed());
			planPresentMapper.updateByPrimaryKeySelective(record);
		}
		
		
	}
	/**
	 * 获取预置位id
	 * @param vo
	 * @return
	 */
	private String findPresentIdBySub(CruiseBean vo) {
		Example presentExample = new Example(PresentPosition.class);
		presentExample.createCriteria().andEqualTo("cameraCode",vo.getDeviceID()).andEqualTo("presentNo",String.valueOf(vo.getPresetNum()));
		List<PresentPosition> presentList = presentMapper.selectByExample(presentExample);
		if(presentExample == null || presentList.isEmpty()) {
			return null;
		}
		return presentList.get(0).getId();
	}
	
	
	private String findPlanIdBySub(CruiseBean vo) {
		List<CameraVo> cameraList = cameraMapper.findCameraByDeviceCode(vo.getDeviceID());
		if(cameraList == null || cameraList.isEmpty()) {
			return null;
		}
		String cameraId = cameraList.get(0).getId();
		Example planExample = new Example(BallPlan.class);
		planExample.createCriteria().andEqualTo("cameraId",cameraId);
		List<BallPlan> planList = ballPlanMapper.selectByExample(planExample);
		if(planList == null || planList.isEmpty()) {
			return null;
		}
		String planId = planList.get(0).getId();
		return planId;
	}
	@Override
	public void updatePlanPresent(CruiseBean vo) {
		// TODO Auto-generated method stub
		//根据设备编码查询查询设备信息
		String presentId = findPresentIdBySub(vo);
		String planId = findPlanIdBySub(vo);
		if(StringUtils.isEmpty(presentId) || StringUtils.isEmpty(planId)) {
			return;
		}
		Example example = new Example(PlanPresent.class);
		example.createCriteria().andEqualTo("planId", planId).andEqualTo("presentId",presentId);
		
		List<PlanPresent> list = planPresentMapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return;
		}
		
		PlanPresent bean = list.get(0);
		bean.setTimeInterval(vo.getStayTime().intValue());
		
		planPresentMapper.updateByPrimaryKeySelective(bean);
	}

	@Override
	public void deletePlanPresent(CruiseBean vo) {
		// TODO Auto-generated method stub
		
		String planId = findPlanIdBySub(vo);
		Example example = new Example(PlanPresent.class);
		Example.Criteria criteria = example.createCriteria().andEqualTo("planId", planId);
		if(vo.getPresetNum() != null) {
			String presentId = findPresentIdBySub(vo);
			criteria.andEqualTo("presentId",presentId);
		}
		planPresentMapper.deleteByExample(example);
	}

}
