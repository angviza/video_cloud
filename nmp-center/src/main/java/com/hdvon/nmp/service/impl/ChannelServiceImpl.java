package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.CameraMapping;
import com.hdvon.nmp.entity.Channel;
import com.hdvon.nmp.entity.EncoderServer;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.CameraMappingMapper;
import com.hdvon.nmp.mapper.ChannelMapper;
import com.hdvon.nmp.mapper.EncoderServerMapper;
import com.hdvon.nmp.service.IChannelService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ChannelVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>通道表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ChannelServiceImpl implements IChannelService {

	@Autowired
	private ChannelMapper channelMapper;
	
	@Autowired 
	private EncoderServerMapper encoderServerMapper;
	
	@Autowired 
	private CameraMappingMapper cameraMappingMapper;
	

	@Override
	public void saveChannel(UserVo userVo, ChannelVo channelVo) {
		Channel channel = Convert.convert(Channel.class, channelVo);
		if(StrUtil.isNotBlank(channelVo.getId())) {//修改
			Example ce_id = new Example(Channel.class);
			ce_id.createCriteria().andEqualTo("id",channel.getId());
			int countId = channelMapper.selectCountByExample(ce_id);
			if(countId == 0) {
				throw new ServiceException("用于修改的通道不存在！");
			}
			EncoderServer entity = encoderServerMapper.selectByPrimaryKey(channel.getEncoderServerId());
			
			if(entity == null) {
				throw new ServiceException("所属编码器不存在！");
			}
			Example ceName = new Example(Channel.class);
			ceName.createCriteria().andEqualTo("encoderServerId",channel.getEncoderServerId()).andEqualTo("name",channel.getName()).andNotEqualTo("id",channel.getId());
			int countChannel = channelMapper.selectCountByExample(ceName);
			if(countChannel > 0) {
				throw new ServiceException("该所属编码器已经存在该通道！");
			}
			channel.setUpdateTime(new Date());
			channel.setUpdateUser(userVo.getAccount());
			channelMapper.updateByPrimaryKeySelective(channel);
		}else {//新增
			EncoderServer entity = encoderServerMapper.selectByPrimaryKey(channel.getEncoderServerId());
			if(entity == null) {
				throw new ServiceException("所属编码器不存在！");
			}
			
			Example ceName = new Example(Channel.class);
			ceName.createCriteria().andEqualTo("encoderServerId",channel.getEncoderServerId()).andEqualTo("name",channel.getName());
			int countChannel = channelMapper.selectCountByExample(ceName);
			if(countChannel > 0) {
				throw new ServiceException("该所属编码器已经存在该通道！");
			}
			channelMapper.insertSelective(channel);
		}
	}

	@Override
	public PageInfo<ChannelVo> getChannelPages(PageParam pp, Map<String, Object> map) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<ChannelVo> list = channelMapper.selectChannelList(map);
		return new PageInfo<ChannelVo>(list);
	}

	@Override
	public List<ChannelVo> getChannelList(Map<String, Object> map) {
		List<ChannelVo> list = channelMapper.selectChannelList(map);
		return list;
	}

	@Override
	public void delChannelsByIds(List<String> ids) {
		Example cme = new Example(CameraMapping.class);
		cme.createCriteria().andIn("id",ids);
		int count = cameraMappingMapper.selectCountByExample(cme);
		if(count > 0) {
			throw new ServiceException("通道有关联的摄像机，不能删除");
		}
		
		Example ce = new Example(Channel.class);
		ce.createCriteria().andIn("id",ids);
		channelMapper.deleteByExample(ce);
	}

	@Override
	public ChannelVo getChannelById(Map<String,Object> map) {
		ChannelVo channelVo = channelMapper.selectChannelInfoById(map);
		return channelVo;
	}

}
