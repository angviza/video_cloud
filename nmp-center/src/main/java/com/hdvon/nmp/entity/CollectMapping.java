package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>收藏夹关联表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_collect_mapping")
public class CollectMapping implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 关联收藏夹id db_column: collect_id 
     */
    @Column(name = "collect_id")
	private java.lang.String collectId;

    /**
     * 关联设备id db_column: device_id 
     */
    @Column(name = "device_id")
	private java.lang.String deviceId;

    /**
     * 设备名称 db_column: device_name 
     */
    @Column(name = "device_name")
	private java.lang.String deviceName;

    /**
     * 设备编码 db_column: device_sbbm 
     */
    @Column(name = "device_sbbm")
	private java.lang.String deviceSbbm;


}

