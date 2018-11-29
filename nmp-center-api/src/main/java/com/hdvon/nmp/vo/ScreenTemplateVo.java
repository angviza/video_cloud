package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>分屏管理模板 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="ScreenTemplate")
public class ScreenTemplateVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "模板名称")
    private java.lang.String name;

    @ApiModelProperty(value = "行数")
    private java.lang.Integer rows;

    @ApiModelProperty(value = "列数")
    private java.lang.Integer cols;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;
    
    @ApiModelProperty(value = "单元格信息集合")
    private List<ScreenTemplateCellinfoVo> cellinfos = new ArrayList<ScreenTemplateCellinfoVo>();
    
    @ApiModelProperty(value = "单元格信息集合")
    private String cellinfoStr;
    
    @ApiModelProperty(value = "轮询摄像机集合")
    private List<CameraVo> cameras;
    
	public void convertCellinfos() {
    	JSONArray array = JSONArray.parseArray(this.cellinfoStr);
    	for(int i=0;i<array.size();i++) {
    		JSONObject json = array.getJSONObject(i);
    		ScreenTemplateCellinfoVo cellinfoVo = new ScreenTemplateCellinfoVo();
    		cellinfoVo.setColNo(json.getInteger("colNo"));
    		cellinfoVo.setRowNo(json.getInteger("rowNo"));
    		cellinfoVo.setColSpan(json.getInteger("colSpan"));
    		cellinfoVo.setRowSpan(json.getInteger("rowSpan"));
    		this.cellinfos.add(cellinfoVo);
    	}
    }

    /*@ApiModelProperty(value = "createTime")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/
    public static void main(String[] args) {
    	
    }

}

