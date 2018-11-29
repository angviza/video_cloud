package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value ="WallTaskVo")
public class WallTaskParamVo  implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;
    
    @ApiModelProperty(value = "任务名称")
    private java.lang.String name;
    
    @ApiModelProperty(value = "任务类型")
    private java.lang.Integer type;

    @ApiModelProperty(value = "用户表id")
    private java.lang.String userId;

    @ApiModelProperty(value = "矩阵id")
    private java.lang.String matrixId;
    
    @ApiModelProperty(value = "逗号隔开的多个任务id")
    private java.lang.String taskIds;
    
	@ApiModelProperty(value = "任务id列表")
	private List<String> taskIdList;
	
    public void convertToList() {
		
		if(StrUtil.isNotBlank(this.taskIds)) {
			String[] taskIdArr = this.taskIds.split(",");
			this.taskIdList = Arrays.asList(taskIdArr);
		}else {
			this.taskIdList = new ArrayList<String>();
		}

	}
}
