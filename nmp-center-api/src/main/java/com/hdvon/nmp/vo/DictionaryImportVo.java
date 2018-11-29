package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="DictionaryImport")
public class DictionaryImportVo  implements Serializable{

	private static final long serialVersionUID = 4397743988876559284L;
	
	@ApiModelProperty(value = "字典中文名")
	private String chName;
	
	@ApiModelProperty(value = "字典值")
	private String value;
}
