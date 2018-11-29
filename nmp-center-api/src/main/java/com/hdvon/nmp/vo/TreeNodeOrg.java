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
@ApiModel(value ="TreeNodeOrg")
public class TreeNodeOrg extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否虚拟组织，1.是,0.否")
    private Integer isVirtual;
    
    @ApiModelProperty(value = "业务分组id")
    private String bussGroupId;
}
