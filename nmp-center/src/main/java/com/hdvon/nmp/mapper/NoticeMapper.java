package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.Notice;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.NoticeVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface NoticeMapper extends Mapper<Notice> , MySqlMapper<Notice>{
    List<NoticeVo> selectPublishMessageByParam(Map<String, Object> param);

    List<NoticeVo> selectReceiveMessageByParam(Map<String, Object> param);

    List<NoticeVo> queryNoticeByParam(@Param("createUser") String createUser,@Param("id") String id);

    List<NoticeVo> queryNoticeByReceive(@Param("userId") String userId,@Param("id") String id);

    List<NoticeVo> queryNoticeByFlag(@Param("userId") String userId,@Param("type") Integer type,@Param("flag") Integer flag);

    List<NoticeVo> queryNoticeUnRead(@Param("userId") String userId, @Param("flag") Integer flag);

    Integer queryUnReadTotal(@Param("userId") String userId, @Param("flag") Integer flag);

    String queryRemindMessage();
}