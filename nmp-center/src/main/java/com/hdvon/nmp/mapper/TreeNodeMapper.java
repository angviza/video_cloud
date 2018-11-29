package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 树查询整合
 * @author guoweijun
 */
public interface TreeNodeMapper {

    /**
     * 查询地址树
     * @param param 参数
     * @return
     */
    List<TreeNode> selectAddressTree(Map<String, Object> param);

    /**
     * 查询地址和摄像机树
     * @param param 参数
     * @return
     */
    List<TreeNodeCamera> selectAddressCameraTree(Map<String, Object> param);


    /**
     * 查询分组和摄像机树
     * @param param 参数
     * @return
     */
    List<TreeNodeCamera> selectGroupCameraTree(Map<String, Object> param);


    /**
     * 查询编码器树
     * @param param 参数
     * @return
     */
    List<TreeNode> selectAddressEncoderTree(Map<String, Object> param);

    /**
     * 查询部门树
     * @param param 参数
     * @return
     */
    List<TreeNodeDepartment> selectDepartmentTree(Map<String, Object> param);

    /**
     * 查询部门用户树
     * @param param 参数
     * @return
     */
    List<TreeNodeUser> selectDepartmentUserTree(Map<String, Object> param);

    /**
     * 查询系统角色用户树
     * @param param 参数
     * @return
     */
    List<TreeNodeUser> selectSysRoleUserTree(Map<String, Object> param);

    /**
     * 查询行政区划树
     * @param param 参数
     * @return
     */
    List<TreeNodeOrg> selectOrganizationTree(Map<String, Object> param);

    /**
     * 查询项目树
     * @param param 参数
     * @return
     */
    List<TreeNode> selectProjectTree(Map<String, Object> param);

    /**
     * 查询分组树
     * @param param 参数
     * @return
     */
    List<TreeNode> selectGroupTree(Map<String, Object> param);


    /**
     * 查询资源角色树
     * @param param 参数
     * @return
     */
    List<TreeNode> selectResourceRoleTree(Map<String, Object> param);


    /**
     * 查询系统角色树
     * @param param 参数
     * @return
     */
    List<TreeNode> selectSysRoleTree(Map<String, Object> param);

    /**
     * 查询当前节点及子节点
     * @param param
     * @return
     */
    List<TreeNode> selectChildNodesByCode(Map<String, Object> param);
    
    /**
     * 修改子节点编码
     * @param param
     */
    void updateChildNodesCode(Map<String,Object> param);
    
    /**
     * 根据自定义部门编号查询部门子节点
     * @param param
     * @return
     */
    List<TreeNodeDepartment> selectDeptChildNodesByCode(Map<String, Object> param);



    /**
     * 单独查询分组关联的编码器
     * @param param
     * @return
     */
    List<TreeNode> selectEncoder(Map<String, Object> param);

    /**
     * 单独查询地址关联的摄像机
     * @param param
     * @return
     */
    List<TreeNodeCamera> selectCamera(Map<String, Object> param);

    /**
     * 单独查询分组关联的摄像机
     * @param param
     * @return
     */
    List<TreeNodeCamera> selectGroupCamera(Map<String, Object> param);
}
