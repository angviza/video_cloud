package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <br>
 * <b>功能：</b>部门用户树 VO类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-6-4<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Department")
public class DepartmentUserTreeVo implements Serializable{
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父id")
    private String pid;
    

    @ApiModelProperty(value = "父节点名称")
    private String parentName;

    @ApiModelProperty(value = "是否用户  1:是 0:否")
    private Integer isUser;
    
    @ApiModelProperty(value = "是否关联  1:是 0:否")
    private String isCheck;
    
    @ApiModelProperty(value = "节点类型")
    private String nodeType;

}
