package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.entity.*;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IWallPlanService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>上墙预案Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class WallPlanServiceImpl implements IWallPlanService {

	@Autowired
	private WallPlanMapper wallPlanMapper;
	
	@Autowired
	private MatrixMapper matrixMapper;
	
	@Autowired
	private WallplanCameraMapper wallplanCameraMapper;
	
	@Autowired
	private MatrixChannelMapper matrixChannelMapper;
	
	@Autowired
	private CameraMapper cameraMapper;
	
	@Autowired
	private PresentPositionMapper presentPositionMapper;
	
	@Autowired
	private PlanPresentMapper planPresentMapper;
	
	@Autowired
	private PlanShareMapper planShareMapper;
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Override
	public void saveWallPlan(UserVo userVo, WallPlanParamVo wallPlanParamVo) {
		WallPlan wallPlan = Convert.convert(WallPlan.class,wallPlanParamVo);
		if(StrUtil.isNotBlank(wallPlanParamVo.getId())) {//修改
			Example we = new Example(WallPlan.class);
			we.createCriteria().andEqualTo("name", wallPlanParamVo.getName()).andNotEqualTo("id", wallPlanParamVo.getId());
			int countName = wallPlanMapper.selectCountByExample(we);
			if(countName > 0) {
				throw new ServiceException("预案名称已经存在！");
			}
			Matrix matrix = matrixMapper.selectByPrimaryKey(wallPlanParamVo.getMatrixId());
			if(matrix == null) {
				throw new ServiceException("矩阵不存在！");
			}
			
			wallPlan.setUpdateTime(new Date());
			wallPlan.setUpdateUser(userVo.getAccount());
			
			wallPlanMapper.updateByPrimaryKeySelective(wallPlan);//修改预案信息
			
			WallPlan wall = wallPlanMapper.selectByPrimaryKey(wallPlan.getId());
			Integer shareStatus = wallPlanParamVo.getShareStatus();
			if(shareStatus != wall.getShareStatus()) {
				//删除旧的共享设置
				Example planShareExa = new Example(PlanShare.class);
				planShareExa.createCriteria().andEqualTo("planId", wallPlan.getId());
				planShareMapper.deleteByExample(planShareExa);
				//添加新增之后的共享设置
				if(wallPlanParamVo.getShareStatus() != null && (wallPlanParamVo.getShareStatus() == 2 || wallPlanParamVo.getShareStatus() == 3)) {//2部门共享；3指定部门共享
					List<PlanShare> planShares = new ArrayList<PlanShare>();
					PlanShare ps2 = new PlanShare();
					ps2.setId(IdGenerator.nextId());
					ps2.setPlanId(wallPlan.getId());
					ps2.setDepartmentId(userVo.getDepartmentId());
					planShares.add(ps2);
					String[] shareDeptIds = wallPlanParamVo.getShareDeptIds().split(",");
					if(shareDeptIds != null && shareDeptIds.length > 0) {//指定部门共享
						for(int i=0;i<shareDeptIds.length;i++) {
							PlanShare ps3 = new PlanShare();
							ps3.setId(IdGenerator.nextId());
							ps3.setPlanId(wallPlan.getId());
							ps3.setDepartmentId(userVo.getDepartmentId());
							planShares.add(ps3);
						}
					}
					planShareMapper.insertList(planShares);
				}
			}
		}else {//新增
			Example ppe = new Example(WallPlan.class);
			ppe.createCriteria().andEqualTo("name", wallPlanParamVo.getName());
			int countName = wallPlanMapper.selectCountByExample(ppe);
			if(countName > 0) {
				throw new ServiceException("预案名称已经存在！");
			}
			wallPlan.setId(IdGenerator.nextId());
			wallPlan.setCreateTime(new Date());
			wallPlan.setCreateUser(userVo.getAccount());
			wallPlan.setUpdateTime(new Date());
			wallPlan.setUpdateUser(userVo.getAccount());
			
			wallPlanMapper.insertSelective(wallPlan);
			
			if(wallPlanParamVo.getShareStatus() != null && (wallPlanParamVo.getShareStatus() == 2 || wallPlanParamVo.getShareStatus() == 3)) {//2部门共享；3指定部门共享
				List<PlanShare> planShares = new ArrayList<PlanShare>();
				PlanShare ps2 = new PlanShare();
				ps2.setId(IdGenerator.nextId());
				ps2.setPlanId(wallPlan.getId());
				ps2.setDepartmentId(userVo.getDepartmentId());
				planShares.add(ps2);
				String[] shareDeptIds = wallPlanParamVo.getShareDeptIds().split(",");
				if(shareDeptIds != null && shareDeptIds.length > 0) {//指定部门共享
					for(int i=0;i<shareDeptIds.length;i++) {
						PlanShare ps3 = new PlanShare();
						ps3.setId(IdGenerator.nextId());
						ps3.setPlanId(wallPlan.getId());
						ps3.setDepartmentId(userVo.getDepartmentId());
						planShares.add(ps3);
					}
				}
				planShareMapper.insertList(planShares);
			}
		}
		/*List<Map<String,List<String>>> idList = wallPlanVo.getIdList();
		List<WallplanCamera> list = new ArrayList<WallplanCamera>();
		for(int i=0;i<idList.size();i++) {
			Map<String,List<String>> map = idList.get(i);
			for(Map.Entry<String, List<String>> entry : map.entrySet()) {
				String key = entry.getKey();//矩阵通道id
				List<String> value = entry.getValue();//矩阵通道对应的摄像机id集合
				for(int j = 0;j<value.size();j++) {
					String cameraId = value.get(j);
					WallplanCamera pc = new WallplanCamera();
					pc.setId(IdGenerator.nextId());
					pc.setWallplanId(wallPlan.getId());
					pc.setMatrixchannelId(key);
					pc.setCameraId(cameraId);
					list.add(pc);
				}
			}
		}*/
		/*String matrixId = wallPlanVo.getMatrixId();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("matrixId", matrixId);
		List<MatrixChannelVo> matrixChannelVos = matrixChannelMapper.selectMatrixChannelList(map);
		wallplanCameraMapper.insertBatch(list);*///新增新的摄像机关联
	}

	@Override
	public PageInfo<WallPlanVo> getWallPlanPages(PageParam pp, Map<String, Object> map) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<WallPlanVo> wallPlanVos = wallPlanMapper.selectWallPlanList(map);

		if(wallPlanVos!=null && wallPlanVos.size()>0) {
			List<String> matrixIds = new ArrayList<String>();
			for(int i=0;i<wallPlanVos.size();i++) {
				if(wallPlanVos.get(i) == null) {
					continue;
				}
				matrixIds.add(wallPlanVos.get(i).getMatrixId());
			}
			if(matrixIds.size()>0) {
				/*Example mce = new Example(MatrixChannel.class);
				mce.createCriteria().andIn("matrixId", matrixIds);
				List<MatrixChannel> list = matrixChannelMapper.selectByExample(mce);
				Multimap<String,MatrixChannelVo> matrixChannelMutimap=ArrayListMultimap.create();
				List<MatrixChannelVo> matrixChannelVos = BeanHelper.convertList(MatrixChannelVo.class, list);
				*/
				
				Multimap<String,MatrixChannelVo> matrixChannelMutimap=ArrayListMultimap.create(); 
				/*Map<String,Object> param = new HashMap<String, Object>();
				param.put("matrixId", matrixIds);
				List<MatrixChannelVo> matrixChannelVos=matrixChannelMapper.selectByParam(param);*/
				List<MatrixChannelVo> matrixChannelVos=matrixChannelMapper.selectMatrixChannelsByMatrixIds(matrixIds);
				for(MatrixChannelVo matrixChannelVo : matrixChannelVos) {
					matrixChannelMutimap.put(matrixChannelVo.getMatrixId(), matrixChannelVo);
				}
				for(WallPlanVo wallPlanVo : wallPlanVos) {
					wallPlanVo.setMatrixChannels((List<MatrixChannelVo>) matrixChannelMutimap.get(wallPlanVo.getMatrixId()));
				}
			}
		}
		
    	return new PageInfo<WallPlanVo>(wallPlanVos);
	}

	@Override
	public List<WallPlanVo> getWallPlanList(WallPlanParamVo wallPlanParamVo, UserVo userVo) {
		 Map<String,Object> paramMap = new HashMap<String,Object>();
         paramMap.put("id", wallPlanParamVo.getId());
         paramMap.put("name", wallPlanParamVo.getName());
         paramMap.put("status", wallPlanParamVo.getStatus());
         paramMap.put("matrixId", wallPlanParamVo.getMatrixId());
         paramMap.put("isValid", wallPlanParamVo.getIsValid());
         paramMap.put("account", userVo.getAccount());
         paramMap.put("isAdmin", userVo.isAdmin());
		List<WallPlanVo> wallPlanVos = wallPlanMapper.selectWallPlanList(paramMap);

		if(wallPlanVos!=null && wallPlanVos.size()>0) {
			List<String> matrixIds = new ArrayList<String>();
			for(int i=0;i<wallPlanVos.size();i++) {
				if(wallPlanVos.get(i) == null) {
					continue;
				}
				matrixIds.add(wallPlanVos.get(i).getMatrixId());
			}
			if(matrixIds.size()>0) {
				/*Example mce = new Example(MatrixChannel.class);
				mce.createCriteria().andIn("matrixId", matrixIds);
				List<MatrixChannel> list = matrixChannelMapper.selectByExample(mce);
				Multimap<String,MatrixChannelVo> matrixChannelMutimap=ArrayListMultimap.create(); 
				List<MatrixChannelVo> matrixChannelVos = BeanHelper.convertList(MatrixChannelVo.class, list);
				*/
				Multimap<String,MatrixChannelVo> matrixChannelMutimap=ArrayListMultimap.create(); 
				/*Map<String,Object> param = new HashMap<String, Object>();
				param.put("matrixId", matrixIds);*/
				List<MatrixChannelVo> matrixChannelVos=matrixChannelMapper.selectMatrixChannelsByMatrixIds(matrixIds);
				for(MatrixChannelVo matrixChannelVo : matrixChannelVos) {
					matrixChannelMutimap.put(matrixChannelVo.getMatrixId(), matrixChannelVo);
				}
				for(WallPlanVo wallPlanVo : wallPlanVos) {
					wallPlanVo.setMatrixChannels((List<MatrixChannelVo>) matrixChannelMutimap.get(wallPlanVo.getMatrixId()));
				}
			}
		}
    	return wallPlanVos;
	}

	@Override
	public void delWallPlansByIds(List<String> ids) {
		Example wpeStatus = new Example(WallPlan.class);
		wpeStatus.createCriteria().andIn("id", ids).andEqualTo("status", "1");
		int countStatus = wallPlanMapper.selectCountByExample(wpeStatus);
		if(countStatus>0) {
			throw new ServiceException("启用的预案不能删除！");
		}
		
		Example wpe = new Example(WallPlan.class);
		wpe.createCriteria().andIn("id", ids);
		wallPlanMapper.deleteByExample(wpe);//删除上墙预案
		
		Example wce = new Example(WallplanCamera.class);
		wce.createCriteria().andIn("wallplanId",ids);
		/*int count_camera = wallplanCameraMapper.countByExample(wce);
		if(count_camera>0) {
			throw new ServiceException("上墙预案关联了摄像机，不能删除！");
		}*/
		wallplanCameraMapper.deleteByExample(wce);//删除上墙预案与摄像头的关联
		
		Example shareExa = new Example(PlanShare.class);
		shareExa.createCriteria().andIn("planId", ids);
		planShareMapper.deleteByExample(shareExa);//删除共享设置
	}

	@Override
	public WallPlanVo getWallPlanById(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		List<WallPlanVo> list = wallPlanMapper.selectWallPlanList(map);
		if(list.size() == 0) {
			throw new ServiceException("上墙预案不存在！");
		}
		return list.get(0);
		/*Map<String,Object> param = new HashMap<String,Object>();
		param.put("planId", id);
		List<PlanShareVo> planShareVos = planShareMapper.selectByParam(param);
		wallPlanVo.setPlanShareVos(planShareVos);*/
		/*List<MatrixChannelVo> matrixChannels = matrixChannelMapper.selectMatrixChannelByWallId(id);
		List<CameraVo> cameras = cameraMapper.selectCamerasByWallId(id);
		Map<String,List<CameraVo>> map = new HashMap<String,List<CameraVo>>();
		for(int i=0;i<cameras.size();i++) {//cameraVo按照矩阵通道分组
			if(cameras.get(i) == null) {
				continue;
			}
			String matrixChannelId = cameras.get(i).getMatrixChannelId();
			if(map.get(matrixChannelId) == null) {
				map.put(matrixChannelId, new ArrayList<CameraVo>());
			}else {
				map.get(matrixChannelId).add(cameras.get(i));
			}
		}
		for(int i=0;i<matrixChannels.size();i++) {
			if(matrixChannels.get(i)==null) {
				continue;
			}
			matrixChannels.get(i).setCameras(map.get(matrixChannels.get(i).getId()));//设置模板下需要轮询的摄像机列表
		}
		wallPlanVo.setMatrixChannels(matrixChannels);*/
	}

	@Override
	public List<CameraVo> getWallChannelCameraList(Map<String, Object> map) {
		List<CameraVo> list = cameraMapper.selectCamerasInWallChannel(map);
		return list;
	}

	@Override
	public void saveChannelRelateCameras(String wallplanId, String channelId, List<String> cameraIdList, List<String> cameraGroupIdList, List<String> mapCameraIdList) {
        //删除预案下某个通道与摄像机之前的关联
	    Example wce = new Example(WallplanCamera.class);
		wce.createCriteria().andEqualTo("wallplanId", wallplanId).andEqualTo("matrixchannelId",channelId);
		wallplanCameraMapper.deleteByExample(wce);

		Map<String,Integer> sortMap = new HashMap<String,Integer>();
		
		//摄像机列表
		List<WallplanCamera> cameraList= new ArrayList<WallplanCamera>();
		for(String cameraId : cameraIdList) {
			WallplanCamera wallplanCamera = new WallplanCamera();
			wallplanCamera.setId(IdGenerator.nextId());
			wallplanCamera.setWallplanId(wallplanId);
			wallplanCamera.setMatrixchannelId(channelId);
			wallplanCamera.setCameraId(cameraId);
			wallplanCamera.setType(1);
			cameraList.add(wallplanCamera);
			sortMap.put(cameraId, 0);
		}

		if(cameraGroupIdList != null && cameraGroupIdList.size()>0) {
			List<CameraVo> cameraVos = cameraMapper.selectCamerasInGroups(cameraGroupIdList);
			
			//分组中的摄像机列表
	        for (CameraVo cameraVo : cameraVos) {
	            WallplanCamera wallCamera = new WallplanCamera();
	            wallCamera.setId(IdGenerator.nextId());
	            wallCamera.setWallplanId(wallplanId);
	            wallCamera.setMatrixchannelId(channelId);
	            wallCamera.setCameraId(cameraVo.getId());
	            wallCamera.setCameragroupId(cameraVo.getCameraGroupId());
	            wallCamera.setType(2);
	            cameraList.add(wallCamera);
	            sortMap.put(cameraVo.getId(), 0);
	        }
		}
        
        //地图摄像机列表
        for (String cameraId : mapCameraIdList) {
            WallplanCamera wallCamera = new WallplanCamera();
            wallCamera.setId(IdGenerator.nextId());
            wallCamera.setWallplanId(wallplanId);
            wallCamera.setMatrixchannelId(channelId);
            wallCamera.setCameraId(cameraId);
            wallCamera.setType(3);
            cameraList.add(wallCamera);
            sortMap.put(cameraId, 0);
        }

        int sort = 1;//初始序号
        for(WallplanCamera wallplanCamera : cameraList) {
        	if(sortMap.get(wallplanCamera.getCameraId()) == 0) {
        		wallplanCamera.setSort(sort);
        		sortMap.put(wallplanCamera.getCameraId(), sort);
        		sort++;
        	}else {
        		wallplanCamera.setSort(sortMap.get(wallplanCamera.getCameraId()));
        	}
        }//设置默认排序序号
        
		if(cameraList != null && cameraList.size()>0) {
			wallplanCameraMapper.insertList(cameraList);
		}

		//todo 预置位要判断已关联的不进行删除，未关联的才新增
        Example presentExample = new Example(PlanPresent.class);
        presentExample.createCriteria().andEqualTo("planId", wallplanId);
        planPresentMapper.deleteByExample(presentExample);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isKeepwatch", 1);
		map.put("cameraIds", cameraIdList);
		List<PresentPositionVo> presents = null;
		if(cameraIdList != null && cameraIdList.size()>0) {
			presents = presentPositionMapper.selectPresentPositionsInCameras(map);
		}
		if(presents!=null && presents.size()>0) {
			List<PlanPresent> planPresents = new ArrayList<PlanPresent>();
			for(PresentPositionVo present : presents) {
				PlanPresent pp = new PlanPresent();
				pp.setId(IdGenerator.nextId());
				pp.setPlanId(wallplanId);
				pp.setPresentId(present.getId());
				pp.setSort(present.getSort());
				planPresents.add(pp);
			}
			planPresentMapper.insertList(planPresents);//添加预案关联的预置位为守望位
		}
	}

	@Override
	public WallPlanVo getWallplanShares(String planId) {
		
		WallPlan wallPlan = wallPlanMapper.selectByPrimaryKey(planId);
		WallPlanVo wallPlanVo = Convert.convert(WallPlanVo.class, wallPlan);
		
		/*Map<String,Object> param = new HashMap<String, Object>();
		param.put("planId", planId);
		List<PlanShareVo> shareVos=planShareMapper.selectByParam(param);
		wallPlanVo.setPlanShareVos(shareVos);*/
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("planId", planId);
		List<DepartmentVo> deptVos = departmentMapper.selectShowDeptTreeByPlanId(map);
		wallPlanVo.setDepartmentVos(deptVos);
		return wallPlanVo;
	}

	@Override
	public List<CameraNode> getWallChannelRelatedCameras(UserVo userVo,String wallplanId, String channelId) {
		Map<String,Object> map = new HashMap<>();
        map.put("wallplanId", wallplanId);
        map.put("channelId", channelId);
        List<CameraNode> list = cameraMapper.selectCameraNode(map);
		return list;
	}

	@Override
	public WallPlanVo getWallChannelCameras(String wallplanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wallplanId", wallplanId);
		WallPlan wallplan = wallPlanMapper.selectByPrimaryKey(wallplanId);
		
		WallPlanVo wallplanVo = Convert.convert(WallPlanVo.class, wallplan);
		
		List<MatrixChannelVo> matrixChannelVos=matrixChannelMapper.selectMatrixChannelByWallId(wallplanId);
		
		List<CameraVo> cameraVos = cameraMapper.selectCamerasInWallChannel(map);
		Multimap<String,CameraVo> cameraVoMutimap=ArrayListMultimap.create(); 
		
		for(CameraVo cameraVo : cameraVos) {
			cameraVoMutimap.put(cameraVo.getMatrixChannelId(), cameraVo);
		}

		for(MatrixChannelVo matrixChannelVo : matrixChannelVos) {
			matrixChannelVo.setCameras((List<CameraVo>) cameraVoMutimap.get(matrixChannelVo.getId()));
		}
		
		wallplanVo.setMatrixChannels(matrixChannelVos);
		
		return wallplanVo;
	}

	@Override
	public void changeCameraSort(String wallplanId, String matrixchannelId, String curCameraId, String cameraId) {
		Integer curSort = 0;
		Integer changeSort = 0;
		Example curSortExa = new Example(WallplanCamera.class);
		curSortExa.createCriteria().andEqualTo("wallplanId", wallplanId).andEqualTo("matrixchannelId",matrixchannelId).andEqualTo("cameraId", curCameraId);
		List<WallplanCamera> curList = wallplanCameraMapper.selectByExample(curSortExa);
		if(curList.size()>0) {
			curSort = curList.get(0).getSort();
		}else {
			throw new ServiceException("预案下不存在该移动的摄像机,请刷新页面！");
		}
		
		Example changeSortExa = new Example(WallplanCamera.class);
		changeSortExa.createCriteria().andEqualTo("wallplanId",wallplanId).andEqualTo("matrixchannelId",matrixchannelId).andEqualTo("cameraId", cameraId);
		List<WallplanCamera> changeList = wallplanCameraMapper.selectByExample(changeSortExa);
		if(changeList.size()>0) {
			changeSort = changeList.get(0).getSort();
		}else {
			throw new ServiceException("预案下不存在移动的摄像机，请刷新页面！");
		}
		
		Example updateCurSort = new Example(WallplanCamera.class);
		updateCurSort.createCriteria().andEqualTo("wallplanId", wallplanId).andEqualTo("matrixchannelId",matrixchannelId).andEqualTo("cameraId", curCameraId);
		WallplanCamera wallplanCamera = new WallplanCamera();
		wallplanCamera.setSort(changeSort);
		wallplanCameraMapper.updateByExampleSelective(wallplanCamera, updateCurSort);
		
		Example updateChangeSort = new Example(WallplanCamera.class);
		updateChangeSort.createCriteria().andEqualTo("wallplanId", wallplanId).andEqualTo("matrixchannelId",matrixchannelId).andEqualTo("cameraId", cameraId);
		WallplanCamera changeWallplanCamera = new WallplanCamera();
		changeWallplanCamera.setSort(curSort);
		wallplanCameraMapper.updateByExampleSelective(changeWallplanCamera, updateChangeSort);
	}
}
