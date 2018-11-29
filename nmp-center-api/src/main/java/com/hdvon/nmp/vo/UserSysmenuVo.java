package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户自定义菜单关联表 VO类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="UserSysmenu")
public class UserSysmenuVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
	private String id;

    @ApiModelProperty(value = "用户id")
	private String userId;

    @ApiModelProperty(value = "系统功能表id")
	private String sysmenuId;

    @ApiModelProperty(value = " 自定义菜单父节点id")
	private String sysmenuPid;

    @ApiModelProperty(value = "自定义菜单名称")
	private String sysmenuName;

    @ApiModelProperty(value = "是否隐藏")
	private Integer hide;

	/**
     * 同级排序 db_column: orderby 
	 */
    @ApiModelProperty(value = "同级排序")
	private Integer orderby;

}

