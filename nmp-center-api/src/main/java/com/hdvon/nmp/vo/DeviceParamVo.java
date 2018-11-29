package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>一机一档扩展 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="deviceParamVo")
public class DeviceParamVo implements Serializable{

	private static final long serialVersionUID = 1L;

	 //扩展属性
    @ApiModelProperty(value = "接入方式:1国标0非国标")
    private Integer accessMode;
    
    @ApiModelProperty(value = "关联地址表的id")
    private String addressId;

    @ApiModelProperty(value = "关联编码器表的id")
    private String encoderServerId;
    
    @ApiModelProperty(value = "关联摄像机工程")
    private String projectId;
    
    @ApiModelProperty(value = "关联行政区划id")
    private String orgId;
    
    @ApiModelProperty(value = "所属编码器")
    private String encodeServerName;

    @ApiModelProperty(value = "所在分组名称")
    private String groupName;
    
    @ApiModelProperty(value = "所属地址树名称")
    private String addressName;
    
    @ApiModelProperty(value = "所属行政区划名称")
    private String orgName;
    
    @ApiModelProperty(value = "所属项目名称")
    private String projectName;
  
    @ApiModelProperty(value = "摄像机所在分组名称 （ 维护在业务分组关联摄像机）")
    private String bussinessGroupName;
    
    @ApiModelProperty(value = "注册用户名")
    private String registeredName;

    @ApiModelProperty(value = "注册用户密码")
    private String registeredPass;

    @ApiModelProperty(value = "注册服务器端口号")
    private Integer registeredServerPort;
    
    
    //以下是一机一档表
    @ApiModelProperty(value = "设备id")
    private String id;

    @ApiModelProperty(value = "设备编码")
    private String sbbm;

    @ApiModelProperty(value = "设备/区域/系统名称")
    private String sbmc;

    @ApiModelProperty(value = "行政区划")
    private String xzqh;

    @ApiModelProperty(value = "父设备/区域/系统ID")
    private String parentId;

    @ApiModelProperty(value = "虚拟组织所属业务分组ID")
    private String businessGroupId;

    @ApiModelProperty(value = "子设备")
    private Integer parental;

    @ApiModelProperty(value = "设备厂商")
    private Integer sbcs;

    @ApiModelProperty(value = "摄像机类型")
    private Integer sxjlx;

    @ApiModelProperty(value = "前端设备型号")
    private String qdsbxh;

    @ApiModelProperty(value = "设备属性")
    private Integer sbsx;

    @ApiModelProperty(value = "摄像机功能类型")
    private Integer sxjgnlx;

    @ApiModelProperty(value = "摄像机补光属性")
    private Integer bgsx;

    @ApiModelProperty(value = "智能分析")
    private Integer znfx;

    @ApiModelProperty(value = "采集点类别")
    private Integer cjdlb;

    @ApiModelProperty(value = "录像存储位置")
    private String lxccwz;

    @ApiModelProperty(value = "存储托管运营商编码")
    private Integer cctgyysbm;

    @ApiModelProperty(value = "存储托管运营商名称")
    private String cctgyysmc;

    @ApiModelProperty(value = "存储托管运营商机房地址")
    private String cctgyysjfdz;

    @ApiModelProperty(value = "录像存储设备IP")
    private String lxccsbip;

    @ApiModelProperty(value = "录像存储设备国标ID号")
    private String lxccsbgbid;

    @ApiModelProperty(value = "录像存储设备名称")
    private String lxccsbmc;

    @ApiModelProperty(value = "录像存储设备登录用户名")
    private String lxccsdlyhm;

    @ApiModelProperty(value = "录像存储设备登录密码")
    private String lxccsbdlmm;

    @ApiModelProperty(value = "录像存储设备端口号")
    private String lxccsbdkh;

    @ApiModelProperty(value = "录像存储设备总通道数")
    private Integer lxccsbztds;

