package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Address;
import com.hdvon.nmp.entity.Camera;
import com.hdvon.nmp.entity.CameraMapping;
import com.hdvon.nmp.entity.Device;
import com.hdvon.nmp.entity.EncoderServer;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.AddressMapper;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.mapper.CameraMappingMapper;
import com.hdvon.nmp.mapper.DepartmentMapper;
import com.hdvon.nmp.mapper.DeviceMapper;
import com.hdvon.nmp.mapper.EncoderServerMapper;
import com.hdvon.nmp.mapper.OrganizationMapper;
import com.hdvon.nmp.mapper.ProjectMapper;
import com.hdvon.nmp.service.IDeviceService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraParamVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DeviceParamVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.OrganizationVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>一机一档Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class DeviceServiceImpl implements IDeviceService {

	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private CameraMapper cameraMapper;
	@Autowired
	private CameraMappingMapper cameraMappingMapper;
	@Autowired
	private DepartmentMapper departmentMapper;
	@Autowired
	private OrganizationMapper organizationMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private EncoderServerMapper encoderServerMapper;
	@Autowired
	private AddressMapper addressMapper;
	

	@Override
	public String editDevice(UserVo userVo, DeviceParamVo vo){
		String deviceId=null;
		if(StrUtil.isBlank(vo.getSbbm())) {
			throw new ServiceException("摄像机设备编号不能为空！");
		}else {
			if( vo.getSbbm().length() !=20) {
				throw new ServiceException("摄像机设备编号必须为20位！");
			}else {
				Example device=new Example(Device.class);
				Example.Criteria de=device.createCriteria();
				de.andEqualTo("sbbm",vo.getSbbm());
				if(StrUtil.isNotBlank(vo.getId())) {
					de.andNotEqualTo("id",vo.getId());
				}
				int count = deviceMapper.selectCountByExample(device);
				if(count >0) {
					throw new ServiceException("摄像机设备编号已重复！");
				}
			}
		}
		
		if(StrUtil.isBlank(vo.getProjectId())) {
			throw new ServiceException("摄像机项目不能为空！");
		}else {
			Project projece=projectMapper.selectByPrimaryKey(vo.getProjectId());
			if(projece ==null) {
				throw new ServiceException("选择的项目不存在！");
			}else {
				vo.setSsxmbh(projece.getCode());//所属项目编号
				vo.setSsxmmc(projece.getName());//所属项目名称
			}
			//项目关联的部门列表
			/*List<DepartmentVo> department = departmentMapper.selectDepartMentList(vo.getProjectId());
			if(department.size()>0) {
				if("1".equals(department.get(0).getIsBuilder())) {//建设单位
					vo.setJsdw(department.get(0).getId());
					vo.setJsdwdm(department.get(0).getDepCode());//
				}
				if("1".equals(department.get(0).getIsConstructor())) {//承建单位
					vo.setCjdw(department.get(0).getId());
				}
			}*/
		}
		//行政区划
		OrganizationVo orgVo = organizationMapper.selectOrgById(vo.getOrgId());
		if(orgVo !=null) {
			vo.setXzqh(orgVo.getOrgCode());
			vo.setParentId(orgVo.getParentOrgName());
		}else {
			throw new ServiceException("选择的行政区划不存在！");
		}
		//编码器
		if(StrUtil.isNotBlank(vo.getEncoderServerId())) {
			EncoderServer encoderServer=encoderServerMapper.selectByPrimaryKey(vo.getEncoderServerId());
			if(null==encoderServer) {
				throw new ServiceException("选择的编码器不存在！");
			}
			vo.setLxccsbip(encoderServer.getIp());//录像存储设备ip
		}
		//地址
		if(StrUtil.isNotBlank(vo.getAddressId())) {
			Address address=addressMapper.selectByPrimaryKey(vo.getAddressId());
			if(null ==address ) {
				throw new ServiceException("选择的地址不存在！");
			}
		}
		
		Device device = Convert.convert(Device.class,vo);
		device.setBussGroupId(vo.getBusinessGroupId());
		Camera camera = new Camera();
		CameraMapping cameraMapping=new CameraMapping();
		device.setIpv4(vo.getCameraIp().trim());//前端过时是camaraip
		if(StrUtil.isNotBlank(vo.getSbbm())) {
			device.setSbbm(vo.getSbbm().trim());
		}
		if(StrUtil.isNotBlank(vo.getAddressId())) {
			cameraMapping.setAddressId(vo.getAddressId().trim());
		}else {
			cameraMapping.setAddressId(vo.getAddressId());
		}
        if(StrUtil.isNotBlank(vo.getEncoderServerId())) {
        	cameraMapping.setEncoderServerId(vo.getEncoderServerId().trim());
		}else {
			cameraMapping.setEncoderServerId(vo.getEncoderServerId());
		}
        if(StrUtil.isNotBlank(vo.getProjectId())) {
        	cameraMapping.setProjectId(vo.getProjectId().trim());
        }else {
        	cameraMapping.setProjectId(vo.getProjectId());
        }
        if(StrUtil.isNotBlank(vo.getOrgId())) {
        	cameraMapping.setOrgId(vo.getOrgId().trim());//行政区划
        }else {
        	cameraMapping.setOrgId(vo.getOrgId());//行政区划
        }
        
        camera.setAccessMode(vo.getAccessMode());
        camera.setRegisteredName(vo.getRegisteredName());
        camera.setRegisteredPass(vo.getRegisteredPass());
        camera.setRegisteredServerPort(vo.getRegisteredServerPort());
        //新增
		if(StringUtils.isBlank(vo.getId())) {
			String cameraId=IdGenerator.nextId();
			deviceId=IdGenerator.nextId();
			device.setId(deviceId);
			camera.setId(cameraId);
			camera.setDeviceId(device.getId());//关联
			camera.setHots(0);
			//camera.setBussGroupId(vo.getBusinessGroupId());
			camera.setCreateUser(userVo.getAccount());
			camera.setCreateTime(new Date());
			cameraMapping.setId(IdGenerator.nextId());
			cameraMapping.setCameraId(cameraId);//关联
			
			cameraMapper.insertSelective(camera);
			cameraMappingMapper.insertSelective(cameraMapping);
			deviceMapper.insertSelective(device);
		}else {
			//修改
			Example caExample=new Example(Camera.class);
			caExample.createCriteria().andEqualTo("deviceId",vo.getId());
			List<Camera> caList = cameraMapper.selectByExample(caExample);
			//camera.setBussGroupId(vo.getBusinessGroupId());
			if(caList.size()>0) {
				camera.setId(caList.get(0).getId());
				camera.setDeviceId(vo.getId().trim());
				camera.setUpdateUser(userVo.getAccount());
				camera.setUpdateTime(new Date());
                cameraMapper.updateByPrimaryKeySelective(camera);
			}else{
                camera.setId(IdGenerator.nextId());
                camera.setDeviceId(vo.getId().trim());
                camera.setCreateUser(userVo.getAccount());
                camera.setCreateTime(new Date());
                cameraMapper.insertSelective(camera);
            }

            //查询出关联表
            Example example = new Example(CameraMapping.class);
            example.createCriteria().andEqualTo("cameraId",camera.getId());
            List<CameraMapping> list = cameraMappingMapper.selectByExample(example);
            if(list.size()==0) {
                cameraMapping.setId(IdGenerator.nextId());
                cameraMapping.setCameraId(camera.getId());
                cameraMappingMapper.insertSelective(cameraMapping);
            }else {
                cameraMapping.setId(list.get(0).getId());
                cameraMapping.setCameraId(camera.getId());
                cameraMappingMapper.updateByPrimaryKeySelective(cameraMapping);
            }
            deviceId=device.getId();
            deviceMapper.updateByPrimaryKeySelective(device);
		}
		return deviceId;
	}

	@Override
	public DeviceParamVo getCameraAndDeviceInfo(String cameraId) {
		
		return deviceMapper.selectCameraAndDeviceInfo(cameraId);
	}

	@Override
	public String getMaxCodeBycode(String smbh) {
		return deviceMapper.selectMaxCodeBycode(smbh);
	}

	@Override
	public List<DeviceVo> getDeviceBySbbm(String sbbm) {
		/*Example example= new Example(Device.class);
		example.createCriteria().andEqualTo("sbbm",sbbm);
		List<Device> entity=deviceMapper.selectByExample(example);
		List<DeviceVo> list=BeanHelper.convertList(DeviceVo.class, entity);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("sbbm", sbbm);
		List<DeviceVo> list=deviceMapper.selectByParam(param);
		return list;
	}

	@Override
	public List<DeviceVo> selectByParam(Map<String, Object> param) {
		return deviceMapper.selectByParam(param);
	}

	@Override
	public DeviceVo getDeviceInfo(String id) {
		Device vo = deviceMapper.selectByPrimaryKey(id);
		return Convert.convert(DeviceVo.class, vo);
	}

	@Override
	public DeviceVo getDeviceByCameraId(String id) {
		Camera camera = cameraMapper.selectByPrimaryKey(id);
		if (null != camera) {
			
			Device vo = deviceMapper.selectByPrimaryKey(camera.getDeviceId());
			return Convert.convert(DeviceVo.class, vo);
		}
		
		return null;
	}

	@Override
	public PageInfo<Map<String,Object>> getDeviceByPage(CameraParamVo vo, TreeNodeChildren treeNodeChildren,
			PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		vo.setAddressNodeIds(treeNodeChildren.getAddressNodeIds());
		vo.setOrgNodeIds(treeNodeChildren.getOrgNodeIds());
		vo.setProjectNodeIds(treeNodeChildren.getProjectNodeIds());
		vo.setGroupNodeIds(treeNodeChildren.getGroupNodeIds());
		List<Map<String,Object>> list=deviceMapper.selectDeviceByMapPage(vo);
		return new PageInfo<Map<String,Object>>(list);
	}

	@Override
	public int countCmaerasByParam(CameraParamVo vo, TreeNodeChildren treeNodeChildren) {
		vo.setAddressNodeIds(treeNodeChildren.getAddressNodeIds());
		vo.setOrgNodeIds(treeNodeChildren.getOrgNodeIds());
		vo.setProjectNodeIds(treeNodeChildren.getProjectNodeIds());
		vo.setGroupNodeIds(treeNodeChildren.getGroupNodeIds());
		int count = deviceMapper.countCamerasByParam(vo);
		return count;
	}

}
