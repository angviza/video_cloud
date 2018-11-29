package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户公告映射表 VO类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="UserNotice")
public class UserNoticeVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "用户ID")
    private java.lang.String userId;

    @ApiModelProperty(value = "公告ID")
    private java.lang.String noticeId;

    @ApiModelProperty(value = "是否已读，1为已读，0为未读")
    private java.lang.Integer flag;


}

