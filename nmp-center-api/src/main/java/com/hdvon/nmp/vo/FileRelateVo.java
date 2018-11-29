package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>文件关联表 VO类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="FileRelate")
public class FileRelateVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private java.lang.String id;

    @ApiModelProperty(value = "t_attach_file文件表Id")
    private java.lang.String fileId;

    @ApiModelProperty(value = "来源关联文件的表ID")
    private java.lang.String relateId;


}

