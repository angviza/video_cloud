package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>公共树查询类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ITreeNodeService {

    /**
     * 查询地址树
     * @param userVo
     * @param pid
     * @return
     */
    List<TreeNode> getAddressTree(UserVo userVo, String pid);

    /**
     * 查询地址和摄像机树
     * @param userVo
     * @param pid
     * @param deviceType 设备类型
     * @param name 名称模糊搜索
     * @return
     */
    List<TreeNodeCamera> getAddressCameraTree(UserVo userVo, String pid , String deviceType , String name);


    /**
     * 查询分组和摄像机树
     * @param userVo
     * @param pid
     * @param name 名称模糊搜索
     * @return
     */
    List<TreeNodeCamera> getGroupCameraTree(UserVo userVo, String pid , String name);

    /**
     * 查询编码器树
     * @param userVo
     * @param pid
     * @param name
     * @return
     */
    List<TreeNode> getAddressEncoderTree(UserVo userVo, String pid , String name);

    /**
     * 查询部门树
     * @param userVo
     * @param pid
     * @return
     */
    List<TreeNodeDepartment> getDepartmentTree(UserVo userVo, String pid);

    /**
     * 查询部门用户树
     * @param userVo
     * @param pid
     * @return
     */
    List<TreeNodeUser> getDepartmentUserTree(UserVo userVo, String pid);


    /**
     * 查询系统角色用户树
     * @param userVo
     * @param pid
     * @return
     */
    List<TreeNodeUser> getSysRoleUserTree(UserVo userVo, String pid);

    /**
     * 查询行政区划树
     * @param userVo
     * @param hasVirtual 是否包含虚拟组织
     * @return
     */
    List<TreeNodeOrg> getOrganizationTree(UserVo userVo, Boolean hasVirtual);

    /**
     * 查询项目树
     * @param userVo
     * @return
     */
    List<TreeNode> getProjectTree(UserVo userVo);

    /**
     * 查询分组树
     * @param userVo
     * @return
     */
    List<TreeNode> getGroupTree(UserVo userVo);


    /**
     * 查询资源角色树
     * @param userVo
     * @return
     */
    List<TreeNode> getResourceRoleTree(UserVo userVo);


    /**
     * 查询系统角色树
     * @param userVo
     * @return
     */
    List<TreeNode> getSysRoleTree(UserVo userVo);
    
    /**
     * 根据节点编号查询当前节点以及子节点的集合
     * @param code 当前节点编号
     * @param type 树类型（address:地址树；）
     * @return List<TreeNode> 节点集合
     */
    List<TreeNode> getChildNodesByCode(String code, String type);
    
    List<TreeNodeDepartment> getDeptChildNodesByCode(String code, String type);
}
