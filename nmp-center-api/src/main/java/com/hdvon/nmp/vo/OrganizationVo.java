package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>组织机构表/行政区划(国标) VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Organization")
public class OrganizationVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "机构名称")
    private java.lang.String name;

    @ApiModelProperty(value = "父id")
    private java.lang.String pid;

    @ApiModelProperty(value = "机构代码")
    private java.lang.String orgCode;

    @ApiModelProperty(value = "父级机构代码")
    private java.lang.String parentOrgCode;
    
    @ApiModelProperty(value = "根节点到当前节点code拼接")
    private java.lang.String orgCodeSplit;
    
    @ApiModelProperty(value = "是否虚拟组织，1.是,0.否")
    private java.lang.Integer isVirtual;

    @ApiModelProperty(value = "机构类型")
    private java.lang.Integer orgType;

    @ApiModelProperty(value = "业务分组id")
    private java.lang.String bussGroupId;

    @ApiModelProperty(value = "节点层级，区分是否虚拟组织，例如：3级行政区划下的虚拟组织为1级虚拟组织（用于限制可添加虚拟组织的最多层级）")
    private java.lang.Integer level;

    @ApiModelProperty(value = "描述")
    private java.lang.String description;

    @ApiModelProperty(value = "同级排序")
    private java.lang.Integer orderby;
    
    @ApiModelProperty(value = "父级机构名称")
    private String parentOrgName;
    
    @ApiModelProperty(value = "业务分组名称")
    private String bussGroupName;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    @ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;
}

