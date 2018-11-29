package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <b>功能：</b>上墙轮巡 VO类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="WallRotateReq")
public class WallRotateReqVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间间隔。单位：秒。operate = START || PLAY 时必填。")
    private java.lang.Integer period;
    
    @ApiModelProperty(value = "矩阵通道id列表。operate = START 时必填。")
    private List<String> channel;
    
    @ApiModelProperty(value = "摄像机id列表。operate = START 时必填。")
    private List<String> camera;
    
    /**
     * @see EWallOperation
     */
    @ApiModelProperty(value = "操作。START：新增上墙。PAUSE：暂停。PLAY：暂停后重新播放。CANCEL_ONE：取消轮巡。")
    private String operate;

    /**
     * t_wall_rotate表id
     */
    @ApiModelProperty(value = "轮巡id。operate = PAUSE || PLAY || CANCEL_ONE 时必填。")
    private java.lang.String rotate;
    
}

