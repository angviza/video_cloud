package com.hdvon.nmp.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.entity.Camera;
import com.hdvon.nmp.entity.CameraCameragroup;
import com.hdvon.nmp.entity.CameraMapping;
import com.hdvon.nmp.entity.Device;
import com.hdvon.nmp.importdata.ImportCamera;
import com.hdvon.nmp.mapper.CameraCameragroupMapper;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.mapper.CameraMappingMapper;
import com.hdvon.nmp.mapper.DeviceMapper;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraCameragroupVo;
import com.hdvon.nmp.vo.CameraParamVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.UserDeviceParamVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;


/**
 * <br>
 * <b>功能：</b>摄像机表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraServiceImpl implements ICameraService{

	@Autowired
	private CameraMapper cameraMapper;
	@Autowired
	private CameraMappingMapper cameraMappingMapper;
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private CameraCameragroupMapper cameraCameragroupMapper;
	

	@Override
	public CameraVo getCameraInfo(String id) {
		Camera vo=cameraMapper.selectByPrimaryKey(id);
		return Convert.convert(CameraVo.class, vo);
	}

	@Override
	public String delCameras(List<String> idList) {
		String deviceIds="";
		Example camera = new Example(Camera.class);
		Example cameraMapping = new Example(CameraMapping.class);
		Example device=new Example(Device.class);
		Example cameraCameragroup= new Example(CameraCameragroup.class);
		
		camera.createCriteria().andIn("id",idList);
		cameraMapping.createCriteria().andIn("cameraId",idList);
		cameraCameragroup.createCriteria().andIn("cameraId",idList);
		List<Camera> list=cameraMapper.selectByExample(camera);
		if(list.isEmpty()) {
			return null;
		}
		List<String> ids= list.stream().map(vo->vo.getDeviceId()).collect(Collectors.toList());
		deviceIds = String.join(",", ids);
		
		
		if(ids.size()!=0 || ids !=null) {
			device.createCriteria().andIn("id",ids); 
			deviceMapper.deleteByExample(device);//摄像机设备
		}
	    cameraMapper.deleteByExample(camera);//摄像机
		cameraMappingMapper.deleteByExample(cameraMapping);//中间关联表
		cameraCameragroupMapper.deleteByExample(cameraCameragroup);//关联分组
		if(deviceIds.length() > 3) {
			deviceIds.substring(0, deviceIds.length()-1);
		}
		return deviceIds;
		
	}

	@Override
	public PageInfo<CameraVo> getCameraByPage(CameraParamVo vo, TreeNodeChildren treeNodeChildren, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		vo.setAddressNodeIds(treeNodeChildren.getAddressNodeIds());
		vo.setOrgNodeIds(treeNodeChildren.getOrgNodeIds());
		vo.setProjectNodeIds(treeNodeChildren.getProjectNodeIds());
		vo.setGroupNodeIds(treeNodeChildren.getGroupNodeIds());
		List<CameraVo> list=cameraMapper.selectCameraByMapPage(vo);
		return new PageInfo<CameraVo>(list);
	}

	//设置分组
	@Override
	public void setGroup(List<String> cameraIds, String groupId) {
		List<CameraCameragroup> list=new ArrayList<CameraCameragroup>();
		for(String cameraId :cameraIds ) {
			if(StrUtil.isNotBlank(cameraId)) {
				CameraCameragroup ccGroup=new CameraCameragroup();
				ccGroup.setCameragroupId(groupId.trim());
				ccGroup.setId(IdGenerator.nextId());
				ccGroup.setCameraId(cameraId.trim());
				list.add(ccGroup);
			}
			
		}
		cameraCameragroupMapper.insertList(list);
	}

	//移除分组
	@Override
	public void delCameracroup(List<String> idList) {
		Example example= new Example(CameraCameragroup.class);
		example.createCriteria().andIn("cameraId",idList);
		cameraCameragroupMapper.deleteByExample(example);
		
	}

	@Override
	public List<CameraCameragroupVo> getCamerasByGroupIds(List<String> groupIds) {
		/*Example example= new Example(CameraCameragroup.class);
		example.createCriteria().andIn("cameragroupId",groupIds);
		List<CameraCameragroup> entity = cameraCameragroupMapper.selectByExample(example);
		List<CameraCameragroupVo> list=BeanHelper.convertList(CameraCameragroupVo.class, entity);*/
		List<CameraCameragroupVo> list = cameraCameragroupMapper.selectCamerasByGroupIds(groupIds);
		return list;
	}

	@Override
	public List<UserDeviceParamVo> getUserCameraPermission(Map<String, Object> param) {
		return cameraMapper.selectUserCameraPermission(param);
	}


	@Override
	public List<CameraVo> getCameraByDeviceId(String deviceId) {
	    Map<String,Object> map = new HashMap<>();
	    map.put("deviceId",deviceId);
		List<CameraVo> cameraVos = cameraMapper.selectCameraByParam(map);
		return cameraVos;
	}

	@Override
	public List<CameraVo> getCameraBySbbm(String sbbm) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sbbm", sbbm);
		List<CameraVo> list=cameraMapper.selectCameraByParam(map);
		return list;
	}

	@Override
	public String batchInsertCameras(List<Map<String, String>> list, List<CheckAttributeVo> attrs, UserVo userVo, Map<String,List<String>> relateIdMap) {
		String deviceIds = "";
		if(list.size() > 0) {
			ImportCamera importCamera = transMapToCameras(list, attrs, userVo, relateIdMap);
			List<Device> devices = importCamera.getDevices();
			List<Camera> cameras = importCamera.getCameras();
			List<CameraMapping> cameraMappings = importCamera.getCameraMappings();
			if(devices != null && devices.size()>0) {
				deviceMapper.insertList(devices);
				cameraMapper.insertList(cameras);
				cameraMappingMapper.insertList(cameraMappings);
			}
			deviceIds = importCamera.getDeviceIds();
		}
		return deviceIds;
	}
	private ImportCamera transMapToCameras(List<Map<String, String>> list,List<CheckAttributeVo> attrs, UserVo userVo, Map<String,List<String>> relateIdMap){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer deviceIds = new StringBuffer();
		List<Device> devices = new ArrayList<Device>();
		List<Camera> cameras = new ArrayList<Camera>();
		List<CameraMapping> cameraMappings = new ArrayList<CameraMapping>();
		
		List<String> xzqhIdList = relateIdMap.get("xzqhIdList");
		List<String> addressIdList = relateIdMap.get("addressIdList");
		List<String> encoderIdList = relateIdMap.get("encoderIdList");
		List<String> bussIdList = relateIdMap.get("bussIdList");
		List<String> projectIdList = relateIdMap.get("projectIdList");
		
		for(int i=0;i<list.size();i++) {
			Map<String,String> map = list.get(i);
			
			Class<Device> clzz = null;
			Device device = null;
			try {
				clzz = (Class<Device>) Class.forName("com.hdvon.nmp.entity.Device");
				device = clzz.newInstance();
				device.setId(IdGenerator.nextId());

				for(int j=0;j<attrs.size();j++) {
					CheckAttributeVo checkVo = attrs.get(j);
					if(checkVo.isMapping()) {
						continue;
					}
					String attr = checkVo.getAttr();
					String methodAttr = attr.substring(0, 1).toUpperCase()+checkVo.getAttr().substring(1);
					Method getMethod = clzz.getMethod("get"+methodAttr);
					Type type = getMethod.getGenericReturnType();
					if("class java.lang.Integer".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Integer.class);
						setMethod.invoke(device,  StrUtil.isBlank(map.get(attr))?null:Integer.parseInt(map.get(attr)));
					}else if("class java.lang.Double".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Double.class);
						setMethod.invoke(device, StrUtil.isBlank(map.get(attr))?null:Double.parseDouble(map.get(attr)));
					}else if("class java.lang.String".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, String.class);
						setMethod.invoke(device, map.get(attr));
					}else if("class java.util.Date".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Date.class);
						JSONObject valid = checkVo.getValid();
						if(valid != null) {
							String date = valid.getString("DATE");
							if(StrUtil.isNotBlank(date)) {
								JSONArray formatArray = (JSONArray) JSONObject.parse(date);
								if(formatArray != null && formatArray.size()>0) {
									SimpleDateFormat format = new SimpleDateFormat(formatArray.getString(0));
									try {
										setMethod.invoke(device, StrUtil.isNotBlank(map.get(attr)) ? format.parse(map.get(attr)) : null);
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
				
			device.setBussGroupId(bussIdList.get(i));
			
			Camera camera = new Camera();
			camera.setId(IdGenerator.nextId());
			camera.setDeviceId(device.getId());
			
			camera.setCreateTime(new Date());
			camera.setCreateUser(userVo.getAccount());
			camera.setUpdateTime(new Date());
			camera.setUpdateUser(userVo.getAccount());
			
			CameraMapping cameraMapping = new CameraMapping();
			cameraMapping.setId(IdGenerator.nextId());
			cameraMapping.setCameraId(camera.getId());
			cameraMapping.setOrgId(xzqhIdList.get(i));
			cameraMapping.setAddressId(addressIdList.get(i));
			cameraMapping.setEncoderServerId(encoderIdList.get(i));
			cameraMapping.setProjectId(projectIdList.get(i));
			
			//用于kafka推送消息的参数
			deviceIds.append(device.getId());
			if(i<list.size()-1) {
				deviceIds.append(",");
			}
			
			devices.add(device);
			cameras.add(camera);
			cameraMappings.add(cameraMapping);
		}
		ImportCamera importCamera = new ImportCamera();
		importCamera.setDeviceIds(deviceIds.toString());
		importCamera.setDevices(devices);
		importCamera.setCameras(cameras);
		importCamera.setCameraMappings(cameraMappings);
		return importCamera;
	}

	@Override
	public List<Map<String,Object>> getCamerasByGroupId(String groupId) {
		List<DeviceVo> list = deviceMapper.selectCamerasByGroupId(groupId);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++) {
			DeviceVo deviceVo = list.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("sbbm", deviceVo.getSbbm());
			map.put("sbmc", deviceVo.getSbmc());
			map.put("registeredName", deviceVo.getRegisteredName());
			map.put("registeredPass", deviceVo.getRegisteredPass());
			map.put("registeredPort", deviceVo.getRegisteredServerPort());
			result.add(map);
		}
		return result;
	}

	@Override
	public List<DeviceVo> getcameraByCodes(List<String> cameraCodes) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceCodes", cameraCodes);
		List<DeviceVo> deviceVos = deviceMapper.selectByParam(map);
		return deviceVos;
	}

	@Override
	public List<CameraVo> getCameraByIds(List<String> ids) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ids", ids);
		List<CameraVo> list = new ArrayList<CameraVo>();
		if(ids != null && ids.size()>0) {
			list = cameraMapper.selectCameraByParam(map);
		}		
		return list;
	}
	
	@Override
	public List<String> getDeviceIdByCamearId(Map<String, Object> param) {
		return cameraMapper.selectDeviceIdByCamearId(param);
	}
}
