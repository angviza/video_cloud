package com.hdvon.quartz.mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.quartz.entity.ReportResponseVo;
import com.hdvon.quartz.entity.RepUseranalysis;
import com.hdvon.quartz.entity.UserLog;
import com.hdvon.quartz.util.MySqlMapper;
import com.hdvon.quartz.vo.UserVo;

import tk.mybatis.mapper.common.Mapper;

public interface UserLogMapper extends Mapper<UserLog> , MySqlMapper<UserLog>{

	List<ReportResponseVo> selectHistoryCamara(Map<String, Object> param);

	List<RepUseranalysis> selectUsertCamerTotal(Map<String, Object> param);

	List<RepUseranalysis> selectUserOnlineTime(Map<String, Object> param);

	List<RepUseranalysis> selectUserLoginTotal(Map<String, Object> param);

	List<UserVo> selectColseInviteUser(Map<String, Object> param);

}