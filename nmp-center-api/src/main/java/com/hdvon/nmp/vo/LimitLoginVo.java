package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户登录限制登录表 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="LimitLogin")
public class LimitLoginVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "限制类型（0.限制的IP,1.允许的IP,2.限制mac地址）")
    private java.lang.String type;

    @ApiModelProperty(value = "是否是区域IP（1.是，0.否）")
    private java.lang.String isRegasion;

    @ApiModelProperty(value = "是否开启（1.开启，0.关闭）")
    private java.lang.String state;

    @ApiModelProperty(value = "ip开始区域")
    private java.lang.String startRegaion;

    @ApiModelProperty(value = "ip结束区域")
    private java.lang.String endRegaion;

    @ApiModelProperty(value = "macName")
    private java.lang.String macName;

    @ApiModelProperty(value = "描述信息")
    private java.lang.String description;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

