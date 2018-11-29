package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.ScreenTemplateVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.ScreenTemplate;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface ScreenTemplateMapper extends Mapper<ScreenTemplate> , MySqlMapper<ScreenTemplate>{

    List<ScreenTemplateVo> selectScreenTemplateByPollingId(@Param("pollingId") String pollingId);

	List<ScreenTemplateVo> selectByParam(Map<String, String> map);
}