package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.AddressVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>地址表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IAddressService{

    /**
     * 按分页查询地址列表
     * @param treeNodeChildren
     * @param pageParam
     * @return
     */
    public PageInfo<AddressVo> getAddressListByPage(AddressVo addressVo, TreeNodeChildren treeNodeChildren , PageParam pageParam);


    /**
     * 根据id查询地址信息
     * @param id
     * @return
     */
    public AddressVo getAddressById(String id);

    /**
     * 保存地址
     * @param addressVo
     */
    public void saveAddress(UserVo userVo , AddressVo addressVo);

    /**
     * 删除地址
     * @param userVo
     * @param addressIds
     */
    public void deleteAddress(UserVo userVo , List<String> addressIds);


	/**
	 * 批量保存地址数据
	 * @param addresses
	 */
	public void batchInsertAddresses(List<Map<String,Object>> addresses, String[] titles);

	/**
	 * 处理id pid关系
	 * @param list
	 * @param titles
	 */
	List<Map<String,Object>> transpCodeToId(List<Map<String,Object>> list,String[] titles);

	/**
	 * 导出地址数据
	 * @param seach
	 */
	public List<AddressVo> exportAddresses(String seach,TreeNodeChildren treeNodeChildren);

	/**
	 * 查询带是否选中状态的所有地址列表
	 * @param addressId
	 * @return
	 */
	List<AddressVo> getStatusAddressList(String addressId);
	
}
