package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 树节点摄像机类
 * @author guoweijun
 */
@Data
@ApiModel(value ="TreeNodeCamera")
public class TreeNodeCamera extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "摄像机id")
    private String cameraId;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    public static TreeNodeCamera fromTreeNode(TreeNode node){
        TreeNodeCamera treeNodeCamera = new TreeNodeCamera();
        treeNodeCamera.setId(node.getId());
        treeNodeCamera.setName(node.getName());
        treeNodeCamera.setPid(node.getPid());
        treeNodeCamera.setCode(node.getCode());
        treeNodeCamera.setNodeType(node.getNodeType());
        return treeNodeCamera;
    }
}
