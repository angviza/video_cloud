package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="TreeNodeChildren",description = "树节点子节点")
public class TreeNodeChildren implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -8934357372401369364L;
	
	//地址
	@ApiModelProperty(value = "地址树节点集合")
    private List<TreeNode> addressNodes;
	
	@ApiModelProperty(value = "地址树节点id集合")
    private List<String> addressNodeIds = new ArrayList<String>();
	
	//行政区划、虚拟组织
	@ApiModelProperty(value = "行政区划树节点集合")
    private List<TreeNode> orgNodes;
	
	@ApiModelProperty(value = "行政区划树节点id集合")
    private List<String> orgNodeIds = new ArrayList<String>();
	
	//项目分组
	@ApiModelProperty(value = "项目分组树节点集合")
    private List<TreeNode> projectNodes;
	
	@ApiModelProperty(value = "项目分组树节点id集合")
    private List<String> projectNodeIds = new ArrayList<String>();
	
	//自定义分组
	@ApiModelProperty(value = "自定义分组树节点集合")
    private List<TreeNode> groupNodes;
	
	@ApiModelProperty(value = "自定义分组树节点id集合")
    private List<String> groupNodeIds = new ArrayList<String>();
	
	//部门树
	@ApiModelProperty(value = "部门树节点集合")
    private List<TreeNodeDepartment> deptNodes;
	
	@ApiModelProperty(value = "部门树节点id集合")
    private List<String> deptNodeIds = new ArrayList<String>();
	
	//系统角色
	@ApiModelProperty(value = "系统角色树节点集合")
    private List<TreeNode> sysroleNodes;
	
	@ApiModelProperty(value = "系统角色树节点id集合")
    private List<String> sysroleNodeIds = new ArrayList<String>();
	
	//资源角色
	@ApiModelProperty(value = "资源角色树节点集合")
    private List<TreeNode> resroleNodes;
	
	@ApiModelProperty(value = "资源角色树节点id集合")
    private List<String> resroleNodeIds = new ArrayList<String>();
	
	public List<TreeNode> getAddressNodes() {
		return addressNodes;
	}

	public void setAddressNodes(List<TreeNode> addressNodes) {
		this.addressNodes = addressNodes;
		for(TreeNode treeNode : addressNodes) {
			this.addressNodeIds.add(treeNode.getId());
		}
	}
	
	public List<TreeNode> getOrgNodes() {
		return orgNodes;
	}

	public void setOrgNodes(List<TreeNode> orgNodes) {
		this.orgNodes = orgNodes;
		for(TreeNode treeNode : orgNodes) {
			this.orgNodeIds.add(treeNode.getId());
		}
	}

	public List<TreeNode> getProjectNodes() {
		return projectNodes;
	}

	public void setProjectNodes(List<TreeNode> projectNodes) {
		this.projectNodes = projectNodes;
		for(TreeNode treeNode : projectNodes) {
			this.projectNodeIds.add(treeNode.getId());
		}
	}

	public List<TreeNode> getGroupNodes() {
		return groupNodes;
	}

	public void setGroupNodes(List<TreeNode> groupNodes) {
		this.groupNodes = groupNodes;
		for(TreeNode treeNode : groupNodes) {
			this.groupNodeIds.add(treeNode.getId());
		}
	}

	public List<TreeNodeDepartment> getDeptNodes() {
		return deptNodes;
	}

	public void setDeptNodes(List<TreeNodeDepartment> deptNodes) {
		this.deptNodes = deptNodes;
		for(TreeNode treeNode : deptNodes) {
			this.deptNodeIds.add(treeNode.getId());
		}
	}

	public List<TreeNode> getSysroleNodes() {
		return sysroleNodes;
	}

	public void setSysroleNodes(List<TreeNode> sysroleNodes) {
		this.sysroleNodes = sysroleNodes;
		for(TreeNode treeNode : sysroleNodes) {
			this.sysroleNodeIds.add(treeNode.getId());
		}
	}

	public List<TreeNode> getResroleNodes() {
		return resroleNodes;
	}

	public void setResroleNodes(List<TreeNode> resroleNodes) {
		this.resroleNodes = resroleNodes;
		for(TreeNode treeNode : resroleNodes) {
			this.resroleNodeIds.add(treeNode.getId());
		}
	}

}
