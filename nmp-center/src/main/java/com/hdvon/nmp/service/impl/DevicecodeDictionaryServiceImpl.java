package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.entity.DevicecodeDictionary;
import com.hdvon.nmp.mapper.DevicecodeDictionaryMapper;
import com.hdvon.nmp.service.IDevicecodeDictionaryService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DevicecodeDictionaryVo;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>设备编码生成字典表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class DevicecodeDictionaryServiceImpl implements IDevicecodeDictionaryService {

	@Autowired
	private DevicecodeDictionaryMapper devicecodeDictionaryMapper;

	@Override
	public void batchInsertDevicecodeDicts(List<Map<String, Object>> list, String[] titles) {
//		List<DevicecodeDictionary> devicecodeDicts = transMapToDevicecodeDicts(list, titles);
		/**
		 * 删除所有数据
		 */
		Example example = new Example(DevicecodeDictionary.class);
        devicecodeDictionaryMapper.deleteByExample(example);
		
		List<DevicecodeDictionary> ret = new ArrayList<DevicecodeDictionary>();
		/**
		 * 保存顶级目录
		 */
		List<DevicecodeDictionary> topList = new ArrayList<DevicecodeDictionary>();
		
		for(Map<String,Object> map : list) {
			DevicecodeDictionary dcd = new DevicecodeDictionary();
			String id = IdGenerator.nextId();
			dcd.setId(id);
			String position = map.get(titles[0])==null?"":map.get(titles[0]).toString();
			dcd.setPosition(position.trim());
			String code = map.get(titles[1])==null?"":map.get(titles[1]).toString();
			dcd.setCode(code.trim());
			String name = map.get(titles[2])==null?"":map.get(titles[2]).toString(); 
			dcd.setName(name.trim());
			String pCode = map.get(titles[3])==null?"":map.get(titles[3]).toString();
			dcd.setPcode(pCode.trim());
			dcd.setIsReserved(map.get(titles[4])==null?0:("是".equals(map.get(titles[4]).toString())?1:0));
			/**
			 * pCode为空或者 pCode ==0 则为顶级目录
			 */
			if(StringUtils.isEmpty(pCode) || "0".equals(pCode)) {
				dcd.setPid("0");
				dcd.setLevel(1);
				topList.add(dcd);
			}

			ret.add(dcd);
		}
		if(topList.isEmpty() || topList.size() == 0) {
			return;
		}
		/**
		 * 保存的数据列表
		 */
		List<DevicecodeDictionary> devicecodeDicts = new ArrayList<>(ret.size());
		topList.stream().forEach(dict->{
			devicecodeDicts.add(dict);
			/**
			 * 递归获取子级元素
			 */
			getChilderNode(devicecodeDicts, ret, dict);
		});
		
		devicecodeDictionaryMapper.insertList(devicecodeDicts);
	}
	/**
	 * 根据位置和编码获取当前节点pCode
	 * @param position 位置
	 * @param code 编码
	 * @return
	 */
	private String getpCode(String position,String code) {
		String key = position + "$" + code;
		return key;
	}
	/**
	 * 递归子级元素
	 * @param saveList 保存对象
	 * @param dataList 数据列表
	 * @param node 当前节点
	 */
	private void getChilderNode(List<DevicecodeDictionary> saveList,List<DevicecodeDictionary> dataList,DevicecodeDictionary node) {
		/**
		 * 当前节点pCode
		 */
		String pCode = getpCode(node.getPosition().trim(), node.getCode().trim());
		int level = node.getLevel();
		/**
		 * 根据pCode获取当前节点的子级元素
		 */
		List<DevicecodeDictionary> childList = dataList.stream().filter(dict->pCode.equals(dict.getPcode())).collect(Collectors.toList());
//		System.out.println(JSON.toJSONString(">>>>>>>>>>>>>>>>>>>>>>>>>>pCode:"+pCode+"===="+childList));
		if(childList.isEmpty() || childList.size() == 0) {
			return;
		}
		childList.stream().forEach(dict->{			
			/**
			 * 重置其父节点
			 */
			dict.setPid(node.getId());
			dict.setLevel(level+1);
			saveList.add(dict);
			/**
			 * 递归调用
			 */
			getChilderNode(saveList, dataList, dict);
		});
	}
	/**
	 * 设备代码字典的map集合数据转成实体集合
	 * @param list
	 * @param titles
	 * @return
	 */
	private List<DevicecodeDictionary> transMapToDevicecodeDicts(List<Map<String, Object>> list, String[] titles){
		List<DevicecodeDictionary> ret = new ArrayList<DevicecodeDictionary>();
		for(Map<String,Object> map : list) {
			DevicecodeDictionary dcd = new DevicecodeDictionary();
			dcd.setId(IdGenerator.nextId());
			dcd.setPosition(map.get(titles[0])==null?"":map.get(titles[0]).toString());
			dcd.setCode(map.get(titles[1])==null?"":map.get(titles[1]).toString());
			dcd.setName(map.get(titles[2])==null?"":map.get(titles[2]).toString());
			dcd.setPcode(map.get(titles[3])==null?"":map.get(titles[3]).toString());
			dcd.setIsReserved(map.get(titles[4])==null?0:("是".equals(map.get(titles[4]).toString())?1:0));
			ret.add(dcd);
		}
		return ret;
	}

	@Override
	public List<DevicecodeDictionaryVo> getDevicecodeDictByPosition(String position, String pCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取父子节点下的子节点
	 * @param vo请求对象
	 * @return
	 */
	@Override
	public List<DevicecodeDictionaryVo> getChildrens(DevicecodeDictionaryVo vo) {
		/*Example example= new Example(DevicecodeDictionary.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("pCode",pcode);
		criteria.andNotEqualTo("isReserved","1");
		List<DevicecodeDictionary> entity =devicecodeDictionaryMapper.selectByExample(example);
		List<DevicecodeDictionaryVo> list=BeanHelper.convertList(DevicecodeDictionaryVo.class, entity);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(vo.getPid())) {
			param.put("pid", vo.getPid());
		}
		if(StringUtils.isNotEmpty(vo.getPcode())) {
			param.put("pCode", vo.getPcode());
		}
		
		param.put("isReserved", "1");
		List<DevicecodeDictionaryVo> list=devicecodeDictionaryMapper.selectByParam(param);
		return list;
	}

	@Override
	public List<DevicecodeDictionaryVo> getList() {
		/*Example example= new Example(DevicecodeDictionary.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("pCode","");
		criteria.andNotEqualTo("isReserved","1");
		List<DevicecodeDictionary> entity =devicecodeDictionaryMapper.selectByExample(example);		
		List<DevicecodeDictionaryVo> list=BeanHelper.convertList(DevicecodeDictionaryVo.class, entity);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("ispCode", "yes");
		param.put("isReserved", "1");
		List<DevicecodeDictionaryVo> list=devicecodeDictionaryMapper.selectByParam(param);
		return list;
	}

	@Override
	public List<DevicecodeDictionaryVo> getAddrList(List<String> params) {
//		Example example= new Example(DevicecodeDictionary.class);
//		Example.Criteria criteria = example.createCriteria();
//		criteria.andIn("position", list);
		List<DevicecodeDictionaryVo> listCode=devicecodeDictionaryMapper.selectAddrList(params);
		return listCode;
	}

}
