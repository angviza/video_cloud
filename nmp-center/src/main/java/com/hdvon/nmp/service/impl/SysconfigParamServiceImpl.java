package com.hdvon.nmp.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.entity.Dictionary;
import com.hdvon.nmp.entity.SysconfigParam;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.DictionaryMapper;
import com.hdvon.nmp.mapper.SysconfigParamMapper;
import com.hdvon.nmp.service.ISysconfigParamService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DictionaryVo;
import com.hdvon.nmp.vo.SysconfigParamVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>系统配置Service<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class SysconfigParamServiceImpl implements ISysconfigParamService {

	@Autowired
	private SysconfigParamMapper sysconfigMapper;
	@Autowired
	private DictionaryMapper dictionaryMapper;

	@Override
	public PageInfo<SysconfigParamVo> getSysConfigByPage(PageParam pageParam, Map<String, Object> param) {
		 PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		 List<SysconfigParamVo> list = sysconfigMapper.selectByParam(param);
	     return new PageInfo<>(list);
	}

	@Override
	public void save(SysconfigParamVo vo, UserVo userVo) {
		
		SysconfigParam sysconfig = Convert.convert(SysconfigParam.class,vo);
		sysconfig.setUpdateTime(new Date());
		sysconfig.setUpdateUser(userVo.getAccount());
		sysconfigMapper.updateByPrimaryKeySelective(sysconfig);
	
	}

	@Override
	public SysconfigParamVo getInfo(String id) {
		Map<String,Object> param =new HashMap<String,Object>();
    	param.put("id", id);
    	param.put("typeName", WebConstant.WEB_SYS_CONFIG);//字典类型
    	List<SysconfigParamVo> list = sysconfigMapper.selectByParam(param);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deleteByIds(List<String> idList) {
		Example example= new Example(SysconfigParam.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", idList);
		sysconfigMapper.deleteByExample(example);
		
	}

	@Override
	public List<SysconfigParamVo> getSysConfigByParam(Map<String, Object> param) {
		List<SysconfigParamVo> list = sysconfigMapper.selectByParam(param);
		return list;
	}

}
