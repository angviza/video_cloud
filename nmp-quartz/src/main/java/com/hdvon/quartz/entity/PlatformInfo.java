package com.hdvon.quartz.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b> 实体类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_platform_info")
public class PlatformInfo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.Long id;
    
    /**
     * userName db_column: user_name 
     */
    @Column(name = "user_name")
	private java.lang.String userName;

    /**
     * deviceCode db_column: device_code 
     */
    @Column(name = "device_code")
	private java.lang.String deviceCode;

    /**
     * operateTime db_column: operate_time 
     */
    @Column(name = "operate_time")
	private java.util.Date operateTime;

    /**
     * operateType db_column: operate_type 
     */
    @Column(name = "operate_type")
	private java.lang.String operateType;

}

