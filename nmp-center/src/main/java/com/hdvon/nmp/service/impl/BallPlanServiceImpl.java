package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.BallPlan;
import com.hdvon.nmp.entity.Camera;
import com.hdvon.nmp.entity.Device;
import com.hdvon.nmp.entity.PlanPresent;
import com.hdvon.nmp.entity.PlanShare;
import com.hdvon.nmp.entity.PollingPlan;
import com.hdvon.nmp.entity.PresentPosition;
import com.hdvon.nmp.entity.WallPlan;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.BallPlanMapper;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.mapper.DepartmentMapper;
import com.hdvon.nmp.mapper.DeviceMapper;
import com.hdvon.nmp.mapper.PlanPresentMapper;
import com.hdvon.nmp.mapper.PlanShareMapper;
import com.hdvon.nmp.mapper.PresentPositionMapper;
import com.hdvon.nmp.service.IBallPlanService;
import com.hdvon.nmp.util.BeanHelper;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.BallPlanParamVo;
import com.hdvon.nmp.vo.BallPlanVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DepartmentVo;
import com.hdvon.nmp.vo.PlanShareVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.WallPlanVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>球机巡航预案表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class BallPlanServiceImpl implements IBallPlanService {

	@Autowired
	private BallPlanMapper ballPlanMapper;
	
	@Autowired
	private CameraMapper cameraMapper;
	
	@Autowired
	private DeviceMapper deviceMapper;
	
	@Autowired
	private PlanPresentMapper planPresentMapper;
	
	@Autowired
	private PlanShareMapper planShareMapper;
	
	@Autowired
	private PresentPositionMapper presentPositionMapper;
	
	@Autowired
	private DepartmentMapper departmentMapper;

	@Override
	public void saveBallPlan(UserVo userVo, BallPlanParamVo ballPlanParamVo) {
		BallPlan ballPlan = Convert.convert(BallPlan.class,ballPlanParamVo);
		if(StrUtil.isNotBlank(ballPlan.getId())) {//修改
			Example we = new Example(WallPlan.class);
			we.createCriteria().andEqualTo("name", ballPlan.getName()).andNotEqualTo("id", ballPlan.getId());
			int countName = ballPlanMapper.selectCountByExample(we);
			if(countName > 0) {
				throw new ServiceException("该预案已名称已经存在！");
			}
			Camera camera = cameraMapper.selectByPrimaryKey(ballPlan.getCameraId());
			if(camera == null) {
				throw new ServiceException("预案关联的摄像机不存在！");
			}
			
			ballPlan.setUpdateTime(new Date());
			ballPlan.setUpdateUser(userVo.getAccount());
			
			ballPlanMapper.updateByPrimaryKeySelective(ballPlan);//修改预案信息
			
			BallPlan ball = ballPlanMapper.selectByPrimaryKey(ballPlan.getId());
			Integer shareStatus = ballPlanParamVo.getShareStatus();
			if(shareStatus != ball.getShareStatus()) {
				//删除旧的共享设置
				Example planShareExa = new Example(PlanShare.class);
				planShareExa.createCriteria().andEqualTo("planId", ballPlan.getId());
				planShareMapper.deleteByExample(planShareExa);
				//添加新增之后的共享设置
				if(ballPlanParamVo.getShareStatus()!= null && (ballPlanParamVo.getShareStatus() == 2 || ballPlanParamVo.getShareStatus() == 3)) {//2部门共享；3指定部门共享
					List<PlanShare> planShares = new ArrayList<PlanShare>();
					PlanShare ps2 = new PlanShare();
					ps2.setId(IdGenerator.nextId());
					ps2.setPlanId(ballPlan.getId());
					ps2.setDepartmentId(userVo.getDepartmentId());
					planShares.add(ps2);
					String[] shareDeptIds = ballPlanParamVo.getShareDeptIds().split(",");
					if(shareDeptIds != null && shareDeptIds.length > 0) {//指定部门共享
						for(int i=0;i<shareDeptIds.length;i++) {
							PlanShare ps3 = new PlanShare();
							ps3.setId(IdGenerator.nextId());
							ps3.setPlanId(ballPlan.getId());
							ps3.setDepartmentId(userVo.getDepartmentId());
							planShares.add(ps3);
						}
					}
					planShareMapper.insertList(planShares);
				}
			}
		}else {//新增
			Example ppe = new Example(BallPlan.class);
			ppe.createCriteria().andEqualTo("name", ballPlan.getName());
			int countName = ballPlanMapper.selectCountByExample(ppe);
			if(countName > 0) {
				throw new ServiceException("预案名称已经存在！");
			}
			ballPlan.setId(IdGenerator.nextId());
			ballPlan.setCreateTime(new Date());
			ballPlan.setCreateUser(userVo.getAccount());
			ballPlan.setUpdateTime(new Date());
			ballPlan.setUpdateUser(userVo.getAccount());
			ballPlan.setShareStatus(0);//私有
			
			ballPlanMapper.insertSelective(ballPlan);
		}
		
	}

	@Override
	public PageInfo<BallPlanVo> getBallPlanPages(PageParam pp, Map<String, Object> map) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<BallPlanVo> ballPlanVos = ballPlanMapper.selectBallPlanList(map);
		return new PageInfo<BallPlanVo>(ballPlanVos);
	}

	@Override
	public List<BallPlanVo> getBallPlanList(BallPlanParamVo ballPlanParamVo, UserVo userVo) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("name", ballPlanParamVo.getName());
        paramMap.put("status", ballPlanParamVo.getStatus());
        paramMap.put("sbbm", ballPlanParamVo.getCameraNo());
        paramMap.put("isValid", ballPlanParamVo.getIsValid());
        if(userVo != null) {
        	 paramMap.put("account", userVo.getAccount());
             paramMap.put("isAdmin", userVo.isAdmin());
        }
		List<BallPlanVo> ballPlanVos = ballPlanMapper.selectBallPlanList(paramMap);
		return ballPlanVos;
	}

	@Override
	public void delBallPlansByIds(List<String> ids) {
		Example wpeStatus = new Example(WallPlan.class);
		wpeStatus.createCriteria().andIn("id", ids).andEqualTo("status", "1");
		int countStatus = ballPlanMapper.selectCountByExample(wpeStatus);
		if(countStatus>0) {
			throw new ServiceException("启用的预案不能删除！");
		}
		
		Example ePresent = new Example(PlanPresent.class);
		ePresent.createCriteria().andIn("planId",ids);
		int countPresent = planPresentMapper.selectCountByExample(ePresent);
		if(countPresent > 0) {
			throw new ServiceException("球机巡航预案存在预置位，不能删除！");
		}
		
		/*Example wceShare = new Example(PlanShare.class);
		wceShare.createCriteria().andIn("planId", ids);
		int countShare = planShareMapper.selectCountByExample(wceShare);
		if(countShare > 0) {
			throw new ServiceException("球机巡航预案存在共享设置，不能删除！");
		}*/
		
		Example wpe = new Example(BallPlan.class);
		wpe.createCriteria().andIn("id", ids);
		ballPlanMapper.deleteByExample(wpe);//删除上墙预案
		
		Example shareExa = new Example(PlanShare.class);
		shareExa.createCriteria().andIn("planId", ids);
		planShareMapper.deleteByExample(shareExa);//删除共享设置
		
		/*Example e_present = new Example(BallplanPresent.class);
		e_present.createCriteria().andIn("ballplanId",ids);
		ballplanPresentMapper.deleteByExample(e_present);*///删除球机巡航预案与预置位的关联
		
		/*Example wce_share = new Example(PlanShare.class);
		wce_share.createCriteria().andIn("planId", ids);
		planShareMapper.deleteByExample(wce_share);*///删除预案的共享设置
		
	}

	@Override
	public BallPlanVo getBallPlanById(String id) {
		BallPlanVo ballPlanVo = ballPlanMapper.selectBallPlanInfoById(id);
		return ballPlanVo;
	}

	@Override
	public void saveBallplanShares(String ballplanId, List<String> departmentIds) {
		Example e = new Example(PlanShare.class);
		e.createCriteria().andEqualTo("planId", ballplanId);
		planShareMapper.deleteByExample(e);//删除未修改前的共享设置
		
		List<PlanShare> list = new ArrayList<PlanShare>();
		for(String deptId : departmentIds) {
			PlanShare ps = new PlanShare();
			ps.setId(IdGenerator.nextId());
			ps.setPlanId(ballplanId);
			ps.setDepartmentId(deptId);
		}
		planShareMapper.insertList(list);//保存修改后的共享设置
	}

	@Override
	public BallPlanVo getBallplanShares(String ballplanId) {
		BallPlan ballPlan = ballPlanMapper.selectByPrimaryKey(ballplanId);
		BallPlanVo ballPlanVo = Convert.convert(BallPlanVo.class, ballPlan);
		
		/*Example e = new Example(PlanShare.class);
		e.createCriteria().andEqualTo("planId", ballplanId);
		List<PlanShare> list = planShareMapper.selectByExample(e);
		List<PlanShareVo> shareVos = BeanHelper.convertList(PlanShareVo.class, list);*/
		/*Map<String,Object> param = new HashMap<String, Object>();
		param.put("planId", ballplanId);
		List<PlanShareVo> shareVos =planShareMapper.selectByParam(param);*/
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("planId", ballplanId);
		List<DepartmentVo> deptVos = departmentMapper.selectShowDeptTreeByPlanId(map);
		ballPlanVo.setDepartmentVos(deptVos);
		return ballPlanVo;
	}

	@Override
	public List<PresentPositionVo> getPresentPositionsInBallPlan(Map<String,Object> map) {
		List<PresentPositionVo> presentPositionVos = presentPositionMapper.selectPresentsInBallPlan(map);
		return presentPositionVos;
	}

	@Override
	public void savePresentPositionToBallPlan(String ballplanId, List<String> ballPresentIdList, List<String> timeIntervals, List<String> presentIds) {
		BallPlan ballPlan = ballPlanMapper.selectByPrimaryKey(ballplanId);
		if(ballPlan == null) {
			throw new ServiceException("该球机巡航预案不存在！");
		}
		
		Example exa = new Example(PlanPresent.class);
		exa.createCriteria().andEqualTo("planId",ballplanId);
		planPresentMapper.deleteByExample(exa);//删除未修改前的所有球机预案预置位
		
		List<PlanPresent> list = new ArrayList<PlanPresent>();
		for(int i=0;i< ballPresentIdList.size();i++) {
			PlanPresent bp = new PlanPresent();
			bp.setId(IdGenerator.nextId());
			bp.setPlanId(ballplanId);
			bp.setPresentId(ballPresentIdList.get(i));
			bp.setTimeInterval(Integer.parseInt(timeIntervals.get(i)));
			bp.setSort(i);
			list.add(bp);
		}
		if(list != null && list.size()>0) {
			planPresentMapper.insertList(list);//添加修改后的所有选择的球机预案预置位
		}
		
		for(int i=0;i< presentIds.size();i++) {//预置位重新排序
			PresentPosition pp = new PresentPosition();
			pp.setId(presentIds.get(i));
			pp.setSort(i);
			presentPositionMapper.updateByPrimaryKeySelective(pp);
		}
	}

	@Override
	public void shiftingUpDown(String ballplanId, List<String> ids) {
		for(int i=0;i<ids.size();i++) {
			String id = ids.get(i);
			Example exa = new Example(PlanPresent.class);
			exa.createCriteria().andEqualTo("planId",ballplanId).andEqualTo("presentId",id).andIsNotNull("presentId");
			PlanPresent bp = new PlanPresent();
			/*bp.setBallplanId(ballplanId);
			bp.setPresentId(id);*/
			bp.setSort(i);//根据前台传过来的参数顺序设置排序序号
			planPresentMapper.updateByExampleSelective(bp, exa);//更新排序序号
		}
	}

	@Override
	public List<PresentPositionVo> getBallPresentsInCamera(Map<String, Object> amp) {
		List<PresentPositionVo> presentPositionVos = presentPositionMapper.selectBallPresentsInCamera(amp);
		return presentPositionVos;
	}

}
