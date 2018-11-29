package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="PollingPlanParam")
public class PollingPlanParamVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "轮询预案名称")
    private java.lang.String name;

    @ApiModelProperty(value = "状态（是否启用：1启用，0停止）")
    private java.lang.Integer status;
    
    @ApiModelProperty(value = "共享状态（0:私有，仅自己可见；1全局共享，所有人可见；2部门共享，本部门用户可见；3指定部门共享，对指定的部门用户可见）")
    private java.lang.Integer shareStatus;

    @ApiModelProperty(value = "bgnTime")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "endTime")
    private java.util.Date endTime;
    
    @ApiModelProperty(value = "开始时间(yyyy-MM-dd HH:mm:ss)")
    private java.lang.String bgnTimeStr;

    @ApiModelProperty(value = "结束时间(yyyy-MM-dd HH:mm:ss)")
    private java.lang.String endTimeStr;

    @ApiModelProperty(value = "description")
    private java.lang.String description;
    
    @ApiModelProperty(value = "多个共享部门id（用逗号隔开，只有当共享状态为指定部门共享时才有值）")
    private java.lang.String shareDeptIds;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/
    
    @ApiModelProperty(value = "根据预案起止时间判断预案是否有效（1：有效；0：无效）")
    private java.lang.Integer isValid;
    
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
