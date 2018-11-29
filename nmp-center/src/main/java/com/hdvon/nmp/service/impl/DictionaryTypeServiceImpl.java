package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Dictionary;
import com.hdvon.nmp.entity.DictionaryType;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.DictionaryMapper;
import com.hdvon.nmp.mapper.DictionaryTypeMapper;
import com.hdvon.nmp.service.IDictionaryTypeService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DictionaryTypeVo;
import com.hdvon.nmp.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>字典类型表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class DictionaryTypeServiceImpl implements IDictionaryTypeService {

    @Autowired
    private DictionaryTypeMapper dictionaryTypeMapper;
    
    @Autowired
    private DictionaryMapper dictionaryMapper;

	@Override
	public void editDictionaryType(UserVo userVo, DictionaryTypeVo typeVo){
		DictionaryType dictionaryType = Convert.convert(DictionaryType.class,typeVo);
		
		
		if(StringUtils.isNotEmpty(dictionaryType.getId())) {
			Example de = new Example(DictionaryType.class);
			de.createCriteria().andEqualTo("chName", dictionaryType.getChName()).andNotEqualTo("id", dictionaryType.getId());
			int countch= dictionaryTypeMapper.selectCountByExample(de);
			if(countch > 0) {
				throw new ServiceException("字典类型中文名已经存在！");
			}
			Example deEn = new Example(DictionaryType.class);
			deEn.createCriteria().andEqualTo("enName", dictionaryType.getEnName()).andNotEqualTo("id", dictionaryType.getId());
			int countEn = dictionaryTypeMapper.selectCountByExample(deEn);
			if(countEn > 0) {
				throw new ServiceException("字典类型英文名已经存在！");
			}
			
			dictionaryType.setUpdateTime(new Date());
			dictionaryType.setUpdateUser(userVo.getAccount());

			dictionaryType.setChName(typeVo.getChName().trim());
			dictionaryType.setEnName(typeVo.getEnName().trim());

			dictionaryTypeMapper.updateByPrimaryKeySelective(dictionaryType);
		}else {
			Example de = new Example(DictionaryType.class);
			de.createCriteria().andEqualTo("chName", dictionaryType.getChName());
			int countCh= dictionaryTypeMapper.selectCountByExample(de);
			if(countCh > 0) {
				throw new ServiceException("字典类型中文名已经存在！");
			}
			Example deEn = new Example(DictionaryType.class);
			deEn.createCriteria().andEqualTo("enName", dictionaryType.getEnName());
			int countEn = dictionaryTypeMapper.selectCountByExample(deEn);
			if(countEn > 0) {
				throw new ServiceException("字典类型英文名已经存在！");
			}
			dictionaryType.setId(IdGenerator.nextId());
			Date date = new Date();
			dictionaryType.setCreateTime(date);
			dictionaryType.setCreateUser(userVo.getAccount());
			dictionaryType.setUpdateUser(userVo.getAccount());
			dictionaryType.setUpdateTime(date);

			dictionaryType.setChName(typeVo.getChName().trim());
			dictionaryType.setEnName(typeVo.getEnName().trim());

			dictionaryTypeMapper.insertSelective(dictionaryType);
		}
	}

	@Override
	public void delDictionaryType(String id) {
		dictionaryTypeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void delDictionaryTypes(List<String> ids) {
		Example de = new Example(DictionaryType.class);
		de.createCriteria().andIn("id", ids);
		dictionaryTypeMapper.deleteByExample(de);
		
		//删除字典类型同时删除字典类型下关联的字典
		Example deDict = new Example(Dictionary.class);
		deDict.createCriteria().andIn("dictionaryTypeId", ids);
		dictionaryMapper.deleteByExample(deDict);
	}

	@Override
	public DictionaryTypeVo getDictionaryTypeById(String id) {
		DictionaryType dicType = dictionaryTypeMapper.selectByPrimaryKey(id);
		DictionaryTypeVo dicTypeVo = Convert.convert(DictionaryTypeVo.class, dicType);
		return dicTypeVo;
	}

	@Override
	public PageInfo<DictionaryTypeVo> getDictionaryTypePage(PageParam pageParam, String search) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("search", search);
		List<DictionaryTypeVo> typeVos=dictionaryTypeMapper.selectByParam(param);
		return new PageInfo<DictionaryTypeVo>(typeVos);
	}

	@Override
	public List<DictionaryTypeVo> getDictionaryTypeList(String search) {
		 Map<String,Object> param = new HashMap<String, Object>();
	     param.put("search", search);
	     List<DictionaryTypeVo> typeVos=dictionaryTypeMapper.selectByParam(param);
		return typeVos;
	}

	@Override
	public List<DictionaryTypeVo> getDictionaryTypeByCnName(String chName) {
		Map<String,Object> param = new HashMap<String, Object>();
	    param.put("chName", chName);
		List<DictionaryTypeVo> typeVos =dictionaryTypeMapper.selectByParam(param);
		return typeVos;
	}

	@Override
	public List<DictionaryTypeVo> getDictionaryTypeByEnName(String enName) {
		Map<String,Object> param = new HashMap<String, Object>();
	    param.put("enName", enName);
		List<DictionaryTypeVo> typeVos =dictionaryTypeMapper.selectByParam(param);
		return typeVos;
	}

}
