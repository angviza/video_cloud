package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.PlanPresent;
import com.hdvon.nmp.entity.PresentPosition;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.mapper.PlanPresentMapper;
import com.hdvon.nmp.mapper.PresentPositionMapper;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.IPresentPositionService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.PresetOptionVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * <br>
 * <b>功能：</b>预置位表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PresentPositionServiceImpl implements IPresentPositionService {

	@Autowired
	private PresentPositionMapper presentPositionMapper;
	
	@Autowired
	private CameraMapper cameraMapper;
	
	@Autowired
	private PlanPresentMapper planPresentMapper;
	
	@Autowired
	private ICameraService cameraService;
	
	@Override
	public void savePresentPosition(UserVo userVo, PresentPositionVo presentPositionVo) {
		PresentPosition presentPosition = Convert.convert(PresentPosition.class, presentPositionVo);
		
		if(StrUtil.isBlank(presentPositionVo.getId())) {//增加
			Example exaName = new Example(PresentPosition.class);
			// 不做校验，预置位编码重复是覆盖
			
//			exaName.createCriteria().andEqualTo("cameraId", presentPositionVo.getCameraId()).andEqualTo("name", presentPosition.getName());
//			int countName = presentPositionMapper.selectCountByExample(exaName);
//			if(countName > 0) {
//				throw new ServiceException("该摄像机已存在该预置位名称！");
//			}
//			
//			Example exaNo = new Example(PresentPosition.class);
//			exaNo.createCriteria().andEqualTo("cameraId", presentPositionVo.getCameraId()).andEqualTo("presentNo", presentPosition.getPresentNo());
//			int countNo = presentPositionMapper.selectCountByExample(exaNo);
//			if(countNo > 0) {
//				throw new ServiceException("该摄像机已存在该预置位编号！");
//			}
	        
			Integer maxSort = presentPositionMapper.selectMaxSort(presentPositionVo.getCameraId());

			presentPosition.setId(IdGenerator.nextId());
			presentPosition.setSort(maxSort == null? 1 : maxSort + 1);
			presentPosition.setCreateTime(new Date());
			presentPosition.setCreateUser(userVo.getAccount());
			presentPosition.setUpdateTime(new Date());
			presentPosition.setUpdateUser(userVo.getAccount());
			
			presentPositionMapper.insertSelective(presentPosition);
		}else {//修改(设置守望位)
			
		}
	}

	@Override
	public List<PresentPositionVo> getPresentPositionList(Map<String, Object> map) {
		/*Example exa = new Example(PresentPosition.class);
		Criteria cir = exa.createCriteria();
		if(map.get("cameraId") != null) {
			cir.andEqualTo("cameraId", map.get("cameraId"));
		}
		if(map.get("name") != null) {
			cir.andLike("name", "%"+(String) map.get("name")+"%");
		}
		List<PresentPosition> list = presentPositionMapper.selectByExample(exa);
		List<PresentPositionVo> result = BeanHelper.convertList(PresentPositionVo.class, list);
		*/
		//List<PresentPositionVo> result =presentPositionMapper.selectByParam(map);
		List<PresentPositionVo> result =presentPositionMapper.selectPresentPositionsByCamera(map);
		return result;
	}

	@Override
	public void delPresentPositionsByIds(List<String> ids) {
		Example exa = new Example(PresentPosition.class);
		exa.createCriteria().andIn("id", ids);
		presentPositionMapper.deleteByExample(exa);
		
		Example planExa = new Example(PlanPresent.class);
		planExa.createCriteria().andIn("presentId", ids);
		planPresentMapper.deleteByExample(planExa);
	}

	@Override
	public PresentPositionVo getPresentPositionById(String id) {
		PresentPosition presentPosition = presentPositionMapper.selectByPrimaryKey(id);
		PresentPositionVo presentPositionVo = Convert.convert(PresentPositionVo.class, presentPosition);
		return presentPositionVo;
	}

	@Override
	public List<PresentPositionVo> getPresentPositionsInPlan(String planId) {
	    Map<String,Object> map = new HashMap<>();
        map.put("planId", planId);
		List<PresentPositionVo> presentPositionVos = presentPositionMapper.selectPresentPositionsInPlan(map);
		return presentPositionVos;
	}

	@Override
	public List<PresentPositionVo> getPresentsInCameras(Map<String, Object> paramMap) {
		Example exa = new Example(PresentPosition.class);
		Criteria c = exa.createCriteria();
		if(paramMap.get("isWatch") != null) {
			c.andEqualTo("isKeepwatch", paramMap.get("isWatch"));
		}
		c.andIn("cameraId", paramMap.get("isWatch") != null ? (List<String>)paramMap.get("cameraIds"):new ArrayList<String>());
		return null;
	}

	@Override
	public List<PresentPositionVo> getAlarmPresentsInCamera(Map<String, Object> map) {
		List<PresentPositionVo> list = presentPositionMapper.selectAlarmPresentsInCamera(map);
		return list;
	}

	/***
	 * 通过调用c++新增预值点
	 */
	@Override
	public void savePresent(UserVo userVo, PresetOptionVo vo,String cameraId) {
		
		Example exaName = new Example(PresentPosition.class);
		exaName.createCriteria().andEqualTo("cameraId", cameraId).andEqualTo("name", vo.getName());
		int countName = presentPositionMapper.selectCountByExample(exaName);
		if(countName > 0) {
			throw new ServiceException("该摄像机已存在该预置位名称！");
		}
		
		Example exaNo = new Example(PresentPosition.class);
		exaNo.createCriteria().andEqualTo("cameraId", cameraId).andEqualTo("presentNo", vo.getPresetNum().toString());
		int countNo = presentPositionMapper.selectCountByExample(exaNo);
		if(countNo > 0) {
			throw new ServiceException("该摄像机已存在该预置位编号！");
		}
		
		Integer maxSort = presentPositionMapper.selectMaxSort(cameraId);

		PresentPosition presentPosition=new PresentPosition();
		presentPosition.setId(IdGenerator.nextId());
		presentPosition.setName(vo.getName());
		presentPosition.setCameraId(cameraId);
		presentPosition.setIsKeepwatch(0);
		presentPosition.setPresentNo(vo.getPresetNum().toString());
		presentPosition.setSort(maxSort == null? 1 : maxSort + 1);
		presentPosition.setCreateTime(new Date());
		presentPosition.setCreateUser(userVo.getAccount());
		presentPosition.setUpdateTime(new Date());
		presentPosition.setUpdateUser(userVo.getAccount());
		presentPositionMapper.insertSelective(presentPosition);
	}

	/**
	 * 查询本地库预置位号的最大值
	 */
	@Override
	public String checkParam(PresetOptionVo vo) {
		List<CameraVo> list =cameraMapper.seletCameraInfo(vo.getDeviceCode());
		if(list.size() >128) {
	    	throw new ServiceException("同一个设备最大能设置128个预置点！");
	    }
		String cameraId=null;
		if(list.size()>0) {
			if(StrUtil.isNotBlank(list.get(0).getId()) && vo.getPresetType() ==1) {//设置预置点
				cameraId=list.get(0).getId();
				Example exaName = new Example(PresentPosition.class);
				exaName.createCriteria().andEqualTo("cameraId", cameraId).andEqualTo("name", vo.getName());
				int countName = presentPositionMapper.selectCountByExample(exaName);
				if(countName > 0) {
					throw new ServiceException("该摄像机已存在该预置位名称！");
				}
			}else {
				cameraId=list.get(0).getId();
			}
		}
		return cameraId;
	    
	}

	/**
	 * 获取当前摄像机的最大编码
	 */
	@Override
	public int getMaxPresetNum(PresetOptionVo vo) {
		int max =1;
		String maxCode=presentPositionMapper.selectMaxPresetNum(vo.getDeviceCode());
		if(StrUtil.isNotBlank(maxCode)) {
			max=Integer.parseInt(maxCode)+1;
		}
		return max;
	}

	/**
	 * 删除预置点
	 */
	@Override
	public void delPresent(PresetOptionVo vo) {
		Example exa = new Example(PresentPosition.class);
		Criteria criteria = exa.createCriteria();
		if(StrUtil.isNotBlank(vo.getCameraId())) {
			criteria.andEqualTo("cameraId",vo.getCameraId());
		}else {
			List<CameraVo> list =cameraMapper.seletCameraInfo(vo.getDeviceCode());
			if(list.size() >0 ) {
				criteria.andEqualTo("cameraId",list.get(0).getId());
			}else {
				throw new ServiceException("设备不存在！");
			}
		}
		criteria.andEqualTo("presentNo", vo.getPresetNum());
		presentPositionMapper.deleteByExample(exa);
		
	}

	/**
	 * 编码是否存在
	 */
	@Override
	public int getCountByParam(PresetOptionVo vo, String cameraId) {
		Example exa = new Example(PresentPosition.class);
		Criteria criteria = exa.createCriteria();
		criteria.andEqualTo("cameraId",cameraId);
		criteria.andEqualTo("presetNum", vo.getPresetNum());
	    return presentPositionMapper.selectCountByExample(exa);
		
	}

	
	@Override
	public List<PresentPositionVo> selectPresetList(Map<String,Object> param) {
		return presentPositionMapper.selectPresetList(param);
	}

	@Override
	public String getMaxPresetNo(String cameraId) {
		String maxPresentNo=presentPositionMapper.selectMaxPresetNo(cameraId);
		return maxPresentNo;
	}

	@Override
	public void resetKeepwatch(String cameraId, String presentId) {
		PresentPosition keepwatchPresent = presentPositionMapper.selectByPrimaryKey(presentId);

		if(keepwatchPresent == null) {
			throw new ServiceException("设置守望位的预置位不存在！");
			
		}
		Example oldWatchExa = new Example(PresentPosition.class);
		oldWatchExa.createCriteria().andEqualTo("cameraId", cameraId).andEqualTo("isKeepwatch",1);
		PresentPosition pp = new PresentPosition();
		pp.setIsKeepwatch(0);
		presentPositionMapper.updateByExampleSelective(pp, oldWatchExa);
		
		PresentPosition newWatchPresent = new PresentPosition();
		newWatchPresent.setId(presentId);
		newWatchPresent.setIsKeepwatch(1);
		presentPositionMapper.updateByPrimaryKeySelective(newWatchPresent);
		
	}

	@Override
	public List<Integer> getEnablePresentPositions(String cameraId) {
		List<String> presentNos =presentPositionMapper.selectPresentNosByCamera(cameraId);
		List<Integer> enableList = new ArrayList<Integer>();
		for(int i=1;i<=255;i++) {
			if(!presentNos.contains(i+"")) {
				enableList.add(i);
			}
		}
		return enableList;
	}

	/**
	 * 批量添加预置位
	 */
	@Override
	public void savePresentPosition(UserVo userVo, List<PresentPositionVo> list, String cameraId) {
		String beforPresentNo="";
	    String newPresentNo="";
	    List<String> deleteNo=new ArrayList<String>();
	    List<String> deleteIds=new ArrayList<String>();
	    List<PresentPosition> addList=new ArrayList<PresentPosition>();
		Example exa = new Example(PresentPosition.class);
		Criteria criteria = exa.createCriteria();
		criteria.andEqualTo("cameraId",cameraId);
		
		//
		
	    List<PresentPosition> beforList=presentPositionMapper.selectByExample(exa);
	    Integer maxSort = presentPositionMapper.selectMaxSort(cameraId);
	    
		for(PresentPosition befor :beforList) {
			beforPresentNo+=befor.getPresentNo()+",";	
		}
		// 找出新增的预置点
	    for(PresentPositionVo  vo :list) {
			if(! beforPresentNo.contains(vo.getPresentNo())) {
				PresentPosition presentPosition=new PresentPosition();
				presentPosition.setId(IdGenerator.nextId());
				presentPosition.setName(vo.getName());
				presentPosition.setCameraId(cameraId);
				presentPosition.setIsKeepwatch(0);
				presentPosition.setPresentNo(vo.getPresentNo());
				presentPosition.setSort(maxSort == null? 1 : maxSort + 1);
				presentPosition.setCreateTime(new Date());
				presentPosition.setCreateUser(userVo.getAccount());
				
				addList.add(presentPosition);
				newPresentNo+=vo.getPresentNo()+",";
			}
		}
	    
	    // 找出要删除预置点
	    for(PresentPosition befor :beforList) {
			if(! newPresentNo.contains(befor.getPresentNo())) {
				deleteIds.add(befor.getId());
				deleteNo.add(befor.getPresentNo());
			}
		}
	    
	    // 新增预置点入库
	    if(addList.size() >0) {
	    	presentPositionMapper.insertList(addList);
	    }
	    
	    //删除查询不到的预置点，包括关联预案的预置
	    if(deleteNo.size() >0) {
	    	criteria.andIn("presentNo", deleteNo);
			presentPositionMapper.deleteByExample(exa);
			
			Example planExa = new Example(PlanPresent.class);
			planExa.createCriteria().andIn("presentId", deleteIds);
			planPresentMapper.deleteByExample(planExa);
	    }

	}

}
