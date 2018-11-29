package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DictionaryParamVo;
import com.hdvon.nmp.vo.DictionaryTypeImportVo;
import com.hdvon.nmp.vo.DictionaryVo;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>数据字典表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IDictionaryService{

	/**
	 * 增加或者编辑数据字典
	 * @param dictionaryVo 数据字典vo
	 */
	void editDictionary(UserVo userVo, DictionaryVo dictionaryVo);
	/**
	 * 单个删除数据字典
	 * @param id 数据字典id
	 */
	void delDictionary(String id);
	/**
	 * 批量删除数据字典
	 * @param ids 数据字典id集合
	 */
	void delDictionaries(List<String> ids);

	/**
	 * 根据id查询数据字典信息
	 * @param id 数据字典id
	 * @return
	 */
	DictionaryVo getDictionaryById(String id);
	/**
	 * 分页查询数据字典数据
	 * @param pageParam 分页参数
	 * @param paramVo 查询参数
	 * @return
	 */
	PageInfo<DictionaryVo> getDictionaryPage(PageParam pageParam, DictionaryParamVo paramVo);
	/**
	 * 根据字典中文名查询字典
	 * @param cnName
	 * @return
	 */
	List<DictionaryVo> getDictionaryByCnName(String cnName);
	/**
	 * 根据字典英文名查询字典
	 * @param enName
	 * @return
	 */
	List<DictionaryVo> getDictionaryByEnName(String enName);
	/**
	 * 根据字典类型和字典值查询字典
	 * @param dictionaryVo
	 * @return
	 */
	List<DictionaryVo> getDictionaryByVlueAndType(DictionaryVo dictionaryVo);
	/**
	 * 根据字典类型和字典英文名称查询字典
	 * @param dictionaryVo
	 * @return
	 */
	public List<DictionaryVo> getDictionaryByEnNameAndType(DictionaryVo dictionaryVo);
	/**
	 * 根据字典类型英文名查询字典列表
	 * @param searchType
	 * @return
	 */
	List<DictionaryVo> getDictionaryList(String[] searchType);
	
	/**
	 * 根据字典类型英文名查询字典列表
	 * @param searchType
	 * @return
	 */
	List<DictionaryVo> getDictionaryList(List<String> searchTypes);
	
	/**
	 * 批量保存字典数据
	 * @param dictionaryImportVo
	 * @param titles
	 */
	void batchInsertDictionarys(List<DictionaryTypeImportVo> dictionaryImportVos);
	
	/**
	 * 根据字典类型英文名查询字典列表，并按照类型分类
	 * @param searchTypes
	 * @return
	 */
	Map<String,List<DictionaryVo>> getDictionaryMap(List<String> searchTypes);

}
