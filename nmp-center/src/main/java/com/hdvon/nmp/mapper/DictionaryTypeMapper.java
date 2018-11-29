package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.DictionaryType;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.DictionaryTypeVo;

public interface DictionaryTypeMapper extends Mapper<DictionaryType> , MySqlMapper<DictionaryType>{

	List<DictionaryTypeVo> selectByParam(Map<String, Object> param);

}