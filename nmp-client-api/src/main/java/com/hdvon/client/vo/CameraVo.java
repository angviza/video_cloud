package com.hdvon.client.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class CameraVo implements Serializable{

	private static final long serialVersionUID = 1L;

    private String id;

    private Integer hots;//访问热度

    private java.lang.String cameraIp;//摄像机ip地址

    private Integer accessMode;//接入方式:1国标0非国标

    private String addressName;//地址树名称

    private String sbmc;//设备名称
    
    private String sbbm;//设备编码
    
    private String azdz;//设备安装地址

    private int sxjlx;//设备类别

    private int sbzt;//状态

    private String encodeServerName;//所属编码器

    private String deviceId;//设备id

    private String addressId;//地址id
    private String addresssName;//所属项目名称
    
    private String encoderServerId;//编码器表id

    private String projectId;//摄像机工程id
    private String projectName;//所属项目名称
    
    private String orgId;//摄像机组织机构id
    private String orgName;//所属项目名称
    
    private String cameraGroupId;//摄像机所在分组id
    private String cameraGroupName;//所属项目名称
    
    private String jsdw;//建设单位

    private String cjdw;//承建单位

    private int zcztCount;//摄像机正常状态
    
    private String matrixChannelId;//矩阵通道id

	
    private String lxccsbip;//录像存储设备IP
    private String lxccsbgbid;//录像存储设备国标ID号
    private String ccxttdh;//存储系统通道号
    private String mpdz;//门牌地址
    private Double jd;//经度
    private Double wd;//纬度
    
    private String bussGroupCode;//业务分组编码
    private String wzlx;//摄像机位置类型
}

