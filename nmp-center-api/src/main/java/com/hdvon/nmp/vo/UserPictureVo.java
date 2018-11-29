package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value ="UserPicture")
public class UserPictureVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
    
    @ApiModelProperty(value = "用户id")
	private String userId;
    
    
    @ApiModelProperty(value = "图片保存地址")
	private String imgPath;

    
    @ApiModelProperty(value = "图片名称")
	private String imgName;

    
    @ApiModelProperty(value = "图片特征值")
	private String features;

    


}
