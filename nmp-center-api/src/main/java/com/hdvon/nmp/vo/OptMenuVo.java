package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 功能：修改自定义菜单的请求参数
 * 作者：zhanqf
 * 日期：2018年5月28日
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 *
 */
@Data
@ApiModel(value ="OptMenuVo")
public class OptMenuVo {

    @ApiModelProperty(value = "id")
	private String id;

    @ApiModelProperty(value = "名称")
	private String name;

    @ApiModelProperty(value = "同级排序")
	private Integer orderOpt;

}
