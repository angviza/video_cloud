package com.hdvon.client.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import com.hdvon.client.es.IndexField;
import com.hdvon.client.mapper.PermissionPlanMapper;
import com.hdvon.client.vo.EsCameraPermissionVo;
import com.hdvon.nmp.common.SystemConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>摄像机预授权服务接口实现<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-5-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Component
@Slf4j
public class PermissionPlanService {

	@Resource
	PermissionPlanMapper permissionPlanMapper;
	

	

	/**
	 * 预授权处理:
	 * 		黑名单摄像机资源过滤
	 * 		白名单摄像机资源过滤
	 * @param userId
	 * @param boolQuery
	 * @return
	 */
	public void filterCamera(String userId,BoolQueryBuilder boolQuery){
		Long uid = Long.parseLong(userId);
		boolQuery.must(QueryBuilders.matchQuery(IndexField.USER_ID, userId).operator(Operator.AND));
		//存储需要过滤的摄像机集合
		List<Long> cameraList = new ArrayList<>();
		//获取当前黑名单预授权的摄像机集合
		List<Long> blackCameraList = permissionPlanMapper.getPermissionPlanBlackCameraList(uid);
		if(!blackCameraList.isEmpty()) {
			cameraList.addAll(blackCameraList);
			
		}

		//获取当前白名单预授权的摄像机集合
		List<EsCameraPermissionVo> whiteCameraList = permissionPlanMapper.getPermissionPlanWhiteCameraList();
		if(!whiteCameraList.isEmpty()) {
			if(!blackCameraList.isEmpty()) {
				whiteCameraList = whiteCameraList.stream().filter(vo->blackCameraList.contains(vo.getDeviceId())).collect(Collectors.toList());
			}
			Map<Long, List<Long>> deviceMap = whiteCameraList.stream().collect(Collectors.groupingBy(EsCameraPermissionVo::getDeviceId,
	                        Collectors.mapping(EsCameraPermissionVo::getUserId,Collectors.toList())));
			//过滤在该摄像机用户白名单不存在			
			for(Iterator<Long> it = deviceMap.keySet().iterator();it.hasNext();) {
				Long key = it.next();
				List<Long> userList = deviceMap.get(key);
				
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>key:{},userList:{}",key,userList);
				/**
				 * 判断单个摄像机的白名单列表是否包含该用户，
				 * 		不包含则剔除该摄像机,将其加入过滤列表中
				 */
				if(!userList.contains(uid)) {
					cameraList.add(key);
					log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>需要过滤的deviceId:{}",key);
				}
			}
		}		
		if(!cameraList.isEmpty()) {
			//剔除黑名单
			boolQuery.mustNot((QueryBuilders.termsQuery(IndexField.DEVICE_ID,cameraList)));
		}
		BoolQueryBuilder query1 = new BoolQueryBuilder();
		
		BoolQueryBuilder query2 = new BoolQueryBuilder();
		Date currentDate = getCurrentDate();
		Date startDate = getAddDate(currentDate, -3);
		Date endDate = getAddDate(currentDate, 3);
		//大于当前日期
		query2.must(QueryBuilders.rangeQuery(IndexField.START_DATE).gte(startDate.getTime()).lte(currentDate.getTime()));
		//小于当前日期
		query2.must(QueryBuilders.rangeQuery(IndexField.END_DATE).gte(currentDate.getTime()).lte(endDate.getTime()));
		query2.must(QueryBuilders.termQuery(IndexField.PERMANENT, "2"));
		
		query1.should(query2);
		query1.should(QueryBuilders.termQuery(IndexField.PERMANENT, "1"));
		
		boolQuery.must(query1);
	} 

	public Date getCurrentDate() {
		Date date = new Date();
		return date;
	}
	
	public Date getAddDate(Date date,int month) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.MONTH, month);
		
		return cl.getTime();
	}
}
