package com.hdvon.nmp.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DictionaryTypeVo;
import com.hdvon.nmp.vo.UserVo;

public interface IDictionaryTypeService {

	/**
	 * 增加或者编辑数据字典类型
	 * @param typeVo 数据字典类型vo
	 */
	void editDictionaryType(UserVo userVo, DictionaryTypeVo typeVo);
	/**
	 * 单个删除数据字典类型
	 * @param id 数据字典类型id
	 */
	void delDictionaryType(String id);
	/**
	 * 批量删除数据字典类型
	 * @param ids
	 */
	void delDictionaryTypes(List<String> ids);
	/**
	 * 根据字典类型id查询字典类型
	 * @param id 字典类型id
	 * @return
	 */
	DictionaryTypeVo getDictionaryTypeById(String id);
	/**
	 * 分页查询字典类型列表
	 * @param pageParam
	 * @param search
	 * @return
	 */
	PageInfo<DictionaryTypeVo> getDictionaryTypePage(PageParam pageParam,String search);
	/**
	 * 根据条件查询字典类型列表
	 * @param search
	 * @return
	 */
	List<DictionaryTypeVo> getDictionaryTypeList(String search);
	/**
	 * 根据字典类型中文名称查询字典类型
	 * @param name
	 * @return
	 */
	List<DictionaryTypeVo> getDictionaryTypeByCnName(String cnName);
	/**
	 * 根据字典类型英文名称查询字典类型
	 * @param name
	 * @return
	 */
	List<DictionaryTypeVo> getDictionaryTypeByEnName(String enName);
}
