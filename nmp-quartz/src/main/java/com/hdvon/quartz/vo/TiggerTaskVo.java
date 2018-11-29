package com.hdvon.quartz.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>定时任务配置表 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class TiggerTaskVo implements Serializable{

	private static final long serialVersionUID = 1L;

    private java.lang.String id;

    //定时任务名称
    private java.lang.String name;

    //定时任务执行规则
    private java.lang.String rules;

    //定时任务执行类型 1 统计用户报表 2 统计摄像机报表....
    private java.lang.String type;

    //"描述信息
    private java.lang.String description;

    //creatTime
    private java.util.Date creatTime;

    //上一次定时器执行时间
    private java.util.Date updateTime;


}

