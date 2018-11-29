package com.hdvon.face.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class HDFaceInfoVo implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public int left_x,left_y,width,height;       //人脸位置
    public float feature[] = new float[512];    //人脸特征
}
