package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.MatrixChannelVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.MatrixChannel;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface MatrixChannelMapper extends Mapper<MatrixChannel> , MySqlMapper<MatrixChannel>{

    List<MatrixChannelVo> selectMatrixChannelList(Map<String, Object> param);

    MatrixChannelVo selectMatrixChannelById(@Param("id") String id);

    List<MatrixChannelVo> selectMatrixChannelByWallId(@Param("wallId") String wallId);

	List<MatrixChannelVo> selectByParam(Map<String, Object> param);
	
	List<MatrixChannelVo> selectMatrixChannelsByMatrixIds(@Param("matrixIds")List<String> matrixIds);

	String selectMaxCodeBycode(Map<String, Object> map);

}