    @ApiModelProperty(value = "存储系统通道号")
    private Integer ccxttdh;

    @ApiModelProperty(value = "录像存储设备厂家")
    private Integer lxccsbcj;

    @ApiModelProperty(value = "录像存储设备类型")
    private Integer lxccsblx;

    @ApiModelProperty(value = "警区")
    private String block;

    @ApiModelProperty(value = "安装地址")
    private String azdz;

    @ApiModelProperty(value = "门牌地址")
    private String mpdz;

    @ApiModelProperty(value = "立杆编号")
    private String lgbh;

    @ApiModelProperty(value = "周边标志")
    private String zbbz;

    @ApiModelProperty(value = "经度")
    private Double jd;

    @ApiModelProperty(value = "纬度")
    private Double wd;

    @ApiModelProperty(value = "摄像机位置类型")
    private Integer wzlx;

    @ApiModelProperty(value = "安装方式")
    private Integer azfs;

    @ApiModelProperty(value = "杆高")
    private String gg;

    @ApiModelProperty(value = "臂长")
    private String bc;

    @ApiModelProperty(value = "横臂方向")
    private String hbfx;

    @ApiModelProperty(value = "取电方式")
    private String qdfs;

    @ApiModelProperty(value = "射域描述")
    private String syms;

    @ApiModelProperty(value = "监控范围截图")
    private String jkfwjt;

    @ApiModelProperty(value = "监控实景")
    private String jksj;

    @ApiModelProperty(value = "摄像机安装位置室内外")
    private Integer snwsx;

    @ApiModelProperty(value = "摄像机监控方位")
    private Integer sxjjkfw;

    @ApiModelProperty(value = "关联摄像机")
    private String glsxj;

    @ApiModelProperty(value = "是否共杆")
    private Integer sfgg;

    @ApiModelProperty(value = "信令安全模式")
    private Integer safetyWay;

    @ApiModelProperty(value = "注册方式")
    private Integer registerWay;

    @ApiModelProperty(value = "保密属性")
    private Integer secrecy;

    @ApiModelProperty(value = "证书序列号")
    private String certNum;

    @ApiModelProperty(value = "证书有效标识")
    private Integer certifiable;

    @ApiModelProperty(value = "无效原因码")
    private Integer errCode;

    @ApiModelProperty(value = "证书终止有效期")
    private java.util.Date endTime;

    @ApiModelProperty(value = "联网属性")
    private Integer lwsx;

    @ApiModelProperty(value = "接入网络")
    private Integer jrwl;
    
    @ApiModelProperty(value = "摄像机ip地址(设备/区域/系统IPv4地址)")
    private String cameraIp;

    /*@ApiModelProperty(value = "设备/区域/系统IPv4地址")
    private String ipv4;*/

    @ApiModelProperty(value = "设备/区域/系统IPv6地址")
    private String ipv6;

    @ApiModelProperty(value = "设备MAC地址")
    private String macAddress;

    @ApiModelProperty(value = "设备/区域/系统端口")
    private Integer port;

    @ApiModelProperty(value = "设备用户名")
    private String sbyhm;

    @ApiModelProperty(value = "设备口令")
    private String password;

    @ApiModelProperty(value = "中心信令服务器IP/网关服务器IP(注册服务器IP)")
    private String gatewayip;

    @ApiModelProperty(value = "中心信令域/网关域(注册服务器域名)")
    private String gatewaydomain;

    @ApiModelProperty(value = "中心信令ID/网关ID(注册服务器ID)")
    private String gatewayid;

    @ApiModelProperty(value = "中心信令服务器操作系统登录用户名/网关服务器操作系统登录用户名")
    private String osloginname;

    @ApiModelProperty(value = "中心信令服务器操作系统登录密码/网关服务器操作系统登录密码")
    private String osloginkey;

    @ApiModelProperty(value = "启用时间")
    private java.util.Date qysj;

