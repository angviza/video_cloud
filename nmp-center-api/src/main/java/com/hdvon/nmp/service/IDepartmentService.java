package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;
import com.hdvon.nmp.vo.DepartmentVo;
import com.hdvon.nmp.vo.ValidAttrVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>部门表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IDepartmentService{

    /**
     * 保存部门
     * @param userVo
     * @param department
     */
    void saveDepartment(UserVo userVo, DepartmentVo department);

    /**
     * 分页查询部门
     * @param pp
     * @param treeNodeChildren
     * @param deptVo
     * @return
     */
    PageInfo<DepartmentVo> getDeptPages(PageParam pp, TreeNodeChildren treeNodeChildren, DepartmentVo deptVo);
    /**
     * 批量删除部门，要删除所有子节点
     * @param ids 选中要删除的所有组织机构id的集合
     */
    void delDepartmentsByIds(List<String> ids);
    /**
     * 根据id查询单个机构信息
     * @param curId
     * @return
     */
    DepartmentVo getDepartmentById(String curId);


    /**
     * 查询部门列表
     * @param id
     * @return
     */
    public List<DepartmentVo> getDeptList(Map<String,Object> map);

    public void batchInsertDepartments(List<Map<String,String>> list, List<CheckAttributeVo> checkVos, UserVo userVo, Map<String,List<String>> relateIdMap);

    /**
     * 当前用户能看到的部门
     * @param userId
     * @return
     */
    public List<DepartmentVo> getUserDepartments(String userId);
    
    
    /**
     * 根据用户查询可授权部门用户树列表
     * @param userId 用户id
     * @param pid 父节点部门id
     * @param isAdmin 是否是超管
     * @return
     */
    public List<DepartmentUserTreeVo> getUserAuthorizeDeptTree(String userId);

    
	List<DepartmentVo> getDepartmentByPid(String pid);
	
	/**
	 * 部门用户数量
	 * @param param
	 * @param pageParam
	 * @return
	 */

	PageInfo<DepartmentVo> getDepartmentUserCount(Map<String, Object> param, PageParam pageParam);

	/**
	 * 查询部门列表
	 * @param departmentVo
	 * @param treeNodeChildren
	 * @return
	 */
	List<DepartmentVo> getDepartmentList(DepartmentVo departmentVo, TreeNodeChildren treeNodeChildren);

}
