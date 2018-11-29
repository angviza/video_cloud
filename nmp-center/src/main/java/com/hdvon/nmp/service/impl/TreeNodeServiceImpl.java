package com.hdvon.nmp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.TreeNodeMapper;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>地址表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 * @param
 */
@Service
@Slf4j
public class TreeNodeServiceImpl implements ITreeNodeService {
	
	@Autowired
	private TreeNodeMapper treeNodeMapper;

    @Override
    public List<TreeNode> getAddressTree(UserVo userVo, String pid) {
        Map<String,Object> param = new HashMap<>();
        param.put("pid",pid);
        return treeNodeMapper.selectAddressTree(param);
    }

    @Override
    public List<TreeNodeCamera> getAddressCameraTree(UserVo userVo, String pid, String deviceType, String name) {
        if(name == null || "".equals(name)){
            /**普通异步加载**/
            if (StrUtil.isEmpty(pid)) {
                pid = "0";
            }
            Map<String, Object> param = new HashMap<>();
            param.put("pid", pid);
            param.put("extends", "camera");
            param.put("isAdmin", userVo.isAdmin());
            param.put("loginUserId", userVo.getId());
            if (deviceType != null && !"".equals(deviceType)) {
                //可控球机条件
                String BALL_TYPE = "ball";
                if (BALL_TYPE.equals(deviceType)) {
                    List<String> deviceTypes = Arrays.asList(new String[]{"1", "2"});
                    param.put("deviceTypes", deviceTypes);
                }
            }
            return treeNodeMapper.selectAddressCameraTree(param);
        }else{
            /**树关键字搜索**/
            //搜索摄像机
            Map<String, Object> param = new HashMap<>();
            param.put("name",name);
            param.put("isAdmin", userVo.isAdmin());
            param.put("loginUserId", userVo.getId());
            if (deviceType != null && !"".equals(deviceType)) {
                //可控球机条件
                String BALL_TYPE = "ball";
                if (BALL_TYPE.equals(deviceType)) {
                    List<String> deviceTypes = Arrays.asList(new String[]{"1", "2"});
                    param.put("deviceTypes", deviceTypes);
                }
            }
            List<TreeNodeCamera> list = treeNodeMapper.selectCamera(param);
            Set<String> addressIdSet = new HashSet();

            //取出父节点
            for (TreeNodeCamera treeNodeCamera : list) {
                if(treeNodeCamera.getPid() != null){
                    addressIdSet.add(treeNodeCamera.getPid());
                }
            }

            //搜索所有地址
            Map<String, Object> groupParam = new HashMap<>();
            groupParam.put("name",name);
            groupParam.put("isAdmin", userVo.isAdmin());
            groupParam.put("loginUserId", userVo.getId());
            List<TreeNode> addressList = treeNodeMapper.selectAddressTree(groupParam);

            Map<String,TreeNode> addressMap = new HashMap<>();
            for (TreeNode treeNode : addressList) {
                addressMap.put(treeNode.getId(),treeNode);
                //将地址树名称也加入搜索匹配
                if(treeNode.getName().indexOf(name) != -1){
                    addressIdSet.add(treeNode.getId());
                }
            }

            //根据id列表取出所有地址树父节点
            Iterator iterator = addressIdSet.iterator();
            Map<String,String> record = new HashMap<>();
            while (iterator.hasNext()) {
                String curPid = iterator.next().toString();
                TreeNode node = addressMap.get(curPid);
                if(node != null && record.get(node.getId()) == null){
                    list.add(TreeNodeCamera.fromTreeNode(node));
                    record.put(node.getId(),"");
                    while(node != null){
                        node = addressMap.get(node.getPid());
                        if(node != null && record.get(node.getId()) == null){
                            list.add(TreeNodeCamera.fromTreeNode(node));
                            record.put(node.getId(),"");
                        }
                    }
                }
            }
            return list;
        }
    }

    @Override
    public List<TreeNodeCamera> getGroupCameraTree(UserVo userVo, String pid, String name) {
        if(name == null || "".equals(name)){
            /**普通异步加载**/
            if (StrUtil.isEmpty(pid)) {
                pid = "0";
            }
            Map<String, Object> param = new HashMap<>();
            param.put("pid", pid);
            param.put("extends", "camera");
            param.put("isAdmin", userVo.isAdmin());
            param.put("loginUserId", userVo.getId());
            return treeNodeMapper.selectGroupCameraTree(param);
        }else{
            /**关键字搜索**/
            //搜索摄像机
            Map<String, Object> param = new HashMap<>();
            param.put("name",name);
            param.put("isAdmin", userVo.isAdmin());
            param.put("loginUserId", userVo.getId());
            List<TreeNodeCamera> list = treeNodeMapper.selectGroupCamera(param);
            Set<String> groupIdSet = new HashSet();
            //取出父节点
            for (TreeNodeCamera treeNodeCamera : list) {
                if(treeNodeCamera.getPid() != null){
                    groupIdSet.add(treeNodeCamera.getPid());
                }
            }

            //搜索所有分组
            Map<String, Object> groupParam = new HashMap<>();
            groupParam.put("name",name);
            groupParam.put("isAdmin", userVo.isAdmin());
            groupParam.put("loginUserId", userVo.getId());
            List<TreeNode> groupList = treeNodeMapper.selectGroupTree(groupParam);
            Map<String,TreeNode> groupMap = new HashMap<>();
            for (TreeNode treeNode : groupList) {
                groupMap.put(treeNode.getId(),treeNode);
                //将分组名称也加入搜索匹配
                if(treeNode.getName().indexOf(name) != -1){
                    groupIdSet.add(treeNode.getId());
                }
            }

            //根据id列表取出所有分组树父节点
            Iterator iterator = groupIdSet.iterator();
            Map<String,String> record = new HashMap<>();
            while (iterator.hasNext()) {
                String curPid = iterator.next().toString();
                TreeNode node = groupMap.get(curPid);
                if(node != null && record.get(node.getId()) == null){
                    list.add(TreeNodeCamera.fromTreeNode(node));
                    record.put(node.getId(),"");
                    while(node != null){
                        node = groupMap.get(node.getPid());
                        if(node != null && record.get(node.getId()) == null){
                            list.add(TreeNodeCamera.fromTreeNode(node));
                            record.put(node.getId(),"");
                        }
                    }
                }
            }
            return list;
        }

    }

