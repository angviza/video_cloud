package com.hdvon.client.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 经纬度对象
 * @author wnsojn 2018-6-11 11:00
 *
 */
@Data
public class PointVo implements Serializable{
    private double latitude;//纬度
    
    private double longitude;//经度
    
    public PointVo() {}
    
    public PointVo(double latitude,double longitude) {
    	this.latitude = latitude;
    	this.longitude = longitude;
    }
}
