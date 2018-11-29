package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>编码器关联中间表(t_encodeserver_mapping) VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="EncodeserverMapping")
public class EncodeserverMappingVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "关联编码器表id")
    private String encodeserverId;

    @ApiModelProperty(value = "关联地址表id")
    private String addressId;

    @ApiModelProperty(value = "projectId")
    private java.lang.String projectId;
}

