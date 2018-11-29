package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>部门表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Department")
public class DepartmentVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "父id")
    private String pid;

    @ApiModelProperty(value = "部门代码")
    private String depCode;

    @ApiModelProperty(value = "父级部门代码")
    private String parentDepCode;
    
    @ApiModelProperty(value = "自定义部门代码")
    private String depCodeSplit;
    
    @ApiModelProperty(value = "是否建设部门(1.是，0.否)")
    private Integer isConstructor;

    @ApiModelProperty(value = "是否承建单位(1.是，0.否)")
    private Integer isBuilder;

    @ApiModelProperty(value = "联系人")
    private String contactor;

    @ApiModelProperty(value = "电话号码")
    private String telelno;

    @ApiModelProperty(value = "mobile")
    private String mobile;

    @ApiModelProperty(value = "同级排序")
    private Integer orderby;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "所属组织机构")
    private String orgId;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

    @ApiModelProperty(value = "父级部门名称")
    private String parentDepName;

    @ApiModelProperty(value = "所属组织机构名称")
    private String orgName;
    
    @ApiModelProperty(value = "部门树显示是否选中(1是；0否)")
    private String status;
    
    @ApiModelProperty(value = "节点类型")
    private String nodeType;

//    @ApiModelProperty(value = "部门用户数量")
//    private Integer userTotal;
    
    
}

