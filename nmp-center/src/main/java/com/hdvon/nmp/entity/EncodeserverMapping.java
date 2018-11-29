package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>编码器关联中间表(t_encodeserver_mapping) 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_encodeserver_mapping")
public class EncodeserverMapping implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 关联编码器表id db_column: encodeserver_id 
     */
    @Column(name = "encodeserver_id")
	private String encodeserverId;

    /**
     * 关联地址表id db_column: address_id 
     */
    @Column(name = "address_id")
	private String addressId;

    /**
     * projectId db_column: project_id 
     */
    @Column(name = "project_id")
	private String projectId;


}

