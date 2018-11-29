package com.hdvon.client.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>部门表 VO类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-5-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class DepartmentVo implements Serializable{


    private String id;//部门id

    private String name;//部门名称

    private String pid;//上级部门id

    private String depCode;//部门编码

    private String parentDepCode;//上级部门编码
    

}

