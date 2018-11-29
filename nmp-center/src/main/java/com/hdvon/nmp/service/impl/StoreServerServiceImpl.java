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
import com.hdvon.nmp.service.IStoreServerService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <br>
 * <b>功能：</b>存储服务器Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class StoreServerServiceImpl implements IStoreServerService {

	@Autowired
	private StoreServerMapper storeServerMapper;
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private StoreserverMappingMapper storeserverMappingMapper;
	
	@Autowired
	private StoreserverCameraMapper storeserverCameraMapper;
	
	@Autowired
	private CameraMapper cameraMapper;
	
	@Autowired
	private TimingVedioMapper timingVedioMapper;

	@Autowired
	private DeviceMapper deviceMapper;

	@Override
	public void saveStoreServer(UserVo userVo, StoreServerParamVo storeServerParamVo, List<String> projectIds) {
		StoreServer storeServer = Convert.convert(StoreServer.class, storeServerParamVo);
		Example pojExample = new Example(Project.class);
		pojExample.createCriteria().andIn("id", projectIds);
		List<Project> projects = projectMapper.selectByExample(pojExample);
		if(projects == null || projects.size()==0) {
			throw new ServiceException("存储服务器不存在所属项目！");
		}
		
		Example nameExa = new Example(StoreServer.class);
		if(StrUtil.isBlank(storeServerParamVo.getId())) {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", storeServerParamVo.getName()).andNotEqualTo("enabled",-1);
			int countName = storeServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("存储服务器名称已存在！");
			}
			
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", storeServerParamVo.getCode()).andNotEqualTo("enabled",-1);
			int countCode = storeServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("存储服务器编号已经存在！");
			}
			
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", storeServerParamVo.getIp()).andNotEqualTo("enabled",-1);
			int countIp = storeServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("存储服务器ip已经存在！");
			}
			
			/*Example domainExa = new Example(TranspondServer.class);
			domainExa.createCriteria().andEqualTo("domainName", transpondServerParamVo.getDomainName()).andNotEqualTo("enabled",-1);
			int countDomain = transpondServerMapper.selectCountByExample(domainExa);
			if(countDomain > 0) {
				throw new ServiceException("存储服务器域名已经存在！");
			}*/
			storeServer.setId(IdGenerator.nextId());
			storeServer.setServerStatus(0);//默认离线0
			storeServer.setDefaultDays(30);//TODO 默认30天
			storeServer.setCreateTime(new Date());
			storeServer.setCreateUser(userVo.getAccount());
			storeServer.setUpdateTime(new Date());
			storeServer.setUpdateUser(userVo.getAccount());
			storeServerMapper.insertSelective(storeServer);
		}else {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", storeServerParamVo.getName()).andNotEqualTo("id", storeServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countName = storeServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("存储服务器名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", storeServerParamVo.getCode()).andNotEqualTo("id", storeServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countCode = storeServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("存储服务器编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", storeServerParamVo.getIp()).andNotEqualTo("id", storeServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countIp = storeServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("存储服务器ip已经存在！");
			}

			/*Example domainExa = new Example(TranspondServer.class);
			domainExa.createCriteria().andEqualTo("domainName", transpondServerParamVo.getDomainName()).andNotEqualTo("id", transpondServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countDomain = transpondServerMapper.selectCountByExample(domainExa);
			if(countDomain > 0) {
				throw new ServiceException("存储服务器域名已经存在！");
			}*/
			storeServer.setUpdateTime(new Date());
			storeServer.setUpdateUser(userVo.getAccount());
			storeServerMapper.updateByPrimaryKeySelective(storeServer);
			
			Example delPojExample = new Example(StoreserverMapping.class);
			delPojExample.createCriteria().andEqualTo("storeserverId",storeServer.getId());
			storeserverMappingMapper.deleteByExample(delPojExample);//删除未修改前关联的所有项目
		}
		List<StoreserverMapping> storeserverMappings = new ArrayList<StoreserverMapping>();
		for(Project project : projects) {
			StoreserverMapping storeserverMapping = new StoreserverMapping();
			storeserverMapping.setId(IdGenerator.nextId());
			storeserverMapping.setStoreserverId(storeServer.getId());
			storeserverMapping.setProjectId(project.getId());
			storeserverMapping.setProjectName(project.getName());
			storeserverMapping.setProjectCode(project.getCode());
			storeserverMappings.add(storeserverMapping);
		}
		storeserverMappingMapper.insertList(storeserverMappings);
		
	}

	@Override
	public void delStoreServersByIds(UserVo userVo, List<String> ids) {
		// 启用不能删除
		Example example = new Example(StoreServer.class);
		example.createCriteria().andIn("id",ids).andEqualTo("enabled",1);
		int count = storeServerMapper.selectCountByExample(example);
		if (count > 0){
			throw new ServiceException("存储服务器正在启用，不能删除！");
		}

		for(String id : ids){
			StoreServer storeServer = new StoreServer();
			storeServer.setId(id);
			storeServer.setEnabled(-1);
			storeServerMapper.updateByPrimaryKeySelective(storeServer);
		}
	}

	@Override
	public StoreServerVo getStoreServerById(String storeserverId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",storeserverId);
		StoreServerVo storeServerVo = storeServerMapper.selectStoreServerByParam(map);

		if(storeServerVo != null){
            map.clear();
            map.put("storeserverId", storeserverId);
            List<StoreserverMappingVo> storeserverMappingVos = storeserverMappingMapper.selectByParam(map);
            storeServerVo.setStoreserverMappingVos(storeserverMappingVos);
        }
		return storeServerVo;
	}

	@Override
	public PageInfo<StoreServerVo> getStoreServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, StoreServerParamVo storeServerParamVo) {
		
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", storeServerParamVo.getName());
        map.put("code", storeServerParamVo.getCode());
        map.put("ip", storeServerParamVo.getIp());
        map.put("serverStatus", storeServerParamVo.getServerStatus());
        map.put("enabled", storeServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<StoreServerVo> storeServerVos = storeServerMapper.selectStoreServersList(map);
		

		if(storeServerVos!=null && storeServerVos.size()>0) {
			List<String> storeServerIds = new ArrayList<String>();
			for(int i=0;i<storeServerVos.size();i++) {
				if(storeServerVos.get(i) == null) {
					continue;
				}
				storeServerIds.add(storeServerVos.get(i).getId());
			}
			if(storeServerIds.size()>0) {
				Multimap<String,StoreserverMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<StoreserverMappingVo> storeserverMappingVos = storeserverMappingMapper.selectProjectByStoreserverIds(storeServerIds);
				for(StoreserverMappingVo storeserverMappingVo : storeserverMappingVos) {
					projectMutimap.put(storeserverMappingVo.getStoreserverId(), storeserverMappingVo);
				}
				for(StoreServerVo storeServerVo : storeServerVos) {
					storeServerVo.setStoreserverMappingVos((List<StoreserverMappingVo>) projectMutimap.get(storeServerVo.getId()));
				}
			}
		}
		
		return new PageInfo<StoreServerVo>(storeServerVos);
	}

	@Override
	public List<StoreServerVo> getStoreServerList(TreeNodeChildren treeNodeChildren, StoreServerParamVo storeServerParamVo) {
		 Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", storeServerParamVo.getName());
        map.put("code", storeServerParamVo.getCode());
        map.put("ip", storeServerParamVo.getIp());
        map.put("serverStatus", storeServerParamVo.getServerStatus());
        map.put("enabled", storeServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
	        
		List<StoreServerVo> storeServerVos = storeServerMapper.selectStoreServersList(map);
		
		if(storeServerVos!=null && storeServerVos.size()>0) {
			List<String> storeServerIds = new ArrayList<String>();
			for(int i=0;i<storeServerVos.size();i++) {
				if(storeServerVos.get(i) == null) {
					continue;
				}
				storeServerIds.add(storeServerVos.get(i).getId());
			}
			if(storeServerIds.size()>0) {
				Multimap<String,StoreserverMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<StoreserverMappingVo> storeserverMappingVos = storeserverMappingMapper.selectProjectByStoreserverIds(storeServerIds);
				for(StoreserverMappingVo storeserverMappingVo : storeserverMappingVos) {
					projectMutimap.put(storeserverMappingVo.getStoreserverId(), storeserverMappingVo);
				}
				for(StoreServerVo storeServerVo : storeServerVos) {
					storeServerVo.setStoreserverMappingVos((List<StoreserverMappingVo>) projectMutimap.get(storeServerVo.getId()));
				}
			}
		}
		
		return storeServerVos;
	}

	@Override
	public List<CameraNode> getStoreserverCamera(UserVo userVo, String storeServerId) {
        Map<String,Object> map = new HashMap<>();
        map.put("isAdmin",userVo.isAdmin());
        map.put("userId",userVo.getId());
        if(storeServerId == null){
            storeServerId = "";
        }
        map.put("storeServerId",storeServerId);
        return cameraMapper.selectCameraNode(map);
	}

	@Override
	public void relateCamerasToStoreserver(UserVo userVo, String storeserverId, List<String> cameraIdList) {
        //todo 增加非管理员摄像机权限过滤(已经实现该功能，留下todo方便查找)
        Example delExa = new Example(StoreserverCamera.class);
        Example.Criteria criteria = delExa.createCriteria().andEqualTo("storeserverId", storeserverId);

        //非管理员摄像机权限过滤
        if(!userVo.isAdmin()){
            //查询有权限的摄像机id
            List<String> authCameraIds = cameraMapper.selectAuthCameraIds(userVo.isAdmin(),userVo.getId());
            //增加删除条件，避免删除无权限数据
            criteria.andIn("cameraId",authCameraIds);
            //cameraIds取权限交集!
            cameraIdList.retainAll(authCameraIds);
        }
        //todo 删除需要根据权限进行过滤(已经实现该功能，留下todo方便查找)
        storeserverCameraMapper.deleteByExample(delExa);

		StoreServer storeServer = storeServerMapper.selectByPrimaryKey(storeserverId);
        List<StoreserverCamera> list = new ArrayList<StoreserverCamera>();
        for (String cameraId : cameraIdList) {
            StoreserverCamera storeserverCamera = new StoreserverCamera();
            storeserverCamera.setId(IdGenerator.nextId());
            storeserverCamera.setStoreserverId(storeserverId);
            storeserverCamera.setCameraId(cameraId);
            storeserverCamera.setKeepDays(storeServer.getDefaultDays());
            list.add(storeserverCamera);
        }
        if(list.size() > 0){
            storeserverCameraMapper.insertList(list);
        }
	}
	
	@Override
	public PageInfo<StoreserverCameraVo> getUserCamerasByStoreserverId(UserVo userVo,PageParam pp, StoreserverCameraVo storeserverCameraVo) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());

        Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userVo.getId());
        map.put("id", storeserverCameraVo.getId());
        map.put("storeserverId", storeserverCameraVo.getStoreserverId());
        map.put("cameraName", storeserverCameraVo.getCameraName());
        map.put("cameraNo", storeserverCameraVo.getCameraNo());
        map.put("cameraType", storeserverCameraVo.getCameraType());
        map.put("status", storeserverCameraVo.getStatus());
		map.put("isAdmin", userVo.isAdmin());

		List<StoreserverCameraVo> list = storeserverCameraMapper.selectUserStoreserverCamera(map);
		return new PageInfo<StoreserverCameraVo>(list);
	}

	@Override
	public void delRelatedCamerasByIds(UserVo userVo, String storeserverId, List<String> cameraIds) {
        Example delExa = new Example(StoreserverCamera.class);
        Example.Criteria criteria = delExa.createCriteria().andEqualTo("storeserverId", storeserverId);

        //非管理员摄像机权限过滤
        if(!userVo.isAdmin()){
            //查询有权限的摄像机id
            List<String> authCameraIds = cameraMapper.selectAuthCameraIds(userVo.isAdmin(),userVo.getId());

            //cameraIds取权限交集!
            cameraIds.retainAll(authCameraIds);
        }
        criteria.andIn("cameraId",cameraIds);
        storeserverCameraMapper.deleteByExample(delExa);
	}

    @Override
    public List<CameraPermissionVo> getStoreserverAddressCameraPermission(Map<String, Object> map) {
        return null;
    }

    private String getDayOfWeek(Calendar c) {
 	   if(Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK)){
	    return "1";
	   }
	   if(Calendar.TUESDAY == c.get(Calendar.DAY_OF_WEEK)){
	    return "2";
	   }
	   if(Calendar.WEDNESDAY == c.get(Calendar.DAY_OF_WEEK)){
	    return "3";
	   }
	   if(Calendar.THURSDAY == c.get(Calendar.DAY_OF_WEEK)){
	    return "4";
	   }
	   if(Calendar.FRIDAY == c.get(Calendar.DAY_OF_WEEK)){
	    return "5";
	   }
	   if(Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)){
	    return "6";
	   }
	   if(Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)){
	    return "7";
	   }
		return "1";
	}
	@Override
	public void saveTimingVedioSet(UserVo userVo, TimingVedioParamVo timingVedioParamVo, Integer keepDays) {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat yyyyMMddhhMMss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,List<String>> map = timingVedioParamVo.getWeekSet();//设置一周中每天对应保存录像的时间段
		
		Example countTimingVedioExa = new Example(TimingVedio.class);
		countTimingVedioExa.createCriteria().andEqualTo("storeCameraId", timingVedioParamVo.getStoreCameraId());
		int countStorecameraTiming = timingVedioMapper.selectCountByExample(countTimingVedioExa);
		
		Calendar calendar = null;
		String curDayOfWeek = null;
		Date curDate = null;
		if(countStorecameraTiming > 0) {//已经设置了定时录像，开始日期已经有了，不是当前日期
			StoreserverCamera storeserverCamera = storeserverCameraMapper.selectByPrimaryKey(timingVedioParamVo.getStoreCameraId());
			String curDateStr = storeserverCamera.getBgnDate();
			try {
				curDate = yyyyMMddhhMMss.parse(curDateStr+" "+"00:00:00");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar = Calendar.getInstance();
			calendar.setTime(curDate);
			
			calendar.get(Calendar.DAY_OF_WEEK);
			curDayOfWeek = getDayOfWeek(calendar);
		}else {
			Date now = new Date();
			calendar = Calendar.getInstance();
			calendar.setTime(now);
			// 将时分秒,毫秒域清零
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			
			calendar.get(Calendar.DAY_OF_WEEK);
			curDayOfWeek = getDayOfWeek(calendar);
			curDate = calendar.getTime();
		}
		
		
		Map<String,List<String>> dayMap = new HashMap<String,List<String>>();
		
		//当前日期
		List<String> curDayList = new ArrayList<String>();
		curDayList.add(yyyyMMdd.format(curDate));
		dayMap.put(curDayOfWeek, curDayList);
		
		//除去第一天，每次增加一天，逐次增加keepDays-1次，获取每天的日期对应的星期
		for(int i=1;i<keepDays;i++) {
			calendar.add(calendar.DATE, 1);
			calendar.get(Calendar.DAY_OF_WEEK);
			String dayOfWeek = getDayOfWeek(calendar);
			Date date = calendar.getTime();
			if(dayMap.get(dayOfWeek) != null) {
				List<String> list = dayMap.get(dayOfWeek);
				list.add(yyyyMMdd.format(date));
				dayMap.put(dayOfWeek, list);
			}else {
				List<String> dayList = new ArrayList<String>();
				dayList.add(yyyyMMdd.format(date));
				dayMap.put(dayOfWeek, dayList);
			}
		}
		
		List<TimingVedio> list = new ArrayList<TimingVedio>();
		for(Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			
			List<String> dayList = dayMap.get(key);
			if(dayList != null) {
				for(String dayStr : dayList) {
					for(int i=0;i<value.size();i++) {
						String timingLeg = value.get(i);
						if(timingLeg != null) {
							String[] timingLegArr = timingLeg.split("-");
							if(timingLegArr.length != 2) {
								throw new ServiceException("参数格式不对,请检查！");
							}
							Date bgnTime = null;
							Date endTime = null;
							try {
								bgnTime = yyyyMMddhhMMss.parse(dayStr + " " +timingLegArr[0]);
								endTime = yyyyMMddhhMMss.parse(dayStr + " " +timingLegArr[1]);
							} catch (ParseException e) {
								throw new ServiceException("参数格式不对,请检查！");
							}
							
							TimingVedio timingVedio = new TimingVedio();
							timingVedio.setId(IdGenerator.nextId());
							timingVedio.setDayOfWeek(Integer.parseInt(key));
							timingVedio.setStoreCameraId(timingVedioParamVo.getStoreCameraId());
							timingVedio.setBgnTime(bgnTime);
							timingVedio.setEndTime(endTime);
							timingVedio.setCreateTime(new Date());
							timingVedio.setCreateUser(userVo.getAccount());
							timingVedio.setUpdateTime(new Date());
							timingVedio.setUpdateUser(userVo.getAccount());
							list.add(timingVedio);
						}
					}
				}
			}
		}
		
		if(countStorecameraTiming == 0) {//没有设置定时录像，需要设置定时录像开始日期
			StoreserverCamera storeserverCamera = new StoreserverCamera();
			storeserverCamera.setId(timingVedioParamVo.getStoreCameraId());
			storeserverCamera.setBgnDate(yyyyMMdd.format(curDate));
			storeserverCameraMapper.updateByPrimaryKeySelective(storeserverCamera);//设置定时录像开始日期
		}
		
		Example delTimingVedioExa = new Example(TimingVedio.class);
		delTimingVedioExa.createCriteria().andEqualTo("storeCameraId", timingVedioParamVo.getStoreCameraId());
		timingVedioMapper.deleteByExample(delTimingVedioExa);//删除已经设置的定时记录
		if(list.size()>0) {
			timingVedioMapper.insertList(list);//添加新的录像定时设置记录
		}		
	}

	@Override
	public List<TimingVedioResultVo> getStoreCameraTimingSet(String storeCameraId) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Example timingVedioExa = new Example(TimingVedio.class);
		timingVedioExa.createCriteria().andEqualTo("storeCameraId",storeCameraId);
		List<TimingVedio> list = timingVedioMapper.selectByExample(timingVedioExa);//存储服务器关联的单个摄像机一周的录像定时设置
		
		List<TimingVedioResultVo> result = new ArrayList<TimingVedioResultVo>();
		
		Map<Integer,List<String>> map = new HashMap<Integer,List<String>>();
		for(TimingVedio timingVedio : list) {
			Date bgnTime = timingVedio.getBgnTime();
			Date endTime = timingVedio.getEndTime();
			String bgnTimeStr = sdf.format(bgnTime);
			String endTimeStr = sdf.format(endTime);
			List<String> legList = map.get(timingVedio.getDayOfWeek());
			if(legList == null) {
				legList = new ArrayList<String>();
				legList.add(bgnTimeStr+"-"+endTimeStr);
				map.put(timingVedio.getDayOfWeek(), legList);
			}else {
				if(!legList.contains(bgnTimeStr+"-"+endTimeStr)) {
					legList.add(bgnTimeStr+"-"+endTimeStr);
				}
			}
		}
		for(Map.Entry<Integer, List<String>> entry : map.entrySet()) {
			Integer key = entry.getKey();
			List<String> timeLegs = entry.getValue();
			TimingVedioResultVo resultVo = new TimingVedioResultVo();
			resultVo.setDayOfWeek(key);
			
			List<TimeingVedioLegVo> legList = new ArrayList<>();//周中每天的所有起止时间段设置
			for(String timeLeg : timeLegs) {
				TimeingVedioLegVo legVo = new TimeingVedioLegVo();
				String[] bgnEndTimeStr = timeLeg.split("-");
				String[] bgnTimeArr = bgnEndTimeStr[0].split(":");
				String[] endTimeArr = bgnEndTimeStr[1].split(":");
				int bgnSeconds = Integer.parseInt(bgnTimeArr[0]) * 3600+Integer.parseInt(bgnTimeArr[1]) * 60+Integer.parseInt(bgnTimeArr[2]);
				int endSeconds = Integer.parseInt(endTimeArr[0]) * 3600+Integer.parseInt(endTimeArr[1]) * 60+Integer.parseInt(endTimeArr[2]);
				legVo.setBgnTime(bgnSeconds);
				legVo.setEndTime(endSeconds);
				legList.add(legVo);
			}//周中每天中每段的起止时间转成TimeingVedioLegVo
			
			resultVo.setVedioLegVos(legList);
			result.add(resultVo);
		}
		return result;
	}

	@Override
	public StoreserverCameraVo getStoreServerCameraByCameraId(String cameraId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", cameraId);
		List<StoreserverCameraVo> list = storeserverCameraMapper.selectUserStoreserverCamera(map);
		if(list !=null && list.size() == 1){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveStoreServerCamera(String[] storeCameraIds, String keepDays) {

		for(String id: storeCameraIds){
			// 保存 存储服务器摄像机关联表
			StoreserverCamera storeserverCamera = new StoreserverCamera();
			storeserverCamera.setId(id);
			storeserverCamera.setKeepDays(StrUtil.isNotBlank(keepDays) ? Integer.valueOf(keepDays) : 0);
			storeserverCameraMapper.updateByPrimaryKeySelective(storeserverCamera);

			// 保存设备 t_device
			DeviceVo deviceVo =deviceMapper.selectDeviceBystoreCameraId(id);
			Device device = Convert.convert(Device.class, deviceVo);
			device.setLxbcts(Integer.valueOf(keepDays));
			deviceMapper.updateByPrimaryKeySelective(device);
		}
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		
		return storeServerMapper.selectMaxCodeBycode(map);
	}

	@Override
	public List<Map<String, Object>> getCameraByStoreId(UserVo userVo,String storeserverId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userVo.getId());
		map.put("storeserverId", storeserverId);
		map.put("isAdmin", userVo.isAdmin());

		List<Map<String,Object>> list=deviceMapper.selectDeviceByStorageId(map);
		return list;
	}
}
