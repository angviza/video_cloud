package com.hdvon.nmp.vo;
/**
 * 部门树类
 * @author guoweijun
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value ="TreeNodeDepartment")
public class TreeNodeDepartment extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否建设部门，1.是,0.否")
    private Integer isConstructor;

    @ApiModelProperty(value = "是否承建单位，1.是,0.否")
    private Integer isBuilder;
    
    @ApiModelProperty(value = "部门自定义编号")
    private String splitCode;
}
