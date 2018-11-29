package com.hdvon.client.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Arcgis地图查询条件
 */
@Data
public class MapQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 页数
     */
    private Integer pageSize = 50;

    /**
     * 当前页
     */
    private Integer currentPage = 1;

    /**
     * 关键字搜索
     */
    private String keyword;

    /**
     * 操作类型，0.普通搜索、1.圈选、2.框选、3.多边形、4.线选
     */
    private Integer choseType = 0;

    /**
     * 设备id，单选
     */
    private String deviceId;

    /**
     * 圈选-圆心坐标
     */
    private PointVo cycleCenter;

    /**
     * 圈选-圆半径，单位(米)
     */
    private int cycleRadius;

    /**
     * 框选-左上角坐标
     */
    private PointVo rectangleLeft;

    /**
     * 框选-右下角坐标
     */
    private PointVo rectangleRight;

    /**
     * 多边形
     */
    private List<PointVo> polygon;

    /**
     * 线选
     */
    private List<PointVo> line;


    /**
     * 行政编码
     */
    private String orgCode;

    /**
     * 项目编号集合
     */
    private String projectId;

    /**
     * 地址编码
     */
    private String addressCode;

    /**
     * 自定义分组编码
     */
    private String groupCode;
    /**
     * 是否高级搜索(1:是；0:否)
     */
    private Integer highFilter;
    /**
     * 摄像机名称
     */
    private String deviceName;
    /**
     * 摄像机编码
     */
    private String deviceCode;
    /**
     * 摄像机类型
     */
    private Integer deviceType;
    /**
     * 所属编码器id
     */
    private String encoderServerId;
    /**
     * ip
     */
    private String ip;
    /**
     * 摄像机状态
     */
    private Integer status;
    /**
     * 建设单位
     */
    private String constructionUnit;
    /**
     * 承建单位
     */
    private String urbanConstructionUnit;
    /**
     * 设备厂商
     */
    private Integer sbcs;
    /**
     * 摄像机所属部门
     */
    private Integer sxjssbm;
    /**
     * 摄像机位置类型
     */
    private Integer sxjwzlx;
    /**
     * 采集点类别
     */
    private Integer cjdlb;
    /**
     * 摄像机功能类型
     */
    private Integer sxjgnlx;
    
    
}
