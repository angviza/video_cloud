package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.NoticeType;
import com.hdvon.nmp.mapper.NoticeTypeMapper;
import com.hdvon.nmp.service.INoticeTypeService;
import com.hdvon.nmp.vo.NoticeTypeVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>公告类型表Service<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class NoticeTypeServiceImpl implements INoticeTypeService {

	@Autowired
	private NoticeTypeMapper noticeTypeMapper;

	@Override
	public List<NoticeTypeVo> selectNoticeType() {
		List<NoticeType> noticeTypes = noticeTypeMapper.selectAll();
		List<NoticeTypeVo> list =  new ArrayList();
		for(NoticeType noticeType:noticeTypes){
			NoticeTypeVo noticeTypeVo = Convert.convert(NoticeTypeVo.class, noticeType);
			list.add(noticeTypeVo);
		}
		return list;
	}
}
