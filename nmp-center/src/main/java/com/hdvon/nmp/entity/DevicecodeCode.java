package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>设备编码器生成编码 实体类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_devicecode_code")
public class DevicecodeCode implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 设备编码编号 db_column: device_code 
     */
    @Column(name = "device_code")
	private java.lang.String deviceCode;

    /**
     * devicecodeOptionId db_column: devicecode_option_id 
     */
    @Column(name = "devicecode_option_id")
	private java.lang.String devicecodeOptionId;

    /**
     * 是否使用（0-否，1-是） db_column: status 
     */
	private java.lang.Integer status;


}

