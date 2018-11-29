package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>一机一档 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Device")
public class DeviceVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;
    
    @ApiModelProperty(value = "registeredName")
    private java.lang.String registeredName;
    
    @ApiModelProperty(value = "registeredPass")
    private java.lang.String registeredPass;
    
    @ApiModelProperty(value = "registeredServerPort")
    private java.lang.String registeredServerPort;
    
    @ApiModelProperty(value = "设备编码")
    private java.lang.String sbbm;

    @ApiModelProperty(value = "设备/区域/系统名称")
    private java.lang.String sbmc;

    @ApiModelProperty(value = "行政区划")
    private java.lang.String xzqh;

    @ApiModelProperty(value = "父设备/区域/系统ID")
    private java.lang.String parentId;

    @ApiModelProperty(value = "子设备")
    private java.lang.Integer parental;

    @ApiModelProperty(value = "bussGroupId")
    private java.lang.String bussGroupId;

    @ApiModelProperty(value = "设备厂商")
    private java.lang.Integer sbcs;

    @ApiModelProperty(value = "摄像机类型")
    private java.lang.Integer sxjlx;

    @ApiModelProperty(value = "前端设备型号")
    private java.lang.String qdsbxh;

    @ApiModelProperty(value = "设备属性")
    private java.lang.Integer sbsx;

    @ApiModelProperty(value = "摄像机功能类型")
    private java.lang.Integer sxjgnlx;

    @ApiModelProperty(value = "摄像机补光属性")
    private java.lang.Integer bgsx;

    @ApiModelProperty(value = "智能分析")
    private java.lang.Integer znfx;

    @ApiModelProperty(value = "采集点类别")
    private java.lang.Integer cjdlb;

    @ApiModelProperty(value = "录像存储位置")
    private java.lang.String lxccwz;

    @ApiModelProperty(value = "存储托管运营商编码")
    private java.lang.Integer cctgyysbm;

    @ApiModelProperty(value = "存储托管运营商名称")
    private java.lang.String cctgyysmc;

    @ApiModelProperty(value = "存储托管运营商机房地址")
    private java.lang.String cctgyysjfdz;

    @ApiModelProperty(value = "录像存储设备IP")
    private java.lang.String lxccsbip;

    @ApiModelProperty(value = "录像存储设备国标ID号")
    private java.lang.String lxccsbgbid;

    @ApiModelProperty(value = "录像存储设备名称")
    private java.lang.String lxccsbmc;

    @ApiModelProperty(value = "录像存储设备登录用户名")
    private java.lang.String lxccsdlyhm;

    @ApiModelProperty(value = "录像存储设备登录密码")
    private java.lang.String lxccsbdlmm;

    @ApiModelProperty(value = "录像存储设备端口号")
    private java.lang.String lxccsbdkh;

    @ApiModelProperty(value = "录像存储设备总通道数")
    private java.lang.Integer lxccsbztds;

    @ApiModelProperty(value = "存储系统通道号")
    private java.lang.Integer ccxttdh;

    @ApiModelProperty(value = "录像存储设备厂家")
    private java.lang.Integer lxccsbcj;

    @ApiModelProperty(value = "录像存储设备类型")
    private java.lang.Integer lxccsblx;

    @ApiModelProperty(value = "警区")
    private java.lang.String block;

    @ApiModelProperty(value = "安装地址")
    private java.lang.String azdz;

    @ApiModelProperty(value = "门牌地址")
    private java.lang.String mpdz;

    @ApiModelProperty(value = "立杆编号")
    private java.lang.String lgbh;

    @ApiModelProperty(value = "周边标志")
    private java.lang.String zbbz;

    @ApiModelProperty(value = "经度")
    private Double jd;

    @ApiModelProperty(value = "纬度")
    private Double wd;

    @ApiModelProperty(value = "摄像机位置类型")
    private java.lang.Integer wzlx;

    @ApiModelProperty(value = "安装方式")
    private java.lang.Integer azfs;

    @ApiModelProperty(value = "杆高")
    private java.lang.String gg;

    @ApiModelProperty(value = "臂长")
    private java.lang.String bc;

    @ApiModelProperty(value = "横臂方向")
    private java.lang.String hbfx;

    @ApiModelProperty(value = "取电方式")
    private java.lang.String qdfs;

    @ApiModelProperty(value = "射域描述")
    private java.lang.String syms;

    @ApiModelProperty(value = "监控范围截图")
    private java.lang.String jkfwjt;

    @ApiModelProperty(value = "监控实景")
    private java.lang.String jksj;

    @ApiModelProperty(value = "摄像机安装位置室内外")
    private java.lang.Integer snwsx;

    @ApiModelProperty(value = "摄像机监控方位")
    private java.lang.Integer sxjjkfw;

    @ApiModelProperty(value = "关联摄像机")
    private java.lang.String glsxj;

    @ApiModelProperty(value = "是否共杆")
    private java.lang.Integer sfgg;

    @ApiModelProperty(value = "信令安全模式")
    private java.lang.Integer safetyWay;

    @ApiModelProperty(value = "注册方式")
    private java.lang.Integer registerWay;

    @ApiModelProperty(value = "保密属性")
    private java.lang.Integer secrecy;

    @ApiModelProperty(value = "证书序列号")
    private java.lang.String certNum;

    @ApiModelProperty(value = "证书有效标识")
    private java.lang.Integer certifiable;

    @ApiModelProperty(value = "无效原因码")
    private java.lang.Integer errCode;

    @ApiModelProperty(value = "证书终止有效期")
    private java.util.Date endTime;

    @ApiModelProperty(value = "联网属性")
    private java.lang.Integer lwsx;

    @ApiModelProperty(value = "接入网络")
    private java.lang.Integer jrwl;

    @ApiModelProperty(value = "设备/区域/系统IPv4地址")
    private java.lang.String ipv4;

    @ApiModelProperty(value = "设备/区域/系统IPv6地址")
    private java.lang.String ipv6;

    @ApiModelProperty(value = "设备MAC地址")
    private java.lang.String macAddress;

    @ApiModelProperty(value = "设备/区域/系统端口")
    private java.lang.Integer port;

    @ApiModelProperty(value = "设备用户名")
    private java.lang.String sbyhm;

    @ApiModelProperty(value = "设备口令")
    private java.lang.String password;

    @ApiModelProperty(value = "中心信令服务器IP/网关服务器IP")
    private java.lang.String gatewayip;

    @ApiModelProperty(value = "中心信令域/网关域")
    private java.lang.String gatewaydomain;

    @ApiModelProperty(value = "中心信令ID/网关ID")
    private java.lang.String gatewayid;

    @ApiModelProperty(value = "中心信令服务器操作系统登录用户名/网关服务器操作系统登录用户名")
    private java.lang.String osloginname;

    @ApiModelProperty(value = "中心信令服务器操作系统登录密码/网关服务器操作系统登录密码")
    private java.lang.String osloginkey;

    @ApiModelProperty(value = "启用时间")
    private java.util.Date qysj;

    @ApiModelProperty(value = "报废时间")
    private java.util.Date bfsj;

    @ApiModelProperty(value = "安装时间")
    private java.util.Date azsj;

    @ApiModelProperty(value = "设备状态")
    private java.lang.Integer sbzt;

    @ApiModelProperty(value = "视频丢失")
    private java.lang.Integer spds;

    @ApiModelProperty(value = "色彩失真")
    private java.lang.Integer scsz;

    @ApiModelProperty(value = "视频模糊")
    private java.lang.Integer spmh;

    @ApiModelProperty(value = "亮度异常")
    private java.lang.Integer ldyc;

    @ApiModelProperty(value = "视频干扰")
    private java.lang.Integer spgr;

    @ApiModelProperty(value = "视频卡顿")
    private java.lang.Integer spkd;

    @ApiModelProperty(value = "视频遮挡")
    private java.lang.Integer spzd;

    @ApiModelProperty(value = "场景变更")
    private java.lang.Integer cjbg;

    @ApiModelProperty(value = "在线时长")
    private java.lang.Integer zxsc;

    @ApiModelProperty(value = "离线时长")
    private java.lang.Integer lxsc;

    @ApiModelProperty(value = "信令时延")
    private java.lang.Integer xlsy;

    @ApiModelProperty(value = "视频流时延")
    private java.lang.Integer splsy;

    @ApiModelProperty(value = "关键帧时延")
    private java.lang.Integer gjzsy;

    @ApiModelProperty(value = "录像保存天数")
    private java.lang.Integer lxbcts;

    @ApiModelProperty(value = "下载倍速范围")
    private java.lang.String downloadSpeed;

    @ApiModelProperty(value = "文件路径名")
    private java.lang.String filepath;

    @ApiModelProperty(value = "录像地址")
    private java.lang.String recordAddress;

    @ApiModelProperty(value = "空域编码能力")
    private java.lang.Integer svcspaceSupportMode;

    @ApiModelProperty(value = "时域编码能力")
    private java.lang.Integer svctimeSupportMode;

    @ApiModelProperty(value = "视频编码格式")
    private java.lang.Integer videoEncodeType;

    @ApiModelProperty(value = "图像分辨率")
    private java.lang.Integer resolution;

    @ApiModelProperty(value = "图像帧率")
    private java.lang.Integer frameRate;

    @ApiModelProperty(value = "视频码率类型")
    private java.lang.Integer videoCodeType;

    @ApiModelProperty(value = "视频码率大小")
    private java.lang.Integer videoCodeRate;

    @ApiModelProperty(value = "码流类型")
    private java.lang.Integer streamType;

    @ApiModelProperty(value = "拾音器")
    private java.lang.Integer pickup;

    @ApiModelProperty(value = "音频编码格式")
    private java.lang.Integer audioEncodeType;

    @ApiModelProperty(value = "音频编码码率")
    private java.lang.Integer audioCodeRate;

    @ApiModelProperty(value = "音频采样率")
    private java.lang.Integer audioSampleRate;

    @ApiModelProperty(value = "监控点位类型")
    private java.lang.Integer jkdwlx;

    @ApiModelProperty(value = "建设类型")
    private java.lang.Integer jslx;

    @ApiModelProperty(value = "投资单位")
    private java.lang.String tzdw;

    @ApiModelProperty(value = "建设单位/设备归属")
    private java.lang.String jsdw;

    @ApiModelProperty(value = "建设单位/平台归属代码")
    private java.lang.Integer jsdwdm;

    @ApiModelProperty(value = "所属项目编号")
    private java.lang.String ssxmbh;

    @ApiModelProperty(value = "所属项目名称")
    private java.lang.String ssxmmc;

    @ApiModelProperty(value = "摄像机所属部门")
    private java.lang.Integer sxjssbm;

    @ApiModelProperty(value = "所属派出所")
    private java.lang.String sspcs;

    @ApiModelProperty(value = "所属街道")
    private java.lang.String ssjd;

    @ApiModelProperty(value = "承建单位")
    private java.lang.String cjdw;

    @ApiModelProperty(value = "维护单位")
    private java.lang.String whdw;

    @ApiModelProperty(value = "维护人")
    private java.lang.String whr;

    @ApiModelProperty(value = "维护人手机")
    private java.lang.String whrsj;

    @ApiModelProperty(value = "修改人")
    private java.lang.String xgr;

    @ApiModelProperty(value = "修改时间")
    private java.util.Date xgsj;


}

