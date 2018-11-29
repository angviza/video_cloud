package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="TimingVedio")
public class TimingVedioParamVo  implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联id")
    private java.lang.String storeCameraId;

    @ApiModelProperty(value = "星期几（星期一：1；星期二：2；星期三：3；星期四：4；星期五：5；星期六：6；星期日：7）")
    private java.lang.Integer dayOfWeek;

    @ApiModelProperty(value = "一天中某一段录像时间段的开始时间")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "一天中某一段录像时间段的结束时间")
    private java.util.Date endTime;
    
    @ApiModelProperty(value = "每周中每天定时录像时间段设置")
    private Map<String,List<String>> weekSet = new HashMap<String,List<String>>();
    
    @ApiModelProperty(value = "每周中每天定时录像时间段设置")
    private java.lang.String weekSetStr;
    
    //weekSetStr 格式：{"1":[{'id':'','value':[startSeconds,endSeconds]},{}],"2":[],"3":[],"4":[],"5":[],"6":[],"7":[]}
    //weekSetStr 格式：{"1":["HH:mm-HH:mm","HH:mm-HH:mm",""],"2":[],"3":[],"4":[],"5":[],"6":[],"7":[]}
    public void convertWeekSets() {
    	System.out.println(this.weekSetStr);
    	JSONObject jsonObj = JSONObject.parseObject(this.weekSetStr);
    	String[] weekValue = {"1","2","3","4","5","6","7"};
    	for(int i=0;i<weekValue.length;i++) {
    		if(jsonObj.containsKey(weekValue[i])) {
    			List<String> list = new ArrayList<String>();
    			JSONArray array = (JSONArray) jsonObj.get(weekValue[i]);//某一天设置的数组数据
    			Iterator<Object> it = array.iterator();
    			while(it.hasNext()) {
    				JSONObject dayPerTimeJson = (JSONObject) it.next();//某一天设置的单个时间段
    				JSONArray bgnEndTimeArr = (JSONArray) dayPerTimeJson.get("value");//单个时间段的起止时间数组
    				
    				String perTimeStr = transBgnEndTimeToStr(bgnEndTimeArr);
    				list.add(perTimeStr);
    			}
    			weekSet.put(weekValue[i], list);
    		}
    	}
    }
    
    /**
     * 整数秒的起止时间转成字符串
     * @param bgnEndTimeArr
     * @return
     */
    private String transBgnEndTimeToStr(JSONArray bgnEndTimeArr) {
    	Integer bgnTime = (Integer) bgnEndTimeArr.get(0);
    	Integer endTime = (Integer) bgnEndTimeArr.get(1);
    	
    	Integer bgnSenconds = bgnTime%60;
    	
    	Integer bgnHourMinutes = bgnTime/60;
    	
    	Integer bgnMinutes = bgnHourMinutes%60;
    	
    	Integer bgnHours = bgnHourMinutes/60;
    	
    	String bgnTimeStr = (bgnHours<10?("0"+bgnHours):(""+bgnHours)) + ":" + (bgnMinutes<10?("0"+bgnMinutes):(""+bgnMinutes)) + ":" + (bgnSenconds<10?("0"+bgnSenconds):(""+bgnSenconds));
    	
    	Integer endSenconds = endTime%60;
    	
    	Integer endHourMinutes = endTime/60;
    	
    	Integer endMinutes = endHourMinutes%60;
    	
    	Integer endHours = endHourMinutes/60;
    	
    	String endTimeStr = (endHours<10?("0"+endHours):(""+endHours)) + ":" + (endMinutes<10?("0"+endMinutes):(""+endMinutes)) + ":" + (endSenconds<10?("0"+endSenconds):(""+endSenconds));
    	
    	return bgnTimeStr + "-" + endTimeStr;
    }
    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

}
