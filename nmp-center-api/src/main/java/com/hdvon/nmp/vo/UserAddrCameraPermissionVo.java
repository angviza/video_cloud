package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户拥有资源地址树摄像机 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="userAddrCameraPermission")
public class UserAddrCameraPermissionVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "id，有d_x为摄像机节点，需要摄像机id时，使用deviceId值")
    private String id;

    @ApiModelProperty(value = "地址名称/摄像机名称")
    private String name;

    @ApiModelProperty(value = "当前地址父id")
    private String pid;
    
    @ApiModelProperty(value = "摄像机id,null是地址数节点，不为null是为摄像机节点")
    private String deviceId;
    
    @ApiModelProperty(value = "该用户已拥护摄像机操作权限值,'1,2,4,8..'")
    private String permissionValue;
	

}
