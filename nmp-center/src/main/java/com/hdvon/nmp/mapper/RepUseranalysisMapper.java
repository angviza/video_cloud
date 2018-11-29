package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.RepUseranalysis;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.RepUseranalysisVo;

public interface RepUseranalysisMapper extends Mapper<RepUseranalysis> , MySqlMapper<RepUseranalysis>{

	List<RepUseranalysisVo> selectUseranalysisByadmin(Map<String, Object> param);

}