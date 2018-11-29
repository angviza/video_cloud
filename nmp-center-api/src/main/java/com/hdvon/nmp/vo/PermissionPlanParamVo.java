package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value ="PermissionPlan")
public class PermissionPlanParamVo implements Serializable{


	private static final long serialVersionUID = 1L;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "权限预案名称")
    private java.lang.String name;

    @ApiModelProperty(value = "权限预案状态（0禁用，1启用）")
    private java.lang.Integer status;

    @ApiModelProperty(value = "用户状态（1：白名单；2：黑名单；9：普通用户）")
    private java.lang.Integer userStatus;

    @ApiModelProperty(value = "预案开始时间")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "预案截止时间")
    private java.util.Date endTime;
    
    @ApiModelProperty(value = "开始时间(yyyy-MM-dd HH:mm:ss)")
    private java.lang.String bgnTimeStr;

    @ApiModelProperty(value = "结束时间(yyyy-MM-dd HH:mm:ss)")
    private java.lang.String endTimeStr;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;
    
    @ApiModelProperty(value = "类型(1:新增授权；2：摄像机管控)")
    private java.lang.String type;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

    public void convertTime() {
    	try {
    		if(StrUtil.isNotBlank(this.bgnTimeStr)) {
    			this.bgnTime = sdf.parse(this.bgnTimeStr);
    		}
    		if(StrUtil.isNotBlank(this.endTimeStr)) {
    			this.endTime = sdf.parse(this.endTimeStr);
    		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
