package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>查询摄像机条件 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Camera")
public class CameraParamVo  implements Serializable{
	private static final long serialVersionUID = 1L;
        
	    @ApiModelProperty(value = "查询条件地址树id")
	    private String addressId;
	    
	    @ApiModelProperty(value = "查询条件地址编号")
	    private String addressCode;

	    @ApiModelProperty(value = "所属编码器id")
	    private String encoderServerId;

	    @ApiModelProperty(value = "查询条件摄像机工程id")
	    private String projectId;
	    
	    @ApiModelProperty(value = "查询条件项目编号")
	    private String projectCode;

	    @ApiModelProperty(value = "查询条件摄像机组织机构id")
	    private String orgId;
	    
	    @ApiModelProperty(value = "查询条件组织结构编号")
	    private String orgCode;
	    
	    @ApiModelProperty(value = "查询条件摄像机分组id")
	    private String cameraGroupId; 
	    
	    @ApiModelProperty(value = "查询条件自定义分组编号")
	    private String cameraGroupCode;
	
		@ApiModelProperty(value = "设备编码支持模糊")
	    private String sbbm;
		
		@ApiModelProperty(value = "设备/区域/系统名称支持模糊")
	    private String sbmc;
		
		@ApiModelProperty(value = "设备厂商")
	    private Integer sbcs;
		
	    @ApiModelProperty(value = "摄像机类型")
		private Integer sxjlx;
	    
	    @ApiModelProperty(value = "摄像机补光属性")
	    private Integer bgsx;
	    
	    @ApiModelProperty(value = "摄像机位置类型")
	    private Integer wzlx;
	    
	    @ApiModelProperty(value = "摄像机所属部门")
	    private Integer sxjssbm;
	    
	    @ApiModelProperty(value = "经度(纬度)")
	    private Double jd;
	    
	    @ApiModelProperty(value = "设备状态")
	    private Integer sbzt;
	    
	    @ApiModelProperty(value = "建设单位")
	    private String jsdw;

	    @ApiModelProperty(value = "承建单位")
	    private String cjdw;
	    
	    @ApiModelProperty(value = "采集点类别")
	    private Integer cjdlb;
	    
	    @ApiModelProperty(value = "摄像机功能类型")
	    private Integer sxjgnlx;
	    
	    @ApiModelProperty(value = "摄像机状态")
	    private Integer sxjzt;
	    
	    @ApiModelProperty(value = "ip")
	    private String cameraIp;
	    
	    @ApiModelProperty(value = "摄像机编码列表")
        private List<String> sbbmList;
	    
		@ApiModelProperty(value = "地址树节点id集合")
        private List<String> addressNodeIds = new ArrayList<String>();
	    
		@ApiModelProperty(value = "树节点id集合")
        private List<String> orgNodeIds = new ArrayList<String>();
	    
		@ApiModelProperty(value = "项目树节点id集合")
        private List<String> projectNodeIds = new ArrayList<String>();
	    
		@ApiModelProperty(value = "自定义分组树节点id集合")
        private List<String> groupNodeIds = new ArrayList<String>();
		
		@ApiModelProperty(value = "部门id")
		private String deptId;
		
		@ApiModelProperty(value = "部门split编号")
		private String deptCode;

	    /*@ApiModelProperty(value = "查询条件摄像机类型")
	    private String cameraTypeId;*/
	    
	  /*  @ApiModelProperty(value = "建设单位支持模糊")
	    private String jsdw;
	    
	    @ApiModelProperty(value = "承建单位支持模糊")
	    private String cjdw;*/
	    /*  
	    @ApiModelProperty(value = "存储托管运营商名称")
	    private String cctgyysmc;
	    
	    @ApiModelProperty(value = "联网属性")
	    private Integer lwsx;
	    
	    @ApiModelProperty(value = "接入网络")
	    private Integer jrwl;
	    
	    @ApiModelProperty(value = "建设类型")
	    private Integer jslx;*/
	    
	    /*@ApiModelProperty(value = "监控点位类型")
	    private Integer jkdwlx;
	    
	    @ApiModelProperty(value = "投资单位")
	    private String tzdw;
	
	    @ApiModelProperty(value = "录像存储设备厂家")
	    private Integer lxccsbcj;
	    
	    @ApiModelProperty(value = "录像存储设备类型")
	    private Integer lxccsblx;*/

}
