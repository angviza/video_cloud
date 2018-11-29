package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import com.hdvon.nmp.entity.AttachFile;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.AttachFileVo;
import com.hdvon.nmp.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.AttachFileMapper;
import com.hdvon.nmp.service.IAttachFileService;

import java.util.Date;

/**
 * <br>
 * <b>功能：</b>上传文件表Service<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
@Service
public class AttachFileServiceImpl implements IAttachFileService {

	@Autowired
	private AttachFileMapper attachFileMapper;

	@Override
	public AttachFileVo saveAttachFile(UserVo userVo, AttachFileVo attachFileVo) {
		AttachFile attachFile = Convert.convert(AttachFile.class, attachFileVo);
		AttachFile aFile = null;
		if(attachFileVo != null){
			Date date = new Date();
			attachFile.setId(IdGenerator.nextId());
			attachFile.setCreateTime(date);
			attachFile.setCreateUser(userVo.getAccount());
			attachFile.setUpdateTime(date);
			attachFile.setUpdateUser(userVo.getAccount());

			attachFileMapper.insertSelective(attachFile);
			aFile = attachFileMapper.selectByPrimaryKey(attachFile);
		}
		AttachFileVo attachF = Convert.convert(AttachFileVo.class, aFile);
		return  attachF;
	}
}
