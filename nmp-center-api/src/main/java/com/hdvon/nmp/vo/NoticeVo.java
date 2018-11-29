package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>通知公告表 VO类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Notice")
public class NoticeVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "主题")
    private java.lang.String theme;

    @ApiModelProperty(value = "公告类型Id")
    private java.lang.String noticeTypeId;

    @ApiModelProperty(value = "正文")
    private java.lang.String content;

    @ApiModelProperty(value = "显示方式")
    private java.lang.Integer showMethod;

    @ApiModelProperty(value = "设备树")
    private java.lang.String deviceTree;

    @ApiModelProperty(value = "设备ID")
    private java.lang.String deviceId;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String deviceCode;

    @ApiModelProperty(value = "设备名称")
    private java.lang.String deviceName;

    @ApiModelProperty(value = "设备端口")
    private java.lang.String devicePort;

    @ApiModelProperty(value = "设置类型，0 立即发布，1 定时发布，2 发布时效")
    private java.lang.Integer settingType;

    @ApiModelProperty(value = "发布时效")
    private java.lang.String timeSetting;

    @ApiModelProperty(value = "发布时效结束")
    private java.lang.String timeSettingEnd;

    @ApiModelProperty(value = "收件人id")
    private java.lang.String userIds;
    
    @ApiModelProperty(value = "收件人集合")
    private List<UserVo> userVos;
    
    @ApiModelProperty(value = "附件集合ID")
    private java.lang.String fileIds;

    @ApiModelProperty(value = "附件集合ID")
    private List<AttachFileVo> attachFileVos;

    @ApiModelProperty(value = "公告阅读标志，1 为已读，0为未读")
    private java.lang.Integer flag;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "未读数")
    private java.lang.Integer count;

    /*@ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;*/

    @ApiModelProperty(value = "创建人")
    private java.lang.String createUser;

    @ApiModelProperty(value = "创建人部门id")
    private String departmentId;

    @ApiModelProperty(value = "创建人所属部门名称")
    private String departmentName;

    /*@ApiModelProperty(value = "更新人")
    private java.lang.String updateUser;*/


}

