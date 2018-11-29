package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.vo.CameraNode;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.BussinessGroup;
import com.hdvon.nmp.entity.Device;
import com.hdvon.nmp.entity.Organization;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.BussinessGroupMapper;
import com.hdvon.nmp.mapper.DeviceMapper;
import com.hdvon.nmp.mapper.OrganizationMapper;
import com.hdvon.nmp.service.IBussinessGroupService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.BussinessGroupVo;
import com.hdvon.nmp.vo.CameraBussinessGroupVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>业务分组Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class BussinessGroupServiceImpl implements IBussinessGroupService {

	@Autowired
	private BussinessGroupMapper bussinessGroupMapper;
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private OrganizationMapper organizationMapper;
	@Autowired
    private CameraMapper cameraMapper;

	@Override
	public void saveBussinessGroup(UserVo userVo, BussinessGroupVo vo) {
		Example example= new Example(BussinessGroup.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("code",vo.getCode());
		if(StrUtil.isNotBlank(vo.getId())) {
			criteria.andNotEqualTo("id",vo.getId());
		}
		int count=bussinessGroupMapper.selectCountByExample(example);
		if(count>0) {
			throw new ServiceException("编号已存在！");
		}
		
		BussinessGroup buss = Convert.convert(BussinessGroup.class,vo);
		if(StrUtil.isBlank(buss.getId())) {
			example.clear();
			example.createCriteria().andEqualTo("name",vo.getName());
			int nameCount = bussinessGroupMapper.selectCountByExample(example);
			if(nameCount > 0) {
				throw new ServiceException("分组名称已存在！");
			}

			buss.setId(IdGenerator.nextId());
			Date date = new Date();
			buss.setCreateUser(userVo.getAccount());
			buss.setCreateTime(date);
			buss.setUpdateUser(userVo.getAccount());
			buss.setUpdateTime(date);
			//未关联
			buss.setStatus(0);
			bussinessGroupMapper.insertSelective(buss);
		}else {
			example.clear();
			example.createCriteria().andEqualTo("name",vo.getName()).andNotEqualTo("id",vo.getId());
			int nameCount = bussinessGroupMapper.selectCountByExample(example);
			if(nameCount > 0) {
				throw new ServiceException("分组名称已存在！");
			}

			buss.setUpdateTime(new Date());
			buss.setUpdateUser(userVo.getAccount());
			bussinessGroupMapper.updateByPrimaryKeySelective(buss);
		}
	}

	@Override
	public PageInfo<BussinessGroupVo> getBussinessGroupByPage(BussinessGroupVo vo, PageParam pageParam) {
		 PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		/* Example example= new Example(BussinessGroup.class);
		 Example.Criteria criteria= example.createCriteria();
		 if(StrUtil.isNotBlank(vo.getCode())) {
			 criteria.andLike("code","%"+vo.getCode()+"%");
		 }
		 if(StrUtil.isNotBlank(vo.getName())) {
			 criteria.andLike("name","%"+vo.getName()+"%");
		 }
		 List<BussinessGroup> entity= bussinessGroupMapper.selectByExample(example);
		 List<BussinessGroupVo> pageList=BeanHelper.convertList(BussinessGroupVo.class, entity);*/
		 Map<String,Object> param = new HashMap<String, Object>();
		 param.put("code", vo.getCode());
		 param.put("name", vo.getName());
		 List<BussinessGroupVo> pageList=bussinessGroupMapper.selectByParam(param);
		return new PageInfo<>(pageList);
	}

	@Override
	public void deleteBussinessGroup(List<String> buessIdList) {
		Example device= new Example(Device.class);
		device.createCriteria().andIn("bussGroupId",buessIdList);
		Example orgEample= new Example(Organization.class);
		orgEample.createCriteria().andIn("bussGroupId",buessIdList);
        int count= deviceMapper.selectCountByExample(device);
        int orgCount= organizationMapper.selectCountByExample(orgEample);
        if(orgCount==0 && count==0) {
        	Example example= new Example(BussinessGroup.class);
        	example.createCriteria().andIn("id",buessIdList);
    		bussinessGroupMapper.deleteByExample(example);
        }else {
        	throw new ServiceException("业务分组已存在关联,不能删除！");
        }
	}

	@Override
	public PageInfo<CameraBussinessGroupVo> getBuessByCameraPage(Map<String,Object> param, PageParam pageParam) {
	    PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		List<CameraBussinessGroupVo> list= bussinessGroupMapper.selectBuessByCameraPage(param);
		return new PageInfo<>(list);
	}

    @Override
    public List<CameraNode> getRelateCamera(UserVo userVo, String bussGroupId){
	    Map<String,Object> map = new HashMap<>();
	    if(bussGroupId == null){
	        bussGroupId = "";
        }
	    map.put("bussGroupId",bussGroupId);
	    return cameraMapper.selectCameraNode(map);
    }

	@Override
	public void saveRelateCamera(UserVo userVo,String buessId, List<String> deviceIdList) {
		Example example= new Example(BussinessGroup.class);
		example.createCriteria().andEqualTo("id",buessId);
		//清空之前关联摄像机
		Example clear =new Example(Device.class);
		clear.createCriteria().andEqualTo("bussGroupId",buessId);
		Device clearDevice=new Device();
		clearDevice.setBussGroupId("");
	    deviceMapper.updateByExampleSelective(clearDevice, clear);
	    
		if(deviceIdList ==null || deviceIdList.size()==0) {
			BussinessGroup record= new BussinessGroup();
			record.setStatus(0);//未关联摄像机
			bussinessGroupMapper.updateByExampleSelective(record, example);
		}else {
			//关联摄像机
			Device device=new Device();
			device.setBussGroupId(buessId);
			for(String deviceId:deviceIdList) {
				if(StrUtil.isNotBlank(deviceId)) {
					device.setId(deviceId);
					deviceMapper.updateByPrimaryKeySelective(device);
				}
			}
			
			BussinessGroup record= new BussinessGroup();
			record.setStatus(1);//已关联摄像机
			bussinessGroupMapper.updateByExampleSelective(record, example);
		}
		
	}

	@Override
	public BussinessGroupVo getBuessByCameraPage(String id) {
		BussinessGroup buss =bussinessGroupMapper.selectByPrimaryKey(id);
		BussinessGroupVo vo = Convert.convert(BussinessGroupVo.class,buss);
		return vo;
	}

	@Override
	public PageInfo<CameraBussinessGroupVo> getBuessByCameraList(PageParam pageParam,Map<String,Object> param) {
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		List<CameraBussinessGroupVo> list =bussinessGroupMapper.selectBuessByCameraPage(param);
		return new PageInfo<>(list);
	}

	@Override
	public List<BussinessGroupVo> getBussinessGroupList() {
		/*List<BussinessGroup> entity=bussinessGroupMapper.selectByExample(null);
		List<BussinessGroupVo> bussList=BeanHelper.convertList(BussinessGroupVo.class, entity);*/
		List<BussinessGroupVo> bussList=bussinessGroupMapper.selectByParam(null);
		return bussList;
	}

}
