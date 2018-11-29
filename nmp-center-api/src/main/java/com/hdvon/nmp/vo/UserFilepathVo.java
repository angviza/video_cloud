package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>个人信息设置表 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="UserFilepath")
public class UserFilepathVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;

    @ApiModelProperty(value = "客户端mac地址")
    private java.lang.String macId;

    @ApiModelProperty(value = "文件储存路径路径")
    private java.lang.String filePath;

    @ApiModelProperty(value = "图片存储路径")
    private java.lang.String imgPath;

    @ApiModelProperty(value = "视频协议方式，(TCP,UDP)默认为TCP")
    private java.lang.String protocol;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

