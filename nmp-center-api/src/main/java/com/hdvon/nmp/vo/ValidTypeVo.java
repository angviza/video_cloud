package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="ValidTypeVo")
public class ValidTypeVo {
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型名称")
	private String chName;
    
    @ApiModelProperty(value = "类型值")
	private String value;
    
    @ApiModelProperty(value = "是否选中")
	private boolean selected;
}
