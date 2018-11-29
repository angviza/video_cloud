package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.entity.PlanPresent;
import com.hdvon.nmp.entity.PlanShare;
import com.hdvon.nmp.entity.PollingPlan;
import com.hdvon.nmp.entity.PollingplanCamera;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IPollingPlanService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>轮询预案Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PollingPlanServiceImpl implements IPollingPlanService {

	@Autowired
	private PollingPlanMapper pollingPlanMapper;
	
	@Autowired
	private CameraMapper cameraMapper;
	
	@Autowired
	private PollingplanCameraMapper pollingplanCameraMapper;
	
	@Autowired
	private PresentPositionMapper presentPositionMapper;
	
	@Autowired
	private PlanPresentMapper planPresentMapper;
	
	@Autowired
	private PlanShareMapper planShareMapper;
	
	@Autowired
	private DepartmentMapper departmentMapper;

    @Autowired
    private CameragrouopMapper cameragrouopMapper;
	
	@Override
	public void savePollingPlan(UserVo userVo, PollingPlanParamVo pollingPlanParamVo, String cameraIds, boolean isMap) {
		PollingPlan pollingPlan = Convert.convert(PollingPlan.class,pollingPlanParamVo);
		if(StrUtil.isNotBlank(pollingPlanParamVo.getId())) {//修改
			Example ppe = new Example(PollingPlan.class);
			ppe.createCriteria().andEqualTo("name", pollingPlanParamVo.getName()).andNotEqualTo("id", pollingPlanParamVo.getId());
			int countName = pollingPlanMapper.selectCountByExample(ppe);
			if(countName > 0) {
				throw new ServiceException("预案名称已经存在！");
			}
			pollingPlan.setUpdateTime(new Date());
			pollingPlan.setUpdateUser(userVo.getAccount());
			
			pollingPlanMapper.updateByPrimaryKeySelective(pollingPlan);//修改预案信息
			
			PollingPlan polling = pollingPlanMapper.selectByPrimaryKey(pollingPlan.getId());
			Integer shareStatus = pollingPlanParamVo.getShareStatus();
			if(shareStatus != polling.getShareStatus()) {
				//删除旧的共享设置
				Example planShareExa = new Example(PlanShare.class);
				planShareExa.createCriteria().andEqualTo("planId", pollingPlan.getId());
				planShareMapper.deleteByExample(planShareExa);
				//添加新增之后的共享设置
				if(pollingPlanParamVo.getShareStatus() != null && (pollingPlanParamVo.getShareStatus() == 2 || pollingPlanParamVo.getShareStatus() == 3)) {//2部门共享；3指定部门共享
					List<PlanShare> planShares = new ArrayList<PlanShare>();
					PlanShare ps2 = new PlanShare();
					ps2.setId(IdGenerator.nextId());
					ps2.setPlanId(pollingPlan.getId());
					ps2.setDepartmentId(userVo.getDepartmentId());
					planShares.add(ps2);
					String[] shareDeptIds = pollingPlanParamVo.getShareDeptIds().split(",");
					if(shareDeptIds != null && shareDeptIds.length > 0) {//指定部门共享
						for(int i=0;i<shareDeptIds.length;i++) {
							PlanShare ps3 = new PlanShare();
							ps3.setId(IdGenerator.nextId());
							ps3.setPlanId(pollingPlan.getId());
							ps3.setDepartmentId(userVo.getDepartmentId());
							planShares.add(ps3);
						}
					}
					planShareMapper.insertList(planShares);
				}
			}
			if(isMap) {
				Example pce = new Example(PollingplanCamera.class);
				pce.createCriteria().andEqualTo("pollingplanId", pollingPlanParamVo.getId());
				pollingplanCameraMapper.deleteByExample(pce);//删除原来的摄像机关联
			}
		}else {//新增
			Example ppe = new Example(PollingPlan.class);
			ppe.createCriteria().andEqualTo("name", pollingPlanParamVo.getName());
			int countName = pollingPlanMapper.selectCountByExample(ppe);
			if(countName > 0) {
				throw new ServiceException("预案名称已经存在！");
			}
			pollingPlan.setId(IdGenerator.nextId());
			//pollingPlan.setStatus(1);//默认“启用”
			pollingPlan.setShareStatus(0);
			pollingPlan.setCreateTime(new Date());
			pollingPlan.setCreateUser(userVo.getAccount());
			pollingPlan.setUpdateTime(new Date());
			pollingPlan.setUpdateUser(userVo.getAccount());
			
			pollingPlanMapper.insertSelective(pollingPlan);
			
			if(pollingPlanParamVo.getShareStatus() != null && (pollingPlanParamVo.getShareStatus() == 2 || pollingPlanParamVo.getShareStatus() == 3)) {//2部门共享；3指定部门共享
				List<PlanShare> planShares = new ArrayList<PlanShare>();
				PlanShare ps2 = new PlanShare();
				ps2.setId(IdGenerator.nextId());
				ps2.setPlanId(pollingPlan.getId());
				ps2.setDepartmentId(userVo.getDepartmentId());
				planShares.add(ps2);
				String[] shareDeptIds = pollingPlanParamVo.getShareDeptIds().split(",");
				if(shareDeptIds != null && shareDeptIds.length > 0) {//指定部门共享
					for(int i=0;i<shareDeptIds.length;i++) {
						PlanShare ps3 = new PlanShare();
						ps3.setId(IdGenerator.nextId());
						ps3.setPlanId(pollingPlan.getId());
						ps3.setDepartmentId(userVo.getDepartmentId());
						planShares.add(ps3);
					}
				}
				planShareMapper.insertList(planShares);
			}
		}
		if(isMap && StrUtil.isNotBlank(cameraIds)) {//保存地图创建或者编辑预案关联摄像机
			String[] cameraIdArr = cameraIds.split(",");
			List<String> cameraIdList = Arrays.asList(cameraIdArr);
			 //摄像机列表
			int sort = 1;
	        List<PollingplanCamera> cameraList = new ArrayList<>();
	        for (String cameraId : cameraIdList) {
	            PollingplanCamera pollingCamera = new PollingplanCamera();
	            pollingCamera.setId(IdGenerator.nextId());
	            pollingCamera.setPollingplanId(pollingPlan.getId());
	            pollingCamera.setCameraId(cameraId);
	            pollingCamera.setType(1);
	            pollingCamera.setSort(sort);
	            sort++;
	            cameraList.add(pollingCamera);
	        }
	        if (cameraList != null && cameraList.size() > 0) {
	            pollingplanCameraMapper.insertList(cameraList);
	        }
		}
		
	}

	@Override
	public PageInfo<PollingPlanVo> getPollingPlanPages(PageParam pp, PollingPlanParamVo pollingPlanParamVo , UserVo userVo){
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id", pollingPlanParamVo.getId());
        paramMap.put("name", pollingPlanParamVo.getName());
        paramMap.put("status", pollingPlanParamVo.getStatus());
        paramMap.put("account", userVo.getAccount());
        paramMap.put("isAdmin", userVo.isAdmin());
		List<PollingPlanVo> pollingPlanVos = pollingPlanMapper.selectPollingPlanList(paramMap);
        PageInfo<PollingPlanVo> voPageInfo = new PageInfo<>(pollingPlanVos);
        return voPageInfo;
	}

	@Override
	public List<PollingPlanVo> getPollingPlanList(PollingPlanParamVo pollingPlanParamVo , UserVo userVo) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("id", pollingPlanParamVo.getId());
        paramMap.put("name", pollingPlanParamVo.getName());
        paramMap.put("status", pollingPlanParamVo.getStatus());
        paramMap.put("isValid", pollingPlanParamVo.getIsValid());
        paramMap.put("account", userVo.getAccount());
        paramMap.put("isAdmin", userVo.isAdmin());
		List<PollingPlanVo> pollingPlanVos = pollingPlanMapper.selectPollingPlanList(paramMap);
        return pollingPlanVos;
	}

	@Override
	public void delPollingPlansByIds(List<String> ids) {
		/*PollingplanCameraExample pce = new PollingplanCameraExample();
		pce.createCriteria().andPollingplanIdIn(ids);
		int count_camera = pollingplanCameraMapper.countByExample(pce);
		if(count_camera>0) {
			throw new ServiceException("轮询预案关联了摄像机，不能删除！");
		}*/
		
		Example ppeStatus = new Example(PollingPlan.class);
		ppeStatus.createCriteria().andIn("id", ids).andEqualTo("status", "1");
		int countStatus = pollingPlanMapper.selectCountByExample(ppeStatus);
		if(countStatus>0) {
			throw new ServiceException("不能删除启用的轮询预案！");
		}
		Example ppe = new Example(PollingPlan.class);
		ppe.createCriteria().andIn("id",ids);
		pollingPlanMapper.deleteByExample(ppe);//删除轮询预案
		
		Example pce = new Example(PollingplanCamera.class);
		pce.createCriteria().andIn("pollingplanId", ids);
		pollingplanCameraMapper.deleteByExample(pce);//删除轮询预案与摄像头的关联
		
		Example shareExa = new Example(PlanShare.class);
		shareExa.createCriteria().andIn("planId", ids);
		planShareMapper.deleteByExample(shareExa);//删除共享设置
	}

	@Override
	public PollingPlanVo getPollingPlanById(String id) {
		PollingPlan pollingPlan = pollingPlanMapper.selectByPrimaryKey(id);
		PollingPlanVo pollingPlanVo = Convert.convert(PollingPlanVo.class,pollingPlan);	
		return pollingPlanVo;
	}

	@Override
    public void savePollingRelateCameras(String pollingplanId, List<String> cameraIdList, List<String> cameraGroupIdList, List<String> mapCameraIdList) {
        /*删除预案与摄像机之前的关联*/
        Example wce = new Example(PollingplanCamera.class);
        wce.createCriteria().andEqualTo("pollingplanId", pollingplanId);
        pollingplanCameraMapper.deleteByExample(wce);
        
        Map<String,Integer> sortMap = new HashMap<String,Integer>();

        //摄像机列表
        List<PollingplanCamera> cameraList = new ArrayList<>();
        for (String cameraId : cameraIdList) {
            PollingplanCamera pollingCamera = new PollingplanCamera();
            pollingCamera.setId(IdGenerator.nextId());
            pollingCamera.setPollingplanId(pollingplanId);
            pollingCamera.setCameraId(cameraId);
            pollingCamera.setType(1);
            cameraList.add(pollingCamera);
            sortMap.put(cameraId, 0);
        }

        //分组列表
        if(cameraGroupIdList != null && cameraGroupIdList.size() > 0){
            List<CameraVo> cameraVos = cameraMapper.selectCamerasInGroups(cameraGroupIdList);
            for (CameraVo cameraVo : cameraVos) {
                PollingplanCamera pollingCamera = new PollingplanCamera();
                pollingCamera.setId(IdGenerator.nextId());
                pollingCamera.setPollingplanId(pollingplanId);
                pollingCamera.setCameraId(cameraVo.getId());
                pollingCamera.setCameragroupId(cameraVo.getCameraGroupId());
                pollingCamera.setType(2);
                cameraList.add(pollingCamera);
                sortMap.put(cameraVo.getId(), 0);
            }
        }

        //地图摄像机列表
        for (String cameraId : mapCameraIdList) {
            PollingplanCamera pollingCamera = new PollingplanCamera();
            pollingCamera.setId(IdGenerator.nextId());
            pollingCamera.setPollingplanId(pollingplanId);
            pollingCamera.setCameraId(cameraId);
            pollingCamera.setType(3);
            cameraList.add(pollingCamera);
            sortMap.put(cameraId, 0);
        }

        //初始序号
        int sort = 1;
        for(PollingplanCamera pollingplanCamera : cameraList) {
        	if(sortMap.get(pollingplanCamera.getCameraId()) == 0) {
        		pollingplanCamera.setSort(sort);
        		sortMap.put(pollingplanCamera.getCameraId(), sort);
        		sort++;
        	}else {
        		pollingplanCamera.setSort(sortMap.get(pollingplanCamera.getCameraId()));
        	}
        }//设置默认排序序号
        
        if (cameraList != null && cameraList.size() > 0) {
            pollingplanCameraMapper.insertList(cameraList);
        }

        //删除预案关联的预置位
        Example presentExample = new Example(PlanPresent.class);
        presentExample.createCriteria().andEqualTo("planId", pollingplanId);
        planPresentMapper.deleteByExample(presentExample);

        List<String> allCameraList = new ArrayList<>();
        allCameraList.addAll(cameraIdList);
        allCameraList.addAll(mapCameraIdList);

        //添加预案关联的预置位为守望位
        if (allCameraList != null && allCameraList.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isKeepwatch", 1);
            map.put("cameraIds", allCameraList);
            List<PresentPositionVo> presents = presentPositionMapper.selectPresentPositionsInCameras(map);
            if (presents != null && presents.size() > 0) {
                List<PlanPresent> planPresents = new ArrayList<>();
                for (PresentPositionVo present : presents) {
                    PlanPresent pp = new PlanPresent();
                    pp.setId(IdGenerator.nextId());
                    pp.setPlanId(pollingplanId);
                    pp.setPresentId(present.getId());
                    pp.setSort(present.getSort());
                    planPresents.add(pp);
                }
                planPresentMapper.insertList(planPresents);
            }
        }

    }


    @Override
    public List<CameraVo> getPollingGroupCameras(String pollingplanId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pollingplanId", pollingplanId);
        List<CameraVo> list = cameraMapper.selectCamerasInPollingPlan(map);
        List<CameraVo> noGroupCamera = new ArrayList<CameraVo>();
        List<String> groupCameraIds = new ArrayList<String>();
        for(CameraVo cameraVo : list) {
        	if(StrUtil.isBlank(cameraVo.getCameraGroupId())) {
        		noGroupCamera.add(cameraVo);
        	}else {
        		groupCameraIds.add(cameraVo.getId());
        	}
        }
        if(groupCameraIds.size() == 0) {
        	return noGroupCamera;
        }else {
        	for(CameraVo cameraVo : noGroupCamera) {
        		if(groupCameraIds.contains(cameraVo.getId())) {
        			list.remove(cameraVo);
        		}
        	}
        	return list;
        }
    }

	@Override
	public PollingPlanVo getPollingplanShares(String pollingplanId) {
		PollingPlan pollingPlan = pollingPlanMapper.selectByPrimaryKey(pollingplanId);
		PollingPlanVo pollingPlanVo = Convert.convert(PollingPlanVo.class, pollingPlan);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("planId", pollingplanId);
		List<DepartmentVo> deptVos = departmentMapper.selectShowDeptTreeByPlanId(map);
		pollingPlanVo.setDepartmentVos(deptVos);
		return pollingPlanVo;
	}

	@Override
	public List<CameraVo> getCamerasByPollingplanId(String pollingplanId) {
		List<CameraVo> cameras = cameraMapper.selectCamerasByPollingId(pollingplanId);
		return cameras;
	}

	@Override
	public List<PollingplanCameraVo> getPollingPlanRelatedCameras(Map<String, Object> map) {
		List<PollingplanCameraVo> relatedCameraVos =pollingplanCameraMapper.selectPlanRelatedCameras(map);
		return relatedCameraVos;
	}

    @Override
	public PollingPlanLinksVo getPollingPlanLinks(String pollingplanId, UserVo userVo){
	    if(StrUtil.isEmpty(pollingplanId)){
	        throw new ServiceException("预案id不能为空");
        }
        //查询轮巡地址摄像机
        Map<String,Object> map = new HashMap<>();
	    map.put("pollingplanId",pollingplanId);
	    map.put("pollingType","1");
        List<CameraNode> addressCameraList = cameraMapper.selectCameraNode(map);

        //查询轮巡预案分组
        map.clear();
        map.put("pollingplanId",pollingplanId);
        map.put("isAdmin",userVo.isAdmin());
        map.put("userId",userVo.getId());
        List<CameragrouopVo> groupList = cameragrouopMapper.selectByParam(map);

        //查询轮巡地图摄像机
        map.clear();
        map.put("pollingplanId",pollingplanId);
        map.put("pollingType","3");
        List<CameraNode> mapCameraList = cameraMapper.selectCameraNode(map);

        //组装返回对象
        PollingPlanLinksVo vo = new PollingPlanLinksVo();
        vo.setAddressCameraList(addressCameraList);
        vo.setGroupList(groupList);
        vo.setMapCameraList(mapCameraList);
        return vo;
    }

	@Override
	public void changeCameraSort(String pollingplanId, String curCameraId, String cameraId) {
		
		Integer curSort = 0;
		Integer changeSort = 0;
		Example curSortExa = new Example(PollingplanCamera.class);
		curSortExa.createCriteria().andEqualTo("pollingplanId", pollingplanId).andEqualTo("cameraId", curCameraId);
		List<PollingplanCamera> curList = pollingplanCameraMapper.selectByExample(curSortExa);
		if(curList.size()>0) {
			curSort = curList.get(0).getSort();
		}else {
			throw new ServiceException("预案下不存在该移动的摄像机,请刷新页面！");
		}
		
		Example changeSortExa = new Example(PollingplanCamera.class);
		changeSortExa.createCriteria().andEqualTo("pollingplanId",pollingplanId).andEqualTo("cameraId", cameraId);
		List<PollingplanCamera> changeList = pollingplanCameraMapper.selectByExample(changeSortExa);
		if(changeList.size()>0) {
			changeSort = changeList.get(0).getSort();
		}else {
			throw new ServiceException("预案下不存在移动的摄像机，请刷新页面！");
		}
		
		Example updateCurSort = new Example(PollingplanCamera.class);
		updateCurSort.createCriteria().andEqualTo("pollingplanId", pollingplanId).andEqualTo("cameraId", curCameraId);
		PollingplanCamera pollingplanCamera = new PollingplanCamera();
		pollingplanCamera.setSort(changeSort);
		pollingplanCameraMapper.updateByExampleSelective(pollingplanCamera, updateCurSort);
		
		Example updateChangeSort = new Example(PollingplanCamera.class);
		updateChangeSort.createCriteria().andEqualTo("pollingplanId", pollingplanId).andEqualTo("cameraId", cameraId);
		PollingplanCamera changePollingplanCamera = new PollingplanCamera();
		changePollingplanCamera.setSort(curSort);
		pollingplanCameraMapper.updateByExampleSelective(changePollingplanCamera, updateChangeSort);
	}

}
