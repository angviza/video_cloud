package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import com.hdvon.nmp.common.CameraPermissionVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>业务分组关联设备初始化 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraBussinessGroupInitVo")
public class CameraBussinessGroupInitVo implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "地址摄像机")
	List<CameraPermissionVo> addressCamera;
	
	@ApiModelProperty(value = "业务分组关联的摄像机")
	List<CameraBussinessGroupVo> bussinessGroup;

}
