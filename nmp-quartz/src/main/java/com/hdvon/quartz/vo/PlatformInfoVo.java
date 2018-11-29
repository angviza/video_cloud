package com.hdvon.quartz.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class PlatformInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

    private java.lang.Long id;
    
    private java.lang.String userName;

    private java.lang.String deviceCode;

    private java.util.Date operateTime;

    private java.lang.String operateType;
    
}

