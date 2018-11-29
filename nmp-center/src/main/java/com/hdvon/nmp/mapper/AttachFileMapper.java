package com.hdvon.nmp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.AttachFile;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.AttachFileVo;

import tk.mybatis.mapper.common.Mapper;

public interface AttachFileMapper extends Mapper<AttachFile> , MySqlMapper<AttachFile>{
	List<AttachFileVo> queryAttachFileByNoticeId(@Param("id") String id);
}