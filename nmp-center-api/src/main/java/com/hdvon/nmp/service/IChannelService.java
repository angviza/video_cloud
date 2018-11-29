package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ChannelVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>通道表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IChannelService{
	
	/**
     * 保存编码器通道信息
     * @param userVo
     * @param matrixVo
     */
    void saveChannel(UserVo userVo, ChannelVo channelVo);
    /**
     * 分页查询编码器通道列表
     * @param pp
     * @param search
     * @return
     */
    PageInfo<ChannelVo> getChannelPages(PageParam pp, Map<String,Object> map);
    /**
     * 查询编码器通道列表
     * @param search
     * @return
     */
    List<ChannelVo> getChannelList(Map<String,Object> map);
    /**
     * 批量删除编码器通道
     * @param ids
     */
    void delChannelsByIds(List<String> ids);
    /**
     * 根据id查询单个编码器通道信息
     * @param id
     * @return
     */
    ChannelVo getChannelById(Map<String,Object> map);
}
