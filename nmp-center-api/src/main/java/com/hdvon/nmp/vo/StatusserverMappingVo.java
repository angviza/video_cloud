package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>状态服务器关联项目表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="StatusserverMapping")
public class StatusserverMappingVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联项目id")
    private java.lang.String projectId;

    @ApiModelProperty(value = "关联状态服务器id")
    private java.lang.String statusserverId;

    @ApiModelProperty(value = "关联项目名称")
    private java.lang.String projectName;

    @ApiModelProperty(value = "关联项目编号")
    private java.lang.String projectCode;

}

