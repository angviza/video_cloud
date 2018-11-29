package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.UserFilepath;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.UserFilepathMapper;
import com.hdvon.nmp.service.IUserFilepathService;
import com.hdvon.nmp.util.BeanHelper;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.UserFilepathVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>用户文件存储路径Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class UserFilepathServiceImpl implements IUserFilepathService {

	@Autowired
	private UserFilepathMapper userFilepathMapper;

	@Override
	public List<UserFilepathVo> getUserFilepath(Map<String ,String> map) {
		/*Example example = new Example(UserFilepath.class);
		example.createCriteria().andEqualTo("userId",map.get("userId")).andEqualTo("macId", map.get("macId"));
		List<UserFilepath> list=userFilepathMapper.selectByExample(example);
		return BeanHelper.convertList(UserFilepathVo.class, list);*/
		List<UserFilepathVo> list=userFilepathMapper.selectByParam(map);
		return list;
	}

	@Override
	public void saveUserFilepath(UserFilepathVo vo, UserVo user) {
		if(StrUtil.isBlank(vo.getFilePath()) && StrUtil.isBlank(vo.getImgPath())) {
			throw new ServiceException("文件储存路径和图片储存路径不能同时为空！");
		}
		UserFilepath userFilepath=Convert.convert(UserFilepath.class,vo);
		
		Example example = new Example(UserFilepath.class);
		example.createCriteria().andEqualTo("userId",user.getId()).andEqualTo("macId",vo.getMacId());
		
		UserFilepath record = userFilepathMapper.selectOneByExample(example);
		if(record == null) {
			userFilepath.setId(IdGenerator.nextId());
			userFilepath.setUserId(user.getId());
			userFilepath.setCreateTime(new Date());
			userFilepath.setCreateUser(user.getAccount());
			userFilepathMapper.insert(userFilepath);
		}else {
			userFilepath.setId(record.getId());
			userFilepath.setUpdateTime(new Date());
			userFilepath.setUpdateUser(user.getAccount());
			userFilepathMapper.updateByPrimaryKeySelective(userFilepath);
		}
		
	}


}
