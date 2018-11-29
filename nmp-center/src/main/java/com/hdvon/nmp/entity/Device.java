package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>一机一档 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_device")
public class Device implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 设备编码 db_column: SBBM 
     */
    @Column(name = "SBBM")
	private String sbbm;

    /**
     * 设备/区域/系统名称 db_column: SBMC 
     */
    @Column(name = "SBMC")
	private String sbmc;

    /**
     * 行政区划 db_column: XZQH 
     */
    @Column(name = "XZQH")
	private String xzqh;

    /**
     * 父设备/区域/系统ID db_column: ParentID 
     */
    @Column(name = "ParentID")
	private String parentId;

    /**
     * 子设备 db_column: Parental 
     */
    @Column(name = "Parental")
	private Integer parental;

    /**
     * bussGroupId db_column: buss_group_id 
     */
    @Column(name = "buss_group_id")
	private String bussGroupId;

    /**
     * 设备厂商 db_column: SBCS 
     */
    @Column(name = "SBCS")
	private Integer sbcs;

    /**
     * 摄像机类型 db_column: SXJLX 
     */
    @Column(name = "SXJLX")
	private Integer sxjlx;

    /**
     * 前端设备型号 db_column: QDSBXH 
     */
    @Column(name = "QDSBXH")
	private String qdsbxh;

    /**
     * 设备属性 db_column: SBSX 
     */
    @Column(name = "SBSX")
	private Integer sbsx;

    /**
     * 摄像机功能类型 db_column: SXJGNLX 
     */
    @Column(name = "SXJGNLX")
	private Integer sxjgnlx;

    /**
     * 摄像机补光属性 db_column: BGSX 
     */
    @Column(name = "BGSX")
	private Integer bgsx;

    /**
     * 智能分析 db_column: ZNFX 
     */
    @Column(name = "ZNFX")
	private Integer znfx;

    /**
     * 采集点类别 db_column: CJDLB 
     */
    @Column(name = "CJDLB")
	private Integer cjdlb;

    /**
     * 录像存储位置 db_column: LXCCWZ 
     */
    @Column(name = "LXCCWZ")
	private String lxccwz;

    /**
     * 存储托管运营商编码 db_column: CCTGYYSBM 
     */
    @Column(name = "CCTGYYSBM")
	private Integer cctgyysbm;

    /**
     * 存储托管运营商名称 db_column: CCTGYYSMC 
     */
    @Column(name = "CCTGYYSMC")
	private String cctgyysmc;

    /**
     * 存储托管运营商机房地址 db_column: CCTGYYSJFDZ 
     */
    @Column(name = "CCTGYYSJFDZ")
	private String cctgyysjfdz;

    /**
     * 录像存储设备IP db_column: LXCCSBIP 
     */
    @Column(name = "LXCCSBIP")
	private String lxccsbip;

    /**
     * 录像存储设备国标ID号 db_column: LXCCSBGBID 
     */
    @Column(name = "LXCCSBGBID")
	private String lxccsbgbid;

    /**
     * 录像存储设备名称 db_column: LXCCSBMC 
     */
    @Column(name = "LXCCSBMC")
	private String lxccsbmc;

    /**
     * 录像存储设备登录用户名 db_column: LXCCSDLYHM 
     */
    @Column(name = "LXCCSDLYHM")
	private String lxccsdlyhm;

    /**
     * 录像存储设备登录密码 db_column: LXCCSBDLMM 
     */
    @Column(name = "LXCCSBDLMM")
	private String lxccsbdlmm;

    /**
     * 录像存储设备端口号 db_column: LXCCSBDKH 
     */
    @Column(name = "LXCCSBDKH")
	private String lxccsbdkh;

    /**
     * 录像存储设备总通道数 db_column: LXCCSBZTDS 
     */
    @Column(name = "LXCCSBZTDS")
	private Integer lxccsbztds;

    /**
     * 存储系统通道号 db_column: CCXTTDH 
     */
    @Column(name = "CCXTTDH")
	private Integer ccxttdh;

    /**
     * 录像存储设备厂家 db_column: LXCCSBCJ 
     */
    @Column(name = "LXCCSBCJ")
	private Integer lxccsbcj;

    /**
     * 录像存储设备类型 db_column: LXCCSBLX 
     */
    @Column(name = "LXCCSBLX")
	private Integer lxccsblx;

    /**
     * 警区 db_column: Block 
     */
    @Column(name = "Block")
	private String block;

    /**
     * 安装地址 db_column: AZDZ 
     */
    @Column(name = "AZDZ")
	private String azdz;

    /**
     * 门牌地址 db_column: MPDZ 
     */
    @Column(name = "MPDZ")
	private String mpdz;

    /**
     * 立杆编号 db_column: LGBH 
     */
    @Column(name = "LGBH")
	private String lgbh;

    /**
     * 周边标志 db_column: ZBBZ 
     */
    @Column(name = "ZBBZ")
	private String zbbz;

    /**
     * 经度 db_column: JD 
     */
    @Column(name = "JD")
	private Double jd;

    /**
     * 纬度 db_column: WD 
     */
    @Column(name = "WD")
	private Double wd;

    /**
     * 摄像机位置类型 db_column: WZLX 
     */
    @Column(name = "WZLX")
	private Integer wzlx;

    /**
     * 安装方式 db_column: AZFS 
     */
    @Column(name = "AZFS")
	private Integer azfs;

    /**
     * 杆高 db_column: GG 
     */
    @Column(name = "GG")
	private String gg;

    /**
     * 臂长 db_column: BC 
     */
    @Column(name = "BC")
	private String bc;

    /**
     * 横臂方向 db_column: HBFX 
     */
    @Column(name = "HBFX")
	private String hbfx;

    /**
     * 取电方式 db_column: QDFS 
     */
    @Column(name = "QDFS")
	private String qdfs;

    /**
     * 射域描述 db_column: SYMS 
     */
    @Column(name = "SYMS")
	private String syms;

    /**
     * 监控范围截图 db_column: JKFWJT 
     */
    @Column(name = "JKFWJT")
	private String jkfwjt;

    /**
     * 监控实景 db_column: JKSJ 
     */
    @Column(name = "JKSJ")
	private String jksj;

    /**
     * 摄像机安装位置室内外 db_column: SNWSX 
     */
    @Column(name = "SNWSX")
	private Integer snwsx;

    /**
     * 摄像机监控方位 db_column: SXJJKFW 
     */
    @Column(name = "SXJJKFW")
	private Integer sxjjkfw;

    /**
     * 关联摄像机 db_column: GLSXJ 
     */
    @Column(name = "GLSXJ")
	private String glsxj;

    /**
     * 是否共杆 db_column: SFGG 
     */
    @Column(name = "SFGG")
	private Integer sfgg;

    /**
     * 信令安全模式 db_column: SafetyWay 
     */
    @Column(name = "SafetyWay")
	private Integer safetyWay;

    /**
     * 注册方式 db_column: RegisterWay 
     */
    @Column(name = "RegisterWay")
	private Integer registerWay;

    /**
     * 保密属性 db_column: Secrecy 
     */
    @Column(name = "Secrecy")
	private Integer secrecy;

    /**
     * 证书序列号 db_column: CertNum 
     */
    @Column(name = "CertNum")
	private String certNum;

    /**
     * 证书有效标识 db_column: Certifiable 
     */
    @Column(name = "Certifiable")
	private Integer certifiable;

    /**
     * 无效原因码 db_column: ErrCode 
     */
    @Column(name = "ErrCode")
	private Integer errCode;

    /**
     * 证书终止有效期 db_column: EndTime 
     */
    @Column(name = "EndTime")
	private java.util.Date endTime;

    /**
     * 联网属性 db_column: LWSX 
     */
    @Column(name = "LWSX")
	private Integer lwsx;

    /**
     * 接入网络 db_column: JRWL 
     */
    @Column(name = "JRWL")
	private Integer jrwl;

    /**
     * 设备/区域/系统IPv4地址 db_column: IPv4 
     */
    @Column(name = "IPv4")
	private String ipv4;

    /**
     * 设备/区域/系统IPv6地址 db_column: IPv6 
     */
    @Column(name = "IPv6")
	private String ipv6;

    /**
     * 设备MAC地址 db_column: MacAddress 
     */
    @Column(name = "MacAddress")
	private String macAddress;

    /**
     * 设备/区域/系统端口 db_column: Port 
     */
    @Column(name = "Port")
	private Integer port;

    /**
     * 设备用户名 db_column: SBYHM 
     */
    @Column(name = "SBYHM")
	private String sbyhm;

    /**
     * 设备口令 db_column: Password 
     */
    @Column(name = "Password")
	private String password;

    /**
     * 中心信令服务器IP/网关服务器IP db_column: GATEWAYIP 
     */
    @Column(name = "GATEWAYIP")
	private String gatewayip;

    /**
     * 中心信令域/网关域 db_column: GATEWAYDOMAIN 
     */
    @Column(name = "GATEWAYDOMAIN")
	private String gatewaydomain;

    /**
     * 中心信令ID/网关ID db_column: GATEWAYID 
     */
    @Column(name = "GATEWAYID")
	private String gatewayid;

    /**
     * 中心信令服务器操作系统登录用户名/网关服务器操作系统登录用户名 db_column: OSLOGINNAME 
     */
    @Column(name = "OSLOGINNAME")
	private String osloginname;

    /**
     * 中心信令服务器操作系统登录密码/网关服务器操作系统登录密码 db_column: OSLOGINKEY 
     */
    @Column(name = "OSLOGINKEY")
	private String osloginkey;

    /**
     * 启用时间 db_column: QYSJ 
     */
    @Column(name = "QYSJ")
	private java.util.Date qysj;

    /**
     * 报废时间 db_column: BFSJ 
     */
    @Column(name = "BFSJ")
	private java.util.Date bfsj;

    /**
     * 安装时间 db_column: AZSJ 
     */
    @Column(name = "AZSJ")
	private java.util.Date azsj;

    /**
     * 设备状态 db_column: SBZT 
     */
    @Column(name = "SBZT")
	private Integer sbzt;

    /**
     * 视频丢失 db_column: SPDS 
     */
    @Column(name = "SPDS")
	private Integer spds;

    /**
     * 色彩失真 db_column: SCSZ 
     */
    @Column(name = "SCSZ")
	private Integer scsz;

    /**
     * 视频模糊 db_column: SPMH 
     */
    @Column(name = "SPMH")
	private Integer spmh;

    /**
     * 亮度异常 db_column: LDYC 
     */
    @Column(name = "LDYC")
	private Integer ldyc;

    /**
     * 视频干扰 db_column: SPGR 
     */
    @Column(name = "SPGR")
	private Integer spgr;

    /**
     * 视频卡顿 db_column: SPKD 
     */
    @Column(name = "SPKD")
	private Integer spkd;

    /**
     * 视频遮挡 db_column: SPZD 
     */
    @Column(name = "SPZD")
	private Integer spzd;

    /**
     * 场景变更 db_column: CJBG 
     */
    @Column(name = "CJBG")
	private Integer cjbg;

    /**
     * 在线时长 db_column: ZXSC 
     */
    @Column(name = "ZXSC")
	private Integer zxsc;

    /**
     * 离线时长 db_column: LXSC 
     */
    @Column(name = "LXSC")
	private Integer lxsc;

    /**
     * 信令时延 db_column: XLSY 
     */
    @Column(name = "XLSY")
	private Integer xlsy;

    /**
     * 视频流时延 db_column: SPLSY 
     */
    @Column(name = "SPLSY")
	private Integer splsy;

    /**
     * 关键帧时延 db_column: GJZSY 
     */
    @Column(name = "GJZSY")
	private Integer gjzsy;

    /**
     * 录像保存天数 db_column: LXBCTS 
     */
    @Column(name = "LXBCTS")
	private Integer lxbcts;

    /**
     * 下载倍速范围 db_column: DownloadSpeed 
     */
    @Column(name = "DownloadSpeed")
	private String downloadSpeed;

    /**
     * 文件路径名 db_column: Filepath 
     */
    @Column(name = "Filepath")
	private String filepath;

    /**
     * 录像地址 db_column: RecordAddress 
     */
    @Column(name = "RecordAddress")
	private String recordAddress;

    /**
     * 空域编码能力 db_column: SVCSpaceSupportMode 
     */
    @Column(name = "SVCSpaceSupportMode")
	private Integer svcspaceSupportMode;

    /**
     * 时域编码能力 db_column: SVCTimeSupportMode 
     */
    @Column(name = "SVCTimeSupportMode")
	private Integer svctimeSupportMode;

    /**
     * 视频编码格式 db_column: VideoEncodeType 
     */
    @Column(name = "VideoEncodeType")
	private Integer videoEncodeType;

    /**
     * 图像分辨率 db_column: Resolution 
     */
    @Column(name = "Resolution")
	private Integer resolution;

    /**
     * 图像帧率 db_column: FrameRate 
     */
    @Column(name = "FrameRate")
	private Integer frameRate;

    /**
     * 视频码率类型 db_column: VideoCodeType 
     */
    @Column(name = "VideoCodeType")
	private Integer videoCodeType;

    /**
     * 视频码率大小 db_column: VideoCodeRate 
     */
    @Column(name = "VideoCodeRate")
	private Integer videoCodeRate;

    /**
     * 码流类型 db_column: StreamType 
     */
    @Column(name = "StreamType")
	private Integer streamType;

    /**
     * 拾音器 db_column: Pickup 
     */
    @Column(name = "Pickup")
	private Integer pickup;

    /**
     * 音频编码格式 db_column: AudioEncodeType 
     */
    @Column(name = "AudioEncodeType")
	private Integer audioEncodeType;

    /**
     * 音频编码码率 db_column: AudioCodeRate 
     */
    @Column(name = "AudioCodeRate")
	private Integer audioCodeRate;

    /**
     * 音频采样率 db_column: AudioSampleRate 
     */
    @Column(name = "AudioSampleRate")
	private Integer audioSampleRate;

    /**
     * 监控点位类型 db_column: JKDWLX 
     */
    @Column(name = "JKDWLX")
	private Integer jkdwlx;

    /**
     * 建设类型 db_column: JSLX 
     */
    @Column(name = "JSLX")
	private Integer jslx;

    /**
     * 投资单位 db_column: TZDW 
     */
    @Column(name = "TZDW")
	private String tzdw;

    /**
     * 建设单位/设备归属 db_column: JSDW 
     */
    @Column(name = "JSDW")
	private String jsdw;

    /**
     * 建设单位/平台归属代码 db_column: JSDWDM 
     */
    @Column(name = "JSDWDM")
	private Integer jsdwdm;

    /**
     * 所属项目编号 db_column: SSXMBH 
     */
    @Column(name = "SSXMBH")
	private String ssxmbh;

    /**
     * 所属项目名称 db_column: SSXMMC 
     */
    @Column(name = "SSXMMC")
	private String ssxmmc;

    /**
     * 摄像机所属部门 db_column: SXJSSBM 
     */
    @Column(name = "SXJSSBM")
	private Integer sxjssbm;

    /**
     * 所属派出所 db_column: SSPCS 
     */
    @Column(name = "SSPCS")
	private String sspcs;

    /**
     * 所属街道 db_column: SSJD 
     */
    @Column(name = "SSJD")
	private String ssjd;

    /**
     * 承建单位 db_column: CJDW 
     */
    @Column(name = "CJDW")
	private String cjdw;

    /**
     * 维护单位 db_column: WHDW 
     */
    @Column(name = "WHDW")
	private String whdw;

    /**
     * 维护人 db_column: WHR 
     */
    @Column(name = "WHR")
	private String whr;

    /**
     * 维护人手机 db_column: WHRSJ 
     */
    @Column(name = "WHRSJ")
	private String whrsj;

    /**
     * 修改人 db_column: XGR 
     */
    @Column(name = "XGR")
	private String xgr;

    /**
     * 修改时间 db_column: XGSJ 
     */
    @Column(name = "XGSJ")
	private java.util.Date xgsj;


}

