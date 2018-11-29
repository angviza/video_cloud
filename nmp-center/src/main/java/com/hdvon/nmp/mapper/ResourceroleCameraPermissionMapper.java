package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.ResourceroleCameraPermissionVo;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.ResourceroleCameraPermission;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface ResourceroleCameraPermissionMapper extends Mapper<ResourceroleCameraPermission> , MySqlMapper<ResourceroleCameraPermission>{

    public List<ResourceroleCameraPermissionVo> selectCameraPermission(Map<String,Object> param);

}