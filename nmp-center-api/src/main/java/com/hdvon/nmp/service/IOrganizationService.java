package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.OrganizationVo;
import com.hdvon.nmp.vo.ValidAttrVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>组织机构表/行政区划(国标) 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IOrganizationService{

    /**
     * 增加组织结构
     * @param organization 当增加根机构时，organizationVo.pid=0；当增加子机构时，organizationVo.pid为当前机构的id
     */
    void addOrganization(UserVo userVo, OrganizationVo organization);
    /**
     * 删除组织机构，同时删除当前组织机构下的子机构和组织机构下的人员
     * @param curId 当前要删除的组织机构的id
     */
    void delOrganizationsById(String curId);
    /**
     * 修改当前组织机构的信息,不用修改关联人员信息
     * @param organization 更新的组织机构信息
     */
    void updateOrgInfoById(OrganizationVo organization);
    /**
     * 分页查询组织机构列表
     * @param pageNo
     * @param pageSize
     * @param search
     * @return
     */
    PageInfo<OrganizationVo> getOrgPages(PageParam pp, TreeNodeChildren treeNodeChildren, OrganizationVo orgVo);
    /**
     * 批量删除组织机构，要删除所有子节点
     * @param ids 选中要删除的所有组织机构id的集合
     */
    void delOrganizationsByIds(List<String> ids);
    /**
     * 根据id查询单个组织机构信息，不用关联查询人员
     * @param curId
     * @return
     */
    OrganizationVo getOrganizationById(String curId);
    /**
     * 批量保存组织机构数据
     * @param list
     * @param checkVos
     * @param isVirtual
     */
    void batchInsertOrgs(List<Map<String,String>> list,List<CheckAttributeVo> checkVos, String isVirtual, UserVo userVo, Map<String,List<String>> relateIdMap);
    
	/**
	 * 查询组织机构列表
	 * @param pram
	 * @param treeNodeChildren
	 * @return
	 */
	List<OrganizationVo> getOrgList(Map<String, Object> pram, TreeNodeChildren treeNodeChildren);
}
