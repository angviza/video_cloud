package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户行为日志表 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="UserLog")
public class UserLogVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "操作人姓名")
    private java.lang.String name;

    @ApiModelProperty(value = "操作人账号")
    private java.lang.String account;

    @ApiModelProperty(value = "操作对象")
    private java.lang.String operationObject;

    @ApiModelProperty(value = "视频操作类型 1 实时播放 ;2  录像回放;3 录像下载；4 云台控制")
    private java.lang.String type;

    @ApiModelProperty(value = "操作类型")
    private java.lang.String operationType;

    @ApiModelProperty(value = "日志类型（菜单）")
    private java.lang.String menuId;

    @ApiModelProperty(value = "用户token")
    private java.lang.String tokenId;

    @ApiModelProperty(value = "操作内容")
    private java.lang.String content;

    @ApiModelProperty(value = "操作时间")
    private java.util.Date operationTime;

    @ApiModelProperty(value = "menuName")
    private java.lang.String menuName;
    
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    
    @ApiModelProperty(value = "操作类型")
    private java.lang.String typeName;

    @ApiModelProperty(value = "响应时间")
    private java.lang.Long responseTime;

    @ApiModelProperty(value = "是否同步")
    private java.lang.String isSync;

}

