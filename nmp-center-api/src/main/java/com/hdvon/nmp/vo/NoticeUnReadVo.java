package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value ="Notice")
public class NoticeUnReadVo implements Serializable {

    @ApiModelProperty(value = "未读数")
    private java.lang.Integer count;

    @ApiModelProperty(value = "未读通知公告集合")
    private List<NoticeVo> noticeVos;
}
