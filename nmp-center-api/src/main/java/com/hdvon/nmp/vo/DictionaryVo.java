package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>数据字典表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Dictionary")
public class DictionaryVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "关联字典类型表id")
    private String dictionaryTypeId;

    @ApiModelProperty(value = "字典中文名")
    private String chName;

    @ApiModelProperty(value = "字典英文名")
    private String enName;

    @ApiModelProperty(value = "字段值")
    private String value;

    @ApiModelProperty(value = "是否有效标志：1表示是，0表示否")
    private Integer enable;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    private Integer orderby;
    
    
    @ApiModelProperty(value = "字典类型中文名称")
    private String typeChName;
    
    @ApiModelProperty(value = "字典类型英文名称")
    private String typeEhName;
    
    /**扩展使用**/

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/


}

