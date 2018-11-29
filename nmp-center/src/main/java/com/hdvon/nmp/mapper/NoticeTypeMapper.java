package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.NoticeType;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.NoticeTypeVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NoticeTypeMapper extends Mapper<NoticeType> , MySqlMapper<NoticeType>{
    List<NoticeTypeVo> queryUnReadNoticeType(@Param("userId") String userId,@Param("flag") Integer flag);
}