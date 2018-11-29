package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.LimitLogin;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.LimitLoginMapper;
import com.hdvon.nmp.service.ILimitLoginService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.LimitLoginVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>用户登录限制登录表Service<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class LimitLoginServiceImpl implements ILimitLoginService {

	@Autowired
	private LimitLoginMapper limitLoginMapper;

	/**
	 * 查询
	 */
	@Override
	public PageInfo<LimitLoginVo> getLimitByPage(PageParam pageParam, Map<String, Object> param) {
		 PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		 List<LimitLoginVo> list = limitLoginMapper.selectByParam(param);
	     return new PageInfo<>(list);
	}

	@Override
	public List<LimitLoginVo> getLimitLoginList(Map<String, Object> param) {
		 List<LimitLoginVo> list = limitLoginMapper.selectByParam(param);
		return list;
	}
	
	
	@Override
	public int seletCount(Map<String, Object> param) {
		Example example= new Example(LimitLogin.class);
		Example.Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank((String)param.get("state"))) {
			criteria.andEqualTo("state", param.get("state"));
		}
		if(StrUtil.isNotBlank((String)param.get("type"))) {
			criteria.andEqualTo("type", param.get("type"));
		}
		if(StrUtil.isNotBlank((String)param.get("macName"))) {
			criteria.andEqualTo("macName", param.get("macName"));
		}
		return limitLoginMapper.selectCountByExample(example);
	}
	/**
	 * 保存修改
	 */
	@Override
	public LimitLoginVo limitLoginSave(LimitLoginVo vo,UserVo userVo) {
		LimitLogin limitLogin = Convert.convert(LimitLogin.class,vo);
		Example example= new Example(LimitLogin.class);
		Example.Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(limitLogin.getId())) {
			criteria.andNotEqualTo("id", vo.getId());
		}
		// ip地址
		if(StrUtil.isNotBlank(vo.getIsRegasion())) {
			if("0".equals(vo.getIsRegasion())) {
				criteria.andEqualTo("startRegaion", vo.getStartRegaion());
				criteria.andNotEqualTo("isRegasion", "1");//不是区间地址
				int count = limitLoginMapper.selectCountByExample(example);
				if(count >0 ) {
					throw new ServiceException("该Ip地址已存在！");
				}
			}
		}else {
			//mac地址
			
			criteria.andEqualTo("macName", vo.getMacName());
			int count = limitLoginMapper.selectCountByExample(example);
			if(count >0 ) {
				throw new ServiceException("该mac地址已存在！");
			}
		}
		//保存
		if(StrUtil.isBlank(vo.getId())) {
			limitLogin.setId(IdGenerator.nextId());
			limitLogin.setCreateTime(new Date());
			limitLogin.setCreateUser(userVo.getAccount());
			limitLoginMapper.insert(limitLogin);
		}else {
			limitLogin.setUpdateTime(new Date());
			limitLogin.setUpdateUser(userVo.getAccount());
			limitLoginMapper.updateByPrimaryKeySelective(limitLogin);
			
		}
		return null;
	}

	/**
	 * 查看详细
	 */
	@Override
	public LimitLoginVo getInfo(String id) {
		Map<String,Object> param =new HashMap<String,Object>();
    	param.put("id", id);
    	List<LimitLoginVo> list = limitLoginMapper.selectByParam(param);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 删除
	 */
	@Override
	public void deleteLimitIp(List<String> idList) {
		Example example= new Example(LimitLogin.class);
		example.createCriteria().andIn("id", idList).andEqualTo("state",1);
		int count =limitLoginMapper.selectCountByExample(example);
		if(count > 0){
			throw new ServiceException("存在启用的记录，请停用，再删除");
		}
		example.clear();
		example.createCriteria().andIn("id", idList);
		limitLoginMapper.deleteByExample(example);
	}

	@Override
	public void updateState(LimitLoginVo vo, UserVo userVo) {
		LimitLogin limitLogin = Convert.convert(LimitLogin.class,vo);
		limitLogin.setUpdateTime(new Date());
		limitLogin.setUpdateUser(userVo.getAccount());
		limitLoginMapper.updateByPrimaryKeySelective(limitLogin);
		
	}
	


}
