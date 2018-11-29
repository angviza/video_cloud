package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.ChannelVo;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Channel;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface ChannelMapper extends Mapper<Channel> , MySqlMapper<Channel>{

    List<ChannelVo> selectChannelList(Map<String, Object> map);

    ChannelVo selectChannelInfoById(Map<String, Object> map);
}