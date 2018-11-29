package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value ="Dictionary")
public class DictionaryParamVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3511079233370864783L;

	@ApiModelProperty(value = "字典名称（中文名或者英文名）")
    private String name;

    @ApiModelProperty(value = "字典类型")
    private String dictionaryTypeId;
}
