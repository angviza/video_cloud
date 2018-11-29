package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.MatrixVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Matrix;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface MatrixMapper extends Mapper<Matrix> , MySqlMapper<Matrix>{

    List<MatrixVo> selectMatrixList(Map<String, Object> param);

    MatrixVo selectMatrixDetailById(@Param("id") String id);
    
    List<MatrixVo> selectMatrixPage(Map<String, Object> param);

	String selectMaxCodeBycode(Map<String, Object> map);
}