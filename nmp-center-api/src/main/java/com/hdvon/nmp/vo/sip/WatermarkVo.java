package com.hdvon.nmp.vo.sip;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>视频监控水印信息 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="WatermarkVo")
public class WatermarkVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "身份证id")
    private String cardId;
    
    @ApiModelProperty(value = "平台名称,截图时需要用")
    private String systemName;

}
