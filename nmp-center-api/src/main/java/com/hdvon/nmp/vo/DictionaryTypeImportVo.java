package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="DictionaryTypeImport")
public class DictionaryTypeImportVo implements Serializable{
	private static final long serialVersionUID = 5343603295890219381L;
	
	@ApiModelProperty(value = "字典类型中文名")
	private String chName;
	
	@ApiModelProperty(value = "字典类型英文名")
	private String enName;
	
	@ApiModelProperty(value = "类型下的字典集合")
	private List<DictionaryImportVo> dictionarys;

}
