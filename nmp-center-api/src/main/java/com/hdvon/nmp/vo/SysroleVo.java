package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>系统角色表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Sysrole")
public class SysroleVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "是否角色授权(1.是，0.否)")
    private Integer isMenuAssigned;

    @ApiModelProperty(value = "是否权限配置(1.是，0.否)")
    private Integer isPermAssigned;

    @ApiModelProperty(value = "同级排序")
    private Integer orderby;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "父id")
    private String pid;
    
    @ApiModelProperty(value = "权限预案id")
    private String permissionplanId;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

    @ApiModelProperty(value = "是否选中状态（0：未选中，1：选中）")
    private String status;//0:未选中，1：选中

    @ApiModelProperty(value = "上级名称")
    private String parentName;
    
    @ApiModelProperty(value = "用户数量")
    private Integer userTotal;
    
    
    
    


}
