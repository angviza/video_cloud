package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <br>
 * <b>功能：</b>项目分组 VO类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-5-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class OrganizationTreeVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3410053573868698838L;

	@ApiModelProperty(value = "地址id")
    private String id;//地址id

    @ApiModelProperty(value = "地址名称")
    private String name;//地址名称

    @ApiModelProperty(value = "当前地址父id")
    private String pid;//当前地址父id
    
    @ApiModelProperty(value = "类型 1:行政区域  2:地址  3:项目  4:自定义分组")
    private Integer type;//类型 1:行政区域  2:地址  3:项目  4:自定义分组
    
    @ApiModelProperty(value = "")
    private String depCode;
    
    @ApiModelProperty(value = "是否项目 1:是 0:否")
    private Integer isProject;//是否项目 1:是 0:否
    
    @ApiModelProperty(value = "是否关联预案 1:是 0:否")
    private Integer status;//是否选中
}