    @ApiModelProperty(value = "报废时间")
    private java.util.Date bfsj;

    @ApiModelProperty(value = "安装时间")
    private java.util.Date azsj;

    @ApiModelProperty(value = "设备状态")
    private Integer sbzt;

    @ApiModelProperty(value = "视频丢失")
    private Integer spds;

    @ApiModelProperty(value = "色彩失真")
    private Integer scsz;

    @ApiModelProperty(value = "视频模糊")
    private Integer spmh;

    @ApiModelProperty(value = "亮度异常")
    private Integer ldyc;

    @ApiModelProperty(value = "视频干扰")
    private Integer spgr;

    @ApiModelProperty(value = "视频卡顿")
    private Integer spkd;

    @ApiModelProperty(value = "视频遮挡")
    private Integer spzd;

    @ApiModelProperty(value = "场景变更")
    private Integer cjbg;

    @ApiModelProperty(value = "在线时长")
    private Integer zxsc;

    @ApiModelProperty(value = "离线时长")
    private Integer lxsc;

    @ApiModelProperty(value = "信令时延")
    private Integer xlsy;

    @ApiModelProperty(value = "视频流时延")
    private Integer splsy;

    @ApiModelProperty(value = "关键帧时延")
    private Integer gjzsy;

    @ApiModelProperty(value = "录像保存天数")
    private Integer lxbcts;

    @ApiModelProperty(value = "下载倍速范围")
    private String downloadSpeed;

    @ApiModelProperty(value = "文件路径名")
    private String filepath;

    @ApiModelProperty(value = "录像地址")
    private String recordAddress;

    @ApiModelProperty(value = "空域编码能力")
    private Integer svcspaceSupportMode;

    @ApiModelProperty(value = "时域编码能力")
    private Integer svctimeSupportMode;

    @ApiModelProperty(value = "视频编码格式")
    private Integer videoEncodeType;

    @ApiModelProperty(value = "图像分辨率")
    private Integer resolution;

    @ApiModelProperty(value = "图像帧率")
    private Integer frameRate;

    @ApiModelProperty(value = "视频码率类型")
    private Integer videoCodeType;

    @ApiModelProperty(value = "视频码率大小")
    private Integer videoCodeRate;

    @ApiModelProperty(value = "码流类型")
    private Integer streamType;

    @ApiModelProperty(value = "拾音器")
    private Integer pickup;

    @ApiModelProperty(value = "音频编码格式")
    private Integer audioEncodeType;

    @ApiModelProperty(value = "音频编码码率")
    private Integer audioCodeRate;

    @ApiModelProperty(value = "音频采样率")
    private Integer audioSampleRate;

    @ApiModelProperty(value = "监控点位类型")
    private Integer jkdwlx;

    @ApiModelProperty(value = "建设类型")
    private Integer jslx;

    @ApiModelProperty(value = "投资单位")
    private String tzdw;

    @ApiModelProperty(value = "建设单位/设备归属")
    private String jsdw;

    @ApiModelProperty(value = "建设单位/平台归属代码")
    private String jsdwdm;

    @ApiModelProperty(value = "所属项目编号")
    private String ssxmbh;

    @ApiModelProperty(value = "所属项目名称")
    private String ssxmmc;

    @ApiModelProperty(value = "摄像机所属部门")
    private Integer sxjssbm;

    @ApiModelProperty(value = "所属派出所")
    private String sspcs;

    @ApiModelProperty(value = "所属街道")
    private String ssjd;

    @ApiModelProperty(value = "承建单位")
    private String cjdw;

    @ApiModelProperty(value = "维护单位")
    private String whdw;

    @ApiModelProperty(value = "维护人")
    private String whr;

    @ApiModelProperty(value = "维护人手机")
    private String whrsj;
    
    @ApiModelProperty(value = "承建单位名称")
    private String cjdwName;
    
    @ApiModelProperty(value = "建设单位名称")
    private String jsdwName;

}

