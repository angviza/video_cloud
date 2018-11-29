package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.DictionaryParamVo;
import com.hdvon.nmp.vo.DictionaryVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Dictionary;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface DictionaryMapper extends Mapper<Dictionary> , MySqlMapper<Dictionary>{

    List<DictionaryVo> queryDictionaryVoPage(DictionaryParamVo paramVo);

    DictionaryVo queryDictionaryInfoById(@Param("id") String id);

    List<DictionaryVo> selectDictionaryList(Map<String, Object> param);

	List<DictionaryVo> selectByParam(Map<String, Object> param);
	
}