    @Override
    public List<TreeNode> getAddressEncoderTree(UserVo userVo, String pid, String name) {
        if(name == null || "".equals(name)){
            /**普通异步加载**/
            if (StrUtil.isEmpty(pid)) {
                pid = "0";
            }
            Map<String, Object> param = new HashMap<>();
            param.put("pid", pid);
            return treeNodeMapper.selectAddressEncoderTree(param);
        }else{
            /**树关键字搜索**/
            //搜索编码器
            Map<String, Object> param = new HashMap<>();
            param.put("name",name);
            param.put("isAdmin", userVo.isAdmin());
            param.put("loginUserId", userVo.getId());
            List<TreeNode> list = treeNodeMapper.selectEncoder(param);
            Set<String> addressIdSet = new HashSet();

            //取出父节点
            for (TreeNode treeNode : list) {
                if(treeNode.getPid() != null){
                    addressIdSet.add(treeNode.getPid());
                }
            }

            //搜索所有地址
            Map<String, Object> groupParam = new HashMap<>();
            groupParam.put("name",name);
            groupParam.put("isAdmin", userVo.isAdmin());
            groupParam.put("loginUserId", userVo.getId());
            List<TreeNode> addressList = treeNodeMapper.selectAddressTree(groupParam);

            Map<String,TreeNode> addressMap = new HashMap<>();
            for (TreeNode treeNode : addressList) {
                addressMap.put(treeNode.getId(),treeNode);
                //将地址树名称也加入搜索匹配
                if(treeNode.getName().indexOf(name) != -1){
                    addressIdSet.add(treeNode.getId());
                }
            }

            //根据id列表取出所有地址树父节点
            Iterator iterator = addressIdSet.iterator();
            Map<String,String> record = new HashMap<>();
            while (iterator.hasNext()) {
                String curPid = iterator.next().toString();
                TreeNode node = addressMap.get(curPid);
                if(node != null && record.get(node.getId()) == null){
                    list.add(node);
                    record.put(node.getId(),"");
                    while(node != null){
                        node = addressMap.get(node.getPid());
                        if(node != null && record.get(node.getId()) == null){
                            list.add(node);
                            record.put(node.getId(),"");
                        }
                    }
                }
            }
            return list;
        }

    }

    @Override
    public List<TreeNodeDepartment> getDepartmentTree(UserVo userVo, String pid) {
        Map<String,Object> param = new HashMap<>();
        param.put("pid",pid);
        param.put("isAdmin",userVo.isAdmin());
        param.put("loginUserId",userVo.getId());
        param.put("extends","department");
        return treeNodeMapper.selectDepartmentTree(param);
    }

    @Override
    public List<TreeNodeUser> getDepartmentUserTree(UserVo userVo, String pid) {
        Map<String,Object> param = new HashMap<>();
        //param.put("pid",pid);
        param.put("extends","user");
        param.put("isAdmin",userVo.isAdmin());
        param.put("loginUserId",userVo.getId());
        return treeNodeMapper.selectDepartmentUserTree(param);
    }

    @Override
    public List<TreeNodeUser> getSysRoleUserTree(UserVo userVo, String pid) {
        Map<String,Object> param = new HashMap<>();
        //param.put("pid",pid);
        param.put("extends","user");
        param.put("isAdmin",userVo.isAdmin());
        param.put("loginUserId",userVo.getId());
        return treeNodeMapper.selectSysRoleUserTree(param);
    }

    @Override
    public List<TreeNodeOrg> getOrganizationTree(UserVo userVo, Boolean hasVirtual) {
        Map<String,Object> param = new HashMap<>();
        param.put("hasVirtual",hasVirtual == null ? true: hasVirtual);
        return treeNodeMapper.selectOrganizationTree(param);
    }

    @Override
    public List<TreeNode> getProjectTree(UserVo userVo) {
        Map<String,Object> param = new HashMap<>();
        return treeNodeMapper.selectProjectTree(param);
    }

    @Override
    public List<TreeNode> getGroupTree(UserVo userVo) {
        Map<String,Object> param = new HashMap<>();
        return treeNodeMapper.selectGroupTree(param);
    }

    @Override
    public List<TreeNode> getResourceRoleTree(UserVo userVo) {
        Map<String,Object> param = new HashMap<>();
        return treeNodeMapper.selectResourceRoleTree(param);
    }

    @Override
    public List<TreeNode> getSysRoleTree(UserVo userVo) {
        Map<String,Object> param = new HashMap<>();
        return treeNodeMapper.selectSysRoleTree(param);
    }

	@Override
	public List<TreeNode> getChildNodesByCode(String code, String type) {
		 Map<String,Object> param = new HashMap<>();
		 param.put("code", code);
		 param.put("type", type);
		return treeNodeMapper.selectChildNodesByCode(param);
	}

	@Override
	public List<TreeNodeDepartment> getDeptChildNodesByCode(String code, String type) {
		 Map<String,Object> param = new HashMap<>();
		 param.put("code", code);
		 param.put("type", type);
		return treeNodeMapper.selectDeptChildNodesByCode(param);
	}
	
}
