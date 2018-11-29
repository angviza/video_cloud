package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.UserNotice;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.UserNoticeVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserNoticeMapper extends Mapper<UserNotice> , MySqlMapper<UserNotice>{
	UserNoticeVo queryUserNoticeByParams(@Param("userId") String userId,@Param("noticeId") String noticeId);
	List<UserNoticeVo> queryUserNoticeByTypeId(@Param("userId") String userId,@Param("flag") Integer flag,@Param("array") List<String> array);
	List<UserNoticeVo> queryUserNotice (@Param("userId") String userId,@Param("array") List<String> array);
}