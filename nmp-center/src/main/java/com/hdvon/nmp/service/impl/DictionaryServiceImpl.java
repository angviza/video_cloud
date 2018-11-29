package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Dictionary;
import com.hdvon.nmp.entity.DictionaryType;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.DictionaryMapper;
import com.hdvon.nmp.mapper.DictionaryTypeMapper;
import com.hdvon.nmp.service.IDictionaryService;
import com.hdvon.nmp.util.BeanHelper;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DictionaryImportVo;
import com.hdvon.nmp.vo.DictionaryParamVo;
import com.hdvon.nmp.vo.DictionaryTypeImportVo;
import com.hdvon.nmp.vo.DictionaryTypeVo;
import com.hdvon.nmp.vo.DictionaryVo;
import com.hdvon.nmp.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <br>
 * <b>功能：</b>数据字典表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class DictionaryServiceImpl implements IDictionaryService {

	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	@Autowired
	private DictionaryTypeMapper dictionaryTypeMapper;

    @Override
    public void editDictionary(UserVo userVo, DictionaryVo dictionaryVo){
        Dictionary dictionary = Convert.convert(Dictionary.class,dictionaryVo);
		
        if(StringUtils.isNotEmpty(dictionary.getId())) {
        	Example deCh = new Example(Dictionary.class);
        	deCh.createCriteria().andEqualTo("chName",dictionaryVo.getChName()).andNotEqualTo("id",dictionary.getId()).andEqualTo("dictionaryTypeId",dictionaryVo.getDictionaryTypeId());
    		int countChname = dictionaryMapper.selectCountByExample(deCh);
    		if(countChname>0) {
    			throw new ServiceException("同一字典类型不能存在相同的字典中文名！");
    		}

    		DictionaryType entity = dictionaryTypeMapper.selectByPrimaryKey(dictionaryVo.getDictionaryTypeId());
    		if(entity == null) {
    			throw new ServiceException("不存在该字典类型！");
    		}
    		
    		Example de = new Example(Dictionary.class);
    		de.createCriteria().andEqualTo("dictionaryTypeId",dictionaryVo.getDictionaryTypeId()).andNotEqualTo("id",dictionaryVo.getId()).andEqualTo("value",dictionaryVo.getValue());
    		int countValue = dictionaryMapper.selectCountByExample(de);
    		if(countValue > 0) {
    			throw new ServiceException("同一字典类型不能存在相同的字典值！");
    		}
    		Date date = new Date();
        	dictionary.setUpdateTime(date);
        	dictionary.setUpdateUser(userVo.getAccount());

        	dictionary.setChName(dictionaryVo.getChName());
        	dictionary.setEnName(dictionaryVo.getEnName());

            dictionaryMapper.updateByPrimaryKeySelective(dictionary);
        }else {
        	Example de_ch = new Example(Dictionary.class);
        	de_ch.createCriteria().andEqualTo("chName",dictionaryVo.getChName()).andEqualTo("dictionaryTypeId",dictionaryVo.getDictionaryTypeId());
    		int countChname = dictionaryMapper.selectCountByExample(de_ch);
    		if(countChname>0) {
    			throw new ServiceException("同一字典类型不能存在相同的字典中文名！");
    		}

    		DictionaryType dicType = dictionaryTypeMapper.selectByPrimaryKey(dictionaryVo.getDictionaryTypeId());
    		if(dicType == null) {
    			throw new ServiceException("不存在该字典类型！");
    		}
    		
    		Example de = new Example(Dictionary.class);
    		de.createCriteria().andEqualTo("dictionaryTypeId",dictionaryVo.getDictionaryTypeId()).andEqualTo("value",dictionaryVo.getValue());
    		int countValue = dictionaryMapper.selectCountByExample(de);
    		if(countValue>0) {
    			throw new ServiceException("同一字典类型不能存在相同的字典值！");
    		}
        	dictionary.setId(IdGenerator.nextId());
        	Date date = new Date();
        	dictionary.setCreateTime(date);
        	dictionary.setCreateUser(userVo.getAccount());
        	dictionary.setUpdateTime(date);
        	dictionary.setUpdateUser(userVo.getAccount());

        	dictionary.setChName(dictionaryVo.getChName());
        	dictionary.setEnName(dictionaryVo.getEnName());

            dictionaryMapper.insertSelective(dictionary);
        }
    }

    @Override
    public void delDictionary(String id) {
        dictionaryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delDictionaries(List<String> ids) {
        Example de = new Example(Dictionary.class);
        de.createCriteria().andIn("id",ids);
        dictionaryMapper.deleteByExample(de);

    }

    @Override
    public DictionaryVo getDictionaryById(String id) {
        DictionaryVo dicVo = dictionaryMapper.queryDictionaryInfoById(id);
        return dicVo;
    }

    @Override
    public PageInfo<DictionaryVo> getDictionaryPage(PageParam pageParam, DictionaryParamVo paramVo) {
        PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
        List<DictionaryVo> list = dictionaryMapper.queryDictionaryVoPage(paramVo);
        return new PageInfo<DictionaryVo>(list);
    }

	@Override
	public List<DictionaryVo> getDictionaryByCnName(String cnName) {
		/*Example de = new Example(Dictionary.class);
		de.createCriteria().andEqualTo("chName",cnName);
		List<Dictionary> entity = dictionaryMapper.selectByExample(de);
		List<DictionaryVo> list=BeanHelper.convertList(DictionaryVo.class, entity);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("chName", cnName);
		List<DictionaryVo> list=dictionaryMapper.selectByParam(param);
		return list;
	}

	@Override
	public List<DictionaryVo> getDictionaryByEnName(String enName) {
		/*Example de = new Example(Dictionary.class);
		de.createCriteria().andEqualTo("enName",enName);
		List<Dictionary> entity = dictionaryMapper.selectByExample(de);
		List<DictionaryVo> list =BeanHelper.convertList(DictionaryVo.class, entity);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("enName", enName);
		List<DictionaryVo> list=dictionaryMapper.selectByParam(param);
		return list;
	}

	@Override
	public List<DictionaryVo> getDictionaryByVlueAndType(DictionaryVo dictionaryVo) {
		/*Example de = new Example(Dictionary.class);
		de.createCriteria().andEqualTo("dictionaryTypeId",dictionaryVo.getDictionaryTypeId()).andEqualTo("value",dictionaryVo.getValue());
		List<Dictionary> entity = dictionaryMapper.selectByExample(de);
		List<DictionaryVo> list =BeanHelper.convertList(DictionaryVo.class, entity);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dictionaryTypeId", dictionaryVo.getDictionaryTypeId());
		param.put("value", dictionaryVo.getValue());
		List<DictionaryVo> list=dictionaryMapper.selectByParam(param);
		
		return list;
	}
	
	@Override
	public List<DictionaryVo> getDictionaryByEnNameAndType(DictionaryVo dictionaryVo) {
		/*Example de = new Example(Dictionary.class);
		de.createCriteria().andEqualTo("dictionaryTypeId",dictionaryVo.getDictionaryTypeId()).andEqualTo("enName",dictionaryVo.getEnName());
		List<Dictionary> entity = dictionaryMapper.selectByExample(de);
		List<DictionaryVo> list =BeanHelper.convertList(DictionaryVo.class, entity);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dictionaryTypeId", dictionaryVo.getDictionaryTypeId());
		param.put("enName", dictionaryVo.getEnName());
		List<DictionaryVo> list=dictionaryMapper.selectByParam(param);
		return list;
	}

	@Override
	public List<DictionaryVo> getDictionaryList(String[] searchType) {
		Map<String,Object> param =new HashMap<String,Object>();
		List<String> searchTypes = Arrays.asList(searchType);
		param.put("searchTypes", searchTypes);
		return dictionaryMapper.selectDictionaryList(param);
	}

	@Override
	public void batchInsertDictionarys(List<DictionaryTypeImportVo> dictionaryImportVos) {
		List<DictionaryType> type_list = new ArrayList<DictionaryType>();
		List<Dictionary> dict_list = new ArrayList<Dictionary>();
		for(DictionaryTypeImportVo typeVo : dictionaryImportVos) {
			DictionaryType type = Convert.convert(DictionaryType.class, typeVo);
			type.setId(IdGenerator.nextId());
			type.setCreateTime(new Date());
			//type.setCreateUser("");
			type_list.add(type);
			List<DictionaryImportVo> dictVos = typeVo.getDictionarys();
			for(DictionaryImportVo dictVo : dictVos) {
				Dictionary dict = Convert.convert(Dictionary.class, dictVo);
				dict.setId(IdGenerator.nextId());
				dict.setDictionaryTypeId(type.getId());
				dict.setEnable(1);
				dict.setCreateTime(new Date());
				//dict.setCreateUser("");
				dict_list.add(dict);
			}
		}
		dictionaryTypeMapper.insertList(type_list);
		dictionaryMapper.insertList(dict_list);
	}

	@Override
	public List<DictionaryVo> getDictionaryList(List<String> searchTypes) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("searchTypes", searchTypes);
		return dictionaryMapper.selectDictionaryList(param);
	}

	@Override
	public Map<String, List<DictionaryVo>> getDictionaryMap(List<String> searchTypes) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("searchTypes", searchTypes);
		List<DictionaryVo> dics = dictionaryMapper.selectDictionaryList(param);
		
		Map<String,List<DictionaryVo>> dicMap = dics.stream().collect(Collectors.groupingBy(DictionaryVo::getTypeEhName,Collectors.toList()));
		return dicMap;
	}
}
