package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.BussinessGroup;
import com.hdvon.nmp.vo.BussinessGroupVo;
import com.hdvon.nmp.vo.CameraBussinessGroupVo;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface BussinessGroupMapper extends Mapper<BussinessGroup> , MySqlMapper<BussinessGroup> {

    List<CameraBussinessGroupVo> selectBuessByCameraPage(Map<String,Object> param);

	List<BussinessGroupVo> selectByParam(Map<String, Object> param);

}