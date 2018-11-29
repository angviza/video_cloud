package com.hdvon.nmp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.entity.Resourcerole;
import com.hdvon.nmp.entity.ResourceroleCameraPermission;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.ResourceroleCameraPermissionMapper;
import com.hdvon.nmp.mapper.ResourceroleMapper;
import com.hdvon.nmp.service.IResourceroleCameraPermissionService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.JsonPermissionVo;
import com.hdvon.nmp.vo.ResourceroleAddressPermissionVo;
import com.hdvon.nmp.vo.ResourceroleCameraPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>资源角色与地址树、摄像头权限关联表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ResourceroleCameraPermissionServiceImpl implements IResourceroleCameraPermissionService {

	@Autowired
	private ResourceroleMapper resourceroleMapper;
	@Autowired
    private ResourceroleCameraPermissionMapper resourceroleCameraPermissionMapper;

	@Override
	public List<ResourceroleCameraPermissionVo> getCameraPermission(String resourceroleId) {
        Map<String,Object> param = new HashMap<>();
        if(StrUtil.isEmpty(resourceroleId)){
            resourceroleId = "0";
        }
        param.put("resourceroleId",resourceroleId);
		return resourceroleCameraPermissionMapper.selectCameraPermission(param);
	}

	@Override
	public void saveCameraPermission(ResourceroleAddressPermissionVo resourceVo){
        if(StrUtil.isBlank(resourceVo.getResouceroleId())) {
            throw new ServiceException("资源角色id不能为空，保存失败！");
        }
        Resourcerole resourcerole = resourceroleMapper.selectByPrimaryKey(resourceVo.getResouceroleId());
        if(resourcerole == null){
            throw new ServiceException("资源角色不存在，保存失败！");
        }

        //验证格式是否正确
        List<JsonPermissionVo> jsonPermission = JSON.parseArray(resourceVo.getPermissionContent(), JsonPermissionVo.class);
        ArrayList<ResourceroleCameraPermission> permissionList = new ArrayList<>();
        for(JsonPermissionVo permissionVo :jsonPermission) {
            //验证
            if(StrUtil.isBlank(permissionVo.getId())) {
                throw new ServiceException("地址树id或者摄像机id不能为空，保存失败！");
            }
            if(StrUtil.isBlank(permissionVo.getValue())) {
                throw new ServiceException("摄像机操作权限值不能为空，保存失败！");
            }
            //赋值
            ResourceroleCameraPermission newPermission = new ResourceroleCameraPermission();
            newPermission.setId(IdGenerator.nextId());
            newPermission.setResouceroleId(resourceVo.getResouceroleId());
            newPermission.setCameraId(permissionVo.getId());
            newPermission.setPermissionValue("null".equals(permissionVo.getValue()) ? null : permissionVo.getValue());
            permissionList.add(newPermission);
        }

		//删除原记录
		Example example = new Example(ResourceroleCameraPermission.class);
		example.createCriteria().andEqualTo("resouceroleId", resourceVo.getResouceroleId());
        resourceroleCameraPermissionMapper.deleteByExample(example);

        //插入新数据
        if(permissionList.size() > 0){
            resourceroleCameraPermissionMapper.insertList(permissionList);
        }

        //设置资源角色状态
        Resourcerole resourceRole = new Resourcerole();
        resourceRole.setId(resourceVo.getResouceroleId());
		if(jsonPermission != null && jsonPermission.size() > 0) {
			resourceRole.setIsPermAssigned(1);
		}else {
			resourceRole.setIsPermAssigned(0);
		}
        resourceroleMapper.updateByPrimaryKeySelective(resourceRole);
	}

}
