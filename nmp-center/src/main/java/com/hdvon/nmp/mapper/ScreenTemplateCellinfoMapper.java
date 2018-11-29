package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.ScreenTemplateCellinfo;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.ScreenTemplateCellinfoVo;

public interface ScreenTemplateCellinfoMapper extends Mapper<ScreenTemplateCellinfo> , MySqlMapper<ScreenTemplateCellinfo>{

	List<ScreenTemplateCellinfoVo> selectByParam(Map<String, Object> param);

}