package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>信令服务器关联表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="ProjectMapping")
public class ProjectMappingVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联项目id")
    private java.lang.String projectId;

    @ApiModelProperty(value = "关联信令中心服务器id")
    private java.lang.String sigserverId;

    @ApiModelProperty(value = "关联项目名称")
    private java.lang.String projectName;
    
    @ApiModelProperty(value = "关联项目编号")
    private java.lang.String projectCode;


}

