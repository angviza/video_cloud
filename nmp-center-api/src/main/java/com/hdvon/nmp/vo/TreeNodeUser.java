package com.hdvon.nmp.vo;
/**
 * 树节点部门+用户树类
 * @author guoweijun
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value ="TreeNodeUser")
public class TreeNodeUser extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "用户级别")
    private String userLevel;
}
