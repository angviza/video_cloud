package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class TimingVedioResultVo  implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 561741132880160692L;
	
	@ApiModelProperty(value = "星期几（星期一：1；星期二：2；星期三：3；星期四：4；星期五：5；星期六：6；星期日：7）")
	private java.lang.Integer dayOfWeek;
	
	@ApiModelProperty(value = "星期几对应的定时录像设置时间段集合")
	private List<TimeingVedioLegVo> vedioLegVos;

}
