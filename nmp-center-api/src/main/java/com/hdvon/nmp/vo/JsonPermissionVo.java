package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>资源角色与地址树、摄像头权限关联表json值 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="jsonPermission")
public class JsonPermissionVo {
	@ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "权限类型：0 摄像机，1 地址树，2 分组树")
    private Integer type;

    @ApiModelProperty(value = "权限值，按2进制进行计算，'1,2,4,8,16'....")
    private String value;

}
