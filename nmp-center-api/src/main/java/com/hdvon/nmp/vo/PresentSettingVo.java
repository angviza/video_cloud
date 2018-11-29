package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value ="PresentPosition")
public class PresentSettingVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1858244555958821516L;


	@ApiModelProperty(value = "预置位列表")
    private List<PresentPositionVo> presentPositions;

    @ApiModelProperty(value = "最大预置位编号")
    private java.lang.Integer maxNo;
}
