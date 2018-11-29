package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 树节点基类
 * @author guoweijun
 */
@Data
@ApiModel(value ="TreeNode",description = "树节点")
public class TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "树节点id")
    private String id;

    @ApiModelProperty(value = "树节点名称")
    private String name;

    @ApiModelProperty(value = "父节点")
    private String pid;

    @ApiModelProperty(value = "节点编码")
    private String code;
    
    @ApiModelProperty(value = "根节点到当前节点拼接的编号")
    private String splitCode;

    @ApiModelProperty(value = "节点类型")
    private String nodeType;

}