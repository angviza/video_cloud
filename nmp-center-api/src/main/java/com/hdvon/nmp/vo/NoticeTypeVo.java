package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <br>
 * <b>功能：</b>公告类型表 VO类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="NoticeType")
public class NoticeTypeVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "公告类型名")
    private String name;

    @ApiModelProperty(value = "公告图标")
    private String icon;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "未读数")
    private Integer unread;

    @ApiModelProperty(value = "公告文本")
    private String content;

    @ApiModelProperty(value = "发布选择时间")
    private String timeSetting;

    @ApiModelProperty(value = "发布时间")
    private java.util.Date createTime;
}

