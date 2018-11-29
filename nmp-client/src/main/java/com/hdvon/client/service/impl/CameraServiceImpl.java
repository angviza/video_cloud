package com.hdvon.client.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.client.config.code.CodeConfig;
import com.hdvon.client.config.redis.BaseRedisDao;
import com.hdvon.client.entity.User;
import com.hdvon.client.enums.AggFieldEnum;
import com.hdvon.client.es.AbstractEsFilter;
import com.hdvon.client.es.ElasticsearchUtils;
import com.hdvon.client.es.IndexField;
import com.hdvon.client.exception.CameraException;
import com.hdvon.client.exception.UserException;
import com.hdvon.client.form.CameraForm;
import com.hdvon.client.mapper.CameraMapper;
import com.hdvon.client.mapper.CameraPermissionMapper;
import com.hdvon.client.mapper.TreeMapper;
import com.hdvon.client.service.CameraHelper;
import com.hdvon.client.service.CommonService;
import com.hdvon.client.service.ICameraService;
import com.hdvon.client.service.PermissionPlanService;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.client.vo.CameraVo;
import com.hdvon.client.vo.CheckCameraVo;
import com.hdvon.client.vo.EsCameraVo;
import com.hdvon.client.vo.EsPage;
import com.hdvon.client.vo.PointVo;
import com.hdvon.nmp.common.SystemConstant;


/**
 * <br>
 * <b>功能：</b>摄像机服务接口实现<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-5-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraServiceImpl implements ICameraService {
	
	private Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);

	@Resource
	BaseRedisDao<String, Object> redisDao;

	@Resource
	CameraPermissionMapper cameraPermissionMapper;

	@Resource
	CommonService commonService;

	@Resource
	TreeMapper treeMapper;
	

	@Autowired
	CameraMapper cameraMapper;
	
	@Autowired
	PermissionPlanService permissionPlanService;
	
	@Autowired
    CodeConfig codeConfig;
	
	private static final String DEFAULT_PID = "0";
	
	
	/**
	 * 获取当前地址节点的拥有的摄像机资源
	 * 
	 * @param userId
	 *            用户id
	 * @param pid
	 *            父节点id
	 * @param isAdmin
	 *            是否管理员
	 * @return
	 * @throws CameraException
	 */
	@Override
	public List<CameraPermissionVo> getAddressCameraPermissionTree(String userId, String pid, Integer isAdmin)
			throws CameraException {
		// TODO Auto-generated method stub
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(userId);
		isAdmin = flag?1:0;
		
		Map<String, Object> modelMap = new HashMap<>(16);
		modelMap.put("userId", userId);
		modelMap.put("pid", pid);
		modelMap.put("isAdmin", isAdmin);
		
		//获取当前层级地址列表
		List<CameraPermissionVo> list =  treeMapper.findChildAddress(modelMap);
		
		//获取摄像机资源
		List<CameraPermissionVo> list1 = searchESCameraPermission(TreeGroupEnum.ADDRESS, userId, pid, isAdmin);
		
		List<CameraPermissionVo> dataList = new ArrayList<>();
		dataList.addAll(list);
		dataList.addAll(list1);
		return dataList;
	}
	


	/**
	 * 获取当前行政区域节点的拥有的摄像机资源
	 * 
	 * @param userId
	 *            用户id
	 * @param pid
	 *            父节点id
	 * @param isAdmin
	 *            是否管理员
	 * @return
	 * @throws CameraException
	 */
	@Override
	public List<CameraPermissionVo> getOrganizationCameraPermission(String userId, String pid, Integer isAdmin)
			throws CameraException {
		// TODO Auto-generated method stub
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(userId);
		isAdmin = flag?1:0;
		
		Map<String, Object> modelMap = new HashMap<>(16);
		modelMap.put("userId", userId);
		modelMap.put("pid", pid);
		modelMap.put("isAdmin", isAdmin);
		
		//获取当前层级地址列表
		List<CameraPermissionVo> list =  treeMapper.findChildOrganization(modelMap);
		
		//获取摄像机资源
		List<CameraPermissionVo> list1 = searchESCameraPermission(TreeGroupEnum.ORG, userId, pid, isAdmin);
		
		List<CameraPermissionVo> dataList = new ArrayList<>();
		dataList.addAll(list);
		dataList.addAll(list1);
		//过滤摄像机信息
		return dataList;
	}

	/**
	 * 获取当前项目分组节点的拥有的摄像机资源
	 * 
	 * @param userId
	 *            用户id
	 * @param pid
	 *            父节点id
	 * @param isAdmin
	 *            是否管理员
	 * @param isProject
	 *            是否项目 1:是
	 * @return
	 * @throws CameraException
	 */
	@Override
	public List<CameraPermissionVo> getProjectCameraPermission(String userId, String pid, Integer isAdmin,
			Integer isProject) throws CameraException {
		// TODO Auto-generated method stub
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(userId);
		isAdmin = flag?1:0;
		
		List<CameraPermissionVo> list = new ArrayList<>(16);
		// 获取项目的顶级部门
		if (DEFAULT_PID.equals(pid)) {
			Map<String,String> param = new HashMap<String,String>();
			param.put("splitCodeSuffix",codeConfig.getSplitCodeSuffix()+"-");
			list = treeMapper.findTopDept(param);
		} else {
			Map<String, Object> modelMap = new HashMap<>(16);
			modelMap.put("pid", pid);
			modelMap.put("userId", userId);
			modelMap.put("isAdmin", isAdmin);
			
			list = treeMapper.findChildProjectOrDept(modelMap);
		}
		
		//获取摄像机资源
		List<CameraPermissionVo> list1 = searchESCameraPermission(TreeGroupEnum.PROJECT, userId, pid, isAdmin);
		
		List<CameraPermissionVo> dataList = new ArrayList<>();
		dataList.addAll(list);
		dataList.addAll(list1);
		return dataList;
	}

	/**
	 * 获取当前自定义分组节点的拥有的摄像机资源
	 * 
	 * @param userId
	 *            用户id
	 * @param account
	 *            用户名称
	 * @param pid
	 *            父节点id
	 * @param isAdmin
	 *            是否管理员
	 * @return
	 * @throws UserException
	 */
	@Override
	public List<CameraPermissionVo> getCameraGroupPermission(String userId, String account, String pid, Integer isAdmin)
			throws CameraException {
		// TODO Auto-generated method stub
		//判断用户是否超级管理员
		User user = commonService.findRecord(userId);
		isAdmin = SystemConstant.super_Account.equals(user.getAccount()) ? 1 : 0 ;
		
		Map<String, Object> modelMap = new HashMap<>(16);
		modelMap.put("userName", user.getAccount());
		modelMap.put("pid", pid);
		modelMap.put("isAdmin", isAdmin);
		
		//获取当前层级地址列表
		List<CameraPermissionVo> list =  treeMapper.findChildCameraGroup(modelMap);
		
		//获取摄像机资源
		List<CameraPermissionVo> list1 = searchESCameraPermission(TreeGroupEnum.GROUP, userId, pid, isAdmin);
		
		List<CameraPermissionVo> dataList = new ArrayList<>();
		dataList.addAll(list);
		dataList.addAll(list1);
		
		return dataList;
	}
	/**
	 * 根据用户获取分配的摄像机资源列表【查询elasticsearch索引数据】
	 * @param treeEm 
	 * 			     树形枚举类型
	 * @param userId
	 * 			      用户id
	 * @param pid
	 *            父节点id
	 * @param isAdmin
	 * 			    是否管理员
	 * @return
	 */
	public List<CameraPermissionVo> searchESCameraPermission(TreeGroupEnum treeEm,String userId,String pid,Integer isAdmin){
		//根据用户获取分配的摄像机资源列表【查询elasticsearch索引数据】
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		//是否摄像机树形分组标志  1:是 0:否
		int treeGroupFlag = SystemConstant.TREE_NOTGROUP_FLAG;
		if(TreeGroupEnum.ADDRESS.equals(treeEm)) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.ADDRESS_ID, pid));
		}else if(TreeGroupEnum.ORG.equals(treeEm)) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.ORG_ID, pid));
		}else if(TreeGroupEnum.PROJECT.equals(treeEm)) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.PROJECT_ID, pid));
		}else {
			treeGroupFlag = SystemConstant.TREE_GROUP_FLAG;
			boolQuery.must(QueryBuilders.termQuery(IndexField.GROUP_ID, pid));
		}
		CameraHelper cameraHelper = CameraHelper.getInstance();
		//获取摄像机资源权限值
		String permissionValue = commonService.getPermissionValue();
		//系统管理员
		if(isAdmin ==1 ) {
			return getAllCameraList(boolQuery, permissionValue, treeGroupFlag);
		}
		/**
		 * 预授权处理	
		 * 		1：黑名单摄像机资源过滤
		 *      2： 白名单摄像机资源过滤
		 */
		permissionPlanService.filterCamera(userId, boolQuery);
	

		if(SystemConstant.TREE_GROUP_FLAG == treeGroupFlag) {
			return cameraHelper.convertUserCameraGroupIndex(boolQuery);
		}
		return cameraHelper.convertCameraPermissionIndex(treeEm, boolQuery);
		
		
		
		
	}
	/**
	 * 超管账号获取摄像机信息
	 * @param boolQuery
	 * 			             查询条件
	 * @param permissionValue
	 * 				获取摄像机资源权限值
	 * @param treeGroupFlag
	 * 			             是否摄像机树形分组标志  1:是 0:否 
	 * @return
	 */
	private List<CameraPermissionVo> getAllCameraList(BoolQueryBuilder boolQuery,String permissionValue,int treeGroupFlag){
		if(SystemConstant.TREE_GROUP_FLAG == treeGroupFlag) {
			return CameraHelper.getInstance().convertCameraGroupIndex(boolQuery,permissionValue);
		}
		return CameraHelper.getInstance().convertCameraIndex(boolQuery,permissionValue);
	}
	/**
	 * 获取当前角色的拥有的摄像机资源
	 * 
	 * @param roleid
	 *            角色id
	 * @param isAdmin
	 *            是否管理员
	 * @return
	 * @throws UserException
	 */
	@Override
	public List<CameraPermissionVo> getRoleCameraPermissionTree(String roleid, Integer isAdmin) throws CameraException {
		// TODO Auto-generated method stub
		String key = SystemConstant.redis_role_cameraPermission_key + "_" + roleid;
		
		if (!redisDao.exists(key)) {
			Map<String, Object> modelMap = new HashMap<>(16);
			modelMap.put("roleId", roleid);
			modelMap.put("isAdmin", isAdmin);

			List<CameraPermissionVo> list = cameraPermissionMapper.selectRoleCameraPermission(modelMap);

			int expireTime = 10 * 60 + new Random().nextInt(30);
			redisDao.addList(key, list, expireTime);

			return list;
		}
		List<CameraPermissionVo> list = (List<CameraPermissionVo>) redisDao.getList(key).get(0);
		return list;
	}

	/**
	 * 获取热点摄像机
	 * 
	 * @param pageSize
	 *            返回条数
	 * @return
	 * @throws CameraException
	 */
	@Override
	public List<CameraPermissionVo> getHotsCamera(int pageSize) throws CameraException {
		// TODO Auto-generated method stub

		if (pageSize == 0) {
			pageSize = 50;
		}
		String key = SystemConstant.redis_hots_camera_key + "_" + pageSize;
		if (!redisDao.exists(key)) {
			List<CameraPermissionVo> list = cameraPermissionMapper.getHotsCamera(pageSize);

			int expireTime = 10 * 60 + new Random().nextInt(30);
			redisDao.addList(key, list, expireTime);

			return list;
		}
		List<CameraPermissionVo> list = (List<CameraPermissionVo>) redisDao.getList(key).get(0);
		return list;
	}

	/**
	 * 获取收藏的摄像机列表
	 * 
	 * @param account
	 *            用户账号
	 * @return
	 * @throws CameraException
	 */
	@Override
	public List<CameraPermissionVo> getCollectCameraList(String account) throws CameraException {
		// TODO Auto-generated method stub

		if (StringUtils.isEmpty(account)) {
			return Collections.emptyList();
		}
		String key = SystemConstant.redis_collect_camera_key + "_" + account;
		if (!redisDao.exists(key)) {
			List<CameraPermissionVo> list = cameraPermissionMapper.getCollectCameraList(account);

			int expireTime = 5 * 60 + new Random().nextInt(30);
			redisDao.addList(key, list, expireTime);

			return list;
		}
		List<CameraPermissionVo> list = (List<CameraPermissionVo>) redisDao.getList(key).get(0);
		return list;
	}

	/**
	 * 根据范围获取附近的摄像机列表
	 * 
	 * @param userId
	 *            用户id
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @param distance
	 *            搜索半径（距离 千米）
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @return
	 */
	@Override
	public EsPage<EsCameraVo> getDistanceRangeCameraList(CameraForm form,double latitude, double longitude,
			int distance,int currentPage,int pageSize)throws UserException {
		// TODO Auto-generated method stub
		if (latitude == 0L || distance == 0L) {
			return new EsPage<EsCameraVo>();
		}
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(form.getUserId());
		String highlightField = StringUtils.isEmpty(form.getQueryStr()) ? null : IndexField.DEVICE_NAME;
		BoolQueryBuilder boolQuery = getEsCameraBoolQuery(form, flag);
		
		EsPage<EsCameraVo> cameraList = null;
		if(StringUtils.isNotBlank(form.getGroupCode())) {
			cameraList = ElasticsearchUtils.geoDistanceList(SystemConstant.es_cameragroup_index,SystemConstant.es_cameragroup_mapping,EsCameraVo.class,
					currentPage,pageSize,latitude, longitude, distance,highlightField,boolQuery);
		}else {
			//判断用户是否超管
			if(flag) {
				//超管查询所有数据
				cameraList =  ElasticsearchUtils.geoDistanceList(SystemConstant.es_camera_index,SystemConstant.es_camera_mapping,EsCameraVo.class,
						currentPage,pageSize,latitude, longitude, distance,highlightField,boolQuery);
			}else {
				cameraList =  ElasticsearchUtils.geoDistanceList(SystemConstant.es_cameraPermission_index,SystemConstant.es_cameraPermission_mapping,EsCameraVo.class,
						currentPage,pageSize,latitude, longitude, distance,highlightField,boolQuery);
			}
		}
		return cameraList;

	}
	/**
	 * 根据选定矩形获取摄像机列表
	 * @param form 查询对象
	 * @param topleft_lat 左上角纬度
	 * @param topleft_lon 左上角经度
	 * @param bottomRight_lat  右下角纬度
	 * @param bottomRight_lon 右下角经度
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @return
	 */
	@Override
	public EsPage<EsCameraVo> getBoundingBoxCameraList(CameraForm form,double topleft_lat, double topleft_lon,
			double bottomRight_lat, double bottomRight_lon,int currentPage,int pageSize)throws UserException {
		// TODO Auto-generated method stub
		if (topleft_lat == 0L || topleft_lon == 0L || bottomRight_lat == 0L || bottomRight_lon == 0L) {
			return new EsPage<EsCameraVo>();
		}
		String highlightField = StringUtils.isEmpty(form.getQueryStr()) ? null : IndexField.DEVICE_NAME;
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(form.getUserId());
		BoolQueryBuilder boolQuery = getEsCameraBoolQuery(form, flag);

		//矩形
		GeoBoundingBoxQueryBuilder filter = AbstractEsFilter.geoBoundingBoxFilter(topleft_lat, topleft_lon, bottomRight_lat, bottomRight_lon);
		
		EsPage<EsCameraVo> cameraList = null;
		if(StringUtils.isNotBlank(form.getGroupCode())) {
			cameraList =ElasticsearchUtils.geoSearchDataPage(SystemConstant.es_cameragroup_index,SystemConstant.es_cameragroup_mapping,EsCameraVo.class,
					currentPage,pageSize,filter,highlightField,boolQuery);
		}else{
			//判断用户是否超管
			if(flag) {
				//超管查询所有数据
				cameraList = ElasticsearchUtils.geoSearchDataPage(SystemConstant.es_camera_index,SystemConstant.es_camera_mapping,EsCameraVo.class,
						currentPage,pageSize,filter,highlightField,boolQuery);
			}else {
				cameraList =ElasticsearchUtils.geoSearchDataPage(SystemConstant.es_cameraPermission_index,SystemConstant.es_cameraPermission_mapping,EsCameraVo.class,
						currentPage,pageSize,filter,highlightField,boolQuery);
			}
		}

		return cameraList;
	}
	/**
	 * 根据选定多边形获取摄像机列表
	 * @param form 查询对象
	 * @param points 地址列表
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @return
	 */
	@Override
	public EsPage<EsCameraVo> getPolygonCameraList(CameraForm form,List<PointVo> points,int currentPage,int pageSize)throws UserException {
		// TODO Auto-generated method stub
		if (points.isEmpty() || points.size() == 0) {
			return new EsPage<EsCameraVo>();
		}
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(form.getUserId());

		String highlightField = StringUtils.isEmpty(form.getQueryStr()) ? null : IndexField.DEVICE_NAME;
		BoolQueryBuilder boolQuery = getEsCameraBoolQuery(form, flag);
		
		GeoPolygonQueryBuilder filter = AbstractEsFilter.geoPolygonFilter(points);
		
		if(StringUtils.isNotBlank(form.getGroupCode())) {
			return ElasticsearchUtils.geoSearchDataPage(SystemConstant.es_cameragroup_index,SystemConstant.es_cameragroup_mapping,EsCameraVo.class,
					currentPage,pageSize,filter,highlightField,boolQuery);
		}
		//判断用户是否超管
		if(flag) {
			//超管查询所有数据
			return ElasticsearchUtils.geoSearchDataPage(SystemConstant.es_camera_index,SystemConstant.es_camera_mapping,EsCameraVo.class,
					currentPage,pageSize,filter,highlightField,boolQuery);
		}

		return ElasticsearchUtils.geoSearchDataPage(SystemConstant.es_cameraPermission_index,SystemConstant.es_cameraPermission_mapping,EsCameraVo.class,
				currentPage,pageSize,filter,highlightField,boolQuery);

	}
	/**
	 * 根据条件获取摄像机列表【查询ES】
	 * @param form 查询对象
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @return
	 */
	@Override
	public EsPage<EsCameraVo> searchCameraList(CameraForm form,int currentPage,int pageSize)throws UserException {
		return this.searchCameraList(form, currentPage, pageSize, null, null);
	}
	/**
	 * 根据条件获取摄像机列表【查询ES】
	 * @param form 查询对象
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @param fields 返回字段 ,多个已“,”分割  null 返回所有字段 [参考EsCameraVo字段名称] 
	 * @param sortField 排序字段 [参考EsCameraVo字段名称]
	 * @return
	 */
	@Override
	public EsPage<EsCameraVo> searchCameraList(CameraForm form,int currentPage,int pageSize,String fields,String sortField)throws UserException {
		// TODO Auto-generated method stub
		
		currentPage = currentPage ==0 ? 1:currentPage;
		pageSize = pageSize == 0 ? 20:pageSize;
		

		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(form.getUserId());
		
		String highlightField = StringUtils.isEmpty(form.getQueryStr()) ? null : IndexField.DEVICE_NAME;
		BoolQueryBuilder boolQuery = getEsCameraBoolQuery(form, flag);
		
		return searchEs(flag, form, currentPage, pageSize, fields, sortField, highlightField, boolQuery);

	}


	@Override
	public CameraVo findCameraRecord(String deviceId) {
		// TODO Auto-generated method stub
		List<CameraVo> list = cameraMapper.findCameraRecord(deviceId);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	
	/**
	 * 查询搜索赢钱
	 * @param flag
	 * @param form
	 * @param currentPage
	 * @param pageSize
	 * @param fields
	 * @param sortField
	 * @param highlightField
	 * @param boolQuery
	 * @return
	 * @throws UserException
	 */
	public EsPage<EsCameraVo> searchEs(boolean flag,CameraForm form,int currentPage,int pageSize,String fields,String sortField,String highlightField, BoolQueryBuilder boolQuery)throws UserException{
		if(flag) {
			return searchEsCamera(form, currentPage, pageSize, fields, sortField, highlightField, boolQuery);
		}
		//用户设备所引库
		if(StringUtils.isNotEmpty(form.getGroupCode())) {
			return searchEsCameraGroup(form, currentPage, pageSize, fields, sortField, highlightField, boolQuery);
		}else {
			return searchEsCameraPermission(form, currentPage, pageSize, fields, sortField, highlightField, boolQuery);
		}
		
	}
	/**
	 * 查询hovdu_camera索引数据
	 * @param form
	 * @param currentPage
	 * @param pageSize
	 * @param fields
	 * @param sortField
	 * @param highlightField
	 * @param boolQuery
	 * @return
	 * @throws UserException
	 */
	public EsPage<EsCameraVo> searchEsCamera(CameraForm form,int currentPage,int pageSize,String fields,String sortField,String highlightField, BoolQueryBuilder boolQuery)throws UserException{
		String index = SystemConstant.es_camera_index;
		String type = SystemConstant.es_camera_mapping;
		if(StringUtils.isNotEmpty(form.getGroupCode())) {
			index = SystemConstant.es_cameragroup_index;
			type = SystemConstant.es_cameragroup_mapping;
		}		
		EsPage<EsCameraVo> page = ElasticsearchUtils.searchDataPage (index,type,EsCameraVo.class, 
				currentPage,pageSize,fields,sortField, highlightField, boolQuery);
		return page;		
	}
	
	/**
	 * 封装ES查询对象
	 * @param form
	 * @param flag
	 * @return
	 */
	public BoolQueryBuilder getEsCameraBoolQuery(CameraForm form,Boolean flag) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!flag) {
			//摄像机预授权处理
			permissionPlanService.filterCamera(form.getUserId(), boolQuery);
			
		}
		if(form.isHighFilter()) {
			/********************************added by liyong 2018-10-15 start*********************************************/
			//高级查询添加es查询条件
			if(StringUtils.isNotEmpty(form.getDeviceName())) {//摄像机名称
				boolQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_NAME, CameraHelper.escape(form.getDeviceName())).analyzer("query_ansj")
						.operator(Operator.AND));
			}
			if(StringUtils.isNotEmpty(form.getDeviceCode())) {//摄像机编码
				boolQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_CODE, form.getDeviceCode()));
			}
			if(form.getDeviceType() != null) {//摄像机类型
				boolQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_TYPE, form.getDeviceType()));
			}
			if(StringUtils.isNotEmpty(form.getEncoderServerId())) {//所属编码器
				boolQuery.must(QueryBuilders.matchQuery(IndexField.ENCODER_SERVER_ID, form.getEncoderServerId()));
			}
			if(StringUtils.isNotEmpty(form.getIp())) {//摄像机ip地址
				boolQuery.must(QueryBuilders.matchQuery(IndexField.IP, form.getIp()));
			}
			if(form.getStatus() != null) {//摄像机状态
				boolQuery.must(QueryBuilders.matchQuery(IndexField.STATUS, form.getStatus()));
			}
			if(StringUtils.isNotEmpty(form.getConstructionUnit())) {//建设单位
				boolQuery.must(QueryBuilders.matchQuery(IndexField.CONSTRUCTION_UNIT, form.getConstructionUnit()));
			}
			if(StringUtils.isNotEmpty(form.getUrbanConstructionUnit())) {//承建单位
				boolQuery.must(QueryBuilders.matchQuery(IndexField.URBAN_CONSTRUCTION_UNIT, form.getUrbanConstructionUnit()));
			}
			if(form.getDeviceCompany() != null) {//设备厂商
				boolQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_COMPANY, form.getDeviceCompany()));
			}
			if(form.getSxjssbm() != null) {//所属部门
				boolQuery.must(QueryBuilders.matchQuery(IndexField.SXJSSBM, form.getSxjssbm()));
			}
			if(form.getPositionType() != null) {//位置类型
				boolQuery.must(QueryBuilders.matchQuery(IndexField.POSITION_TYPE, form.getPositionType()));
			}
			if(form.getCollectionCategory() != null) {//采集点类别
				boolQuery.must(QueryBuilders.matchQuery(IndexField.COLLECTION_CATEGORY, form.getCollectionCategory()));
			}
			if(form.getFunctionalType() != null) {//功能类型
				boolQuery.must(QueryBuilders.matchQuery(IndexField.FUNCTIONAL_TYPE, form.getFunctionalType()));
			}
			/*********************************added by liyong end********************************************************/
		}else {
			if(form.getDeviceId() != null) {
				boolQuery.must(QueryBuilders.termQuery(IndexField.DEVICE_ID, form.getDeviceId()));
			}
			if(StringUtils.isNotEmpty(form.getAddressCode())) {
				String[] addressArr = form.getAddressCode().split(",");
				BoolQueryBuilder addressQuery = QueryBuilders.boolQuery();
				for(String addressCode:addressArr) {
					addressQuery.should(QueryBuilders.prefixQuery(IndexField.ADDRESS_CODE,addressCode));
				}
				boolQuery.must(addressQuery);
			}
			if(StringUtils.isNotEmpty(form.getOrgCode())) {
				String[] orgArr = form.getOrgCode().split(",");
				BoolQueryBuilder orgQuery = QueryBuilders.boolQuery();
				for(String orgCode:orgArr) {
					orgQuery.should(QueryBuilders.prefixQuery(IndexField.ORG_CODE,orgCode));
				}
				boolQuery.must(orgQuery);
			}
			if(StringUtils.isNotEmpty(form.getProjectId())) {
				String[] projectArr = form.getProjectId().split(",");
				boolQuery.must(QueryBuilders.termsQuery(IndexField.PROJECT_ID,projectArr));
			}
			if(StringUtils.isNotEmpty(form.getGroupCode())) {
				String[] groupArr = form.getGroupCode().split(",");
				BoolQueryBuilder groupQuery = QueryBuilders.boolQuery();
				for(String groupCode:groupArr) {
					groupQuery.should(QueryBuilders.prefixQuery(IndexField.GROUP_CODE,groupCode));
				}
				boolQuery.must(groupQuery);
			}
		}
		String query = form.getQueryStr();
		if(StringUtils.isNotEmpty(query)) {
			//新增特殊字符处理
			query = CameraHelper.escape(query);
			
			String[] arr = query.split(" ");
			BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
			customQuery(shouldQuery,arr, IndexField.DEVICE_NAME,1.0f);	
			customQuery(shouldQuery,arr, IndexField.INSTALL_ADDRESS,0.05f);
			customQuery(shouldQuery,arr, IndexField.ORG_NAME,0.00005f);
			customQuery(shouldQuery,arr, IndexField.PROJECT_NAME,0.00005f);
			customQuery(shouldQuery,arr, IndexField.ADDRESS_NAME,0.00005f);
			if(StringUtils.isNotEmpty(form.getGroupCode())) {
				customQuery(shouldQuery,arr, IndexField.GROUP_NAME,0.00005f);
			}
			
			
			
			boolQuery.must(shouldQuery);
		}
		return boolQuery;
	}
	
	private void customQuery(BoolQueryBuilder shouldQuery,String[] arr, String matchField,float boost) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		for(String query:arr) {
			boolQuery.must(QueryBuilders.matchQuery(matchField, query).analyzer("query_ansj")
			.operator(Operator.AND).boost(boost));
		}
		shouldQuery.should(boolQuery);
	}
	/**
	 * 查询hovdu_camera_permission索引数据
	 * @param form
	 * @param currentPage
	 * @param pageSize
	 * @param fields
	 * @param sortField
	 * @param highlightField
	 * @param boolQuery
	 * @return
	 * @throws UserException
	 */
	private EsPage<EsCameraVo> searchEsCameraGroup(CameraForm form,int currentPage,int pageSize,String fields,String sortField,String highlightField, BoolQueryBuilder boolQuery)throws UserException{
		return ElasticsearchUtils.searchDataPage (SystemConstant.es_cameragroup_index,SystemConstant.es_cameragroup_mapping,EsCameraVo.class, 
				currentPage,pageSize,fields,sortField, highlightField, boolQuery);
	}
	/**
	 * 查询hovdu_camera_group索引数据
	 * @param form
	 * @param currentPage
	 * @param pageSize
	 * @param fields
	 * @param sortField
	 * @param highlightField
	 * @param boolQuery
	 * @return
	 * @throws UserException
	 */
	private EsPage<EsCameraVo> searchEsCameraPermission(CameraForm form,int currentPage,int pageSize,String fields,String sortField,String highlightField, BoolQueryBuilder boolQuery)throws UserException{
		return ElasticsearchUtils.searchDataPage (SystemConstant.es_cameraPermission_index,SystemConstant.es_cameraPermission_mapping,EsCameraVo.class, 
				currentPage,pageSize,fields,sortField, highlightField, boolQuery);
	}
	
	
	/**
	 * es高级搜索
	 * @param form 查询对象
	 * @param fields 返回字段 ,多个已“,”分割  null 返回所有字段 [参考EsCameraVo字段名称] 
	 * @param aggEm 分组字段枚举类型
	 * @return
	 * @throws UserException
	 */
	@Override
	public List<CameraPermissionVo> searchHighCameraList(CameraForm form, String fields,AggFieldEnum aggEm) throws UserException {
		// TODO Auto-generated method stub
		BoolQueryBuilder boolQuery = null;
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(form.getUserId());
		if(flag) {
			//超管查询所有数据
			boolQuery = QueryBuilders.boolQuery();
		}else {
			//普通权限需要进行用户数据权限过滤
			boolQuery = getEsCameraBoolQuery(form, flag);
		}

		if(StringUtils.isNotEmpty(form.getDeviceName())) {
			//新增特殊字符处理
			boolQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_NAME,			
					CameraHelper.escape(form.getDeviceName())).analyzer("query_ansj")
					.operator(Operator.AND));
		}
		if(StringUtils.isNotEmpty(form.getDeviceCode())) {
			boolQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_CODE,form.getDeviceCode()));
		}
		if(form.getDeviceType() != null) {
			boolQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_TYPE,form.getDeviceType().toString()));
		}
		if(StringUtils.isNotEmpty(form.getEncoderServerId())) {
			boolQuery.must(QueryBuilders.matchQuery(IndexField.ENCODER_SERVER_ID,form.getEncoderServerId()));
		}
		if(StringUtils.isNotEmpty(form.getEncoderServerName())) {
			boolQuery.must(QueryBuilders.matchQuery(IndexField.ENCODER_SERVER_NAME,CameraHelper.escape(form.getEncoderServerName())).analyzer("query_ansj")
					.operator(Operator.AND));
		}
		if(StringUtils.isNotEmpty(form.getIp())) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.IP,form.getIp()));
		}
		if(form.getStatus()!=null) {
			boolQuery.must(QueryBuilders.matchQuery(IndexField.STATUS,form.getStatus().toString()));
		}
		if(StringUtils.isNotEmpty(form.getConstructionUnit())) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.CONSTRUCTION_UNIT,CameraHelper.escape(form.getConstructionUnit())));
		}
		if(StringUtils.isNotEmpty(form.getUrbanConstructionUnit())) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.URBAN_CONSTRUCTION_UNIT,CameraHelper.escape(form.getUrbanConstructionUnit())));
		}
		if(form.getDeviceCompany() != null) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.DEVICE_COMPANY,form.getDeviceCompany()));
		}
		if(StringUtils.isNotEmpty(form.getOrgName())) {
			boolQuery.must(QueryBuilders.matchQuery(IndexField.ORG_NAME,CameraHelper.escape(form.getOrgName())).analyzer("query_ansj")
					.operator(Operator.AND));
		}
		if(form.getPositionType()!=null) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.POSITION_TYPE,form.getPositionType().toString()));
		}
		if(form.getCollectionCategory()!=null) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.COLLECTION_CATEGORY,form.getCollectionCategory().toString()));
		}
		if(form.getFunctionalType()!=null) {
			boolQuery.must(QueryBuilders.termQuery(IndexField.FUNCTIONAL_TYPE,form.getFunctionalType().toString()));
		}

		SearchResponse searchResponse = null;
		if(flag) {
			//超管查询所有数据
			searchResponse = ElasticsearchUtils.searchAvgDataPage (SystemConstant.es_camera_index,SystemConstant.es_camera_mapping,fields,IndexField.ADDRESS_CODE,aggEm.getValue(), null, boolQuery);
		}else {
			searchResponse = ElasticsearchUtils.searchAvgDataPage (SystemConstant.es_cameraPermission_index,SystemConstant.es_cameraPermission_mapping,fields,IndexField.ADDRESS_CODE,aggEm.getValue(), null, boolQuery);
		}
		if(searchResponse.status().getStatus() != SystemConstant.success_stauts) {
			return Collections.emptyList();
		}
		//获取摄像机资源权限值
		String permissionValueArr = commonService.getPermissionValue();
		
		LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询总记录数:{}",searchResponse.getHits().totalHits);
		
		
		List<CameraPermissionVo> dataList = new ArrayList<>();
		List<CameraPermissionVo> list = CameraHelper.getInstance().convertCameraPermission(searchResponse,aggEm,flag,permissionValueArr);
		
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		dataList.addAll(list);
		
		//根据分组编码查找对应所属上级单位
		List<CameraPermissionVo> treeList = new ArrayList<>();
		Map<String, CameraPermissionVo> treeMap = new HashMap<>(16);
		if(aggEm.equals(AggFieldEnum.ADDRESS_CODE)) {
			treeList = treeMapper.findAddressTree();
			treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getCode, Function.identity(),(entity1,entity2) -> entity1));
		}else {
			treeList = treeMapper.findOrganizationTree();
			treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getCode, Function.identity(),(entity1,entity2) -> entity1));
		}
		
		Terms terms = (Terms) searchResponse.getAggregations().get("terms");
		if(terms.getBuckets().size()>0) {
			//根据分组id
			List<CameraPermissionVo> nodeList = new ArrayList<>();
			for(Terms.Bucket bucket:terms.getBuckets()) {
				String key = bucket.getKey().toString();
				
				CameraPermissionVo currentNode = treeMap.get(key);
				
				nodeList.add(currentNode);
				
				//获取当前节点的上级节点
				reverseSearch(treeList, nodeList, currentNode);
			}
			dataList.addAll(nodeList);
		}
		
		return dataList;
	}
	

	
	/**
	 * es普通搜索【整合权限预案】
	 * @param queryStr 查询字段
	 * @param userId 用户id
	 * @param fields 返回字段 ,多个已“,”分割  null 返回所有字段 [参考EsCameraVo字段名称] 
	 * @param aggEm 分组字段枚举类型
	 * @return
	 * @throws UserException
	 */
	@Override
	public List<CameraPermissionVo>  searchNomalCameraList(String queryStr,String userId,String fields,AggFieldEnum aggEm)throws UserException{
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();;
		//判断用户是否超级管理员
		boolean flag = commonService.isSuperAdmin(userId);
		if(!flag) {
			/**
			 * 普通用户处理
			 */
			//摄像机预授权处理
			permissionPlanService.filterCamera(userId, boolQuery);
			
		}
		BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
		if(StringUtils.isNotEmpty(queryStr)) {
			String q = CameraHelper.escape(queryStr);//特殊字符处理
			if(queryStr.contains("code")) {//摄像机编码
				String deviceCode=null;
				if(queryStr.contains("code:")) {
					deviceCode=queryStr.replace("code:", "");
				}else {
					deviceCode=queryStr.substring(5, queryStr.length());
				}
				shouldQuery.must(QueryBuilders.matchQuery(IndexField.DEVICE_CODE,deviceCode).boost(1.0f));// 编码
			}else {
				shouldQuery.should(QueryBuilders.matchQuery(IndexField.DEVICE_NAME,q).analyzer("query_ansj")// 设备名称
						.operator(Operator.AND).boost(1.0f));
			}
			
			if(AggFieldEnum.ADDRESS_CODE.equals(aggEm)) {
				shouldQuery.should(QueryBuilders.matchQuery(IndexField.ADDRESS_NAME,q).operator(Operator.AND).boost(0.5f));			
			}else if(AggFieldEnum.ORG_CODE.equals(aggEm)) {
				shouldQuery.should(QueryBuilders.matchQuery(IndexField.ORG_NAME,q).analyzer("query_ansj")
						.operator(Operator.AND).boost(0.5f));			
			}else if(AggFieldEnum.PROJECT_NAME.equals(aggEm)) {
				shouldQuery.should(QueryBuilders.matchQuery(IndexField.PROJECT_NAME,q).analyzer("query_ansj")
						.operator(Operator.AND).boost(0.5f));			
			}else{
				shouldQuery.should(QueryBuilders.matchQuery(IndexField.GROUP_NAME,q).analyzer("query_ansj")
						.operator(Operator.AND).boost(0.5f));			
			}
			
		}
		boolQuery.must(shouldQuery);
		SearchResponse searchResponse = null;
		//摄像机分组单独处理
		if(AggFieldEnum.GROUP_NAME.equals(aggEm)) {
			if(flag) {
				//超管查询所有数据
				searchResponse = ElasticsearchUtils.searchAvgDataPage (SystemConstant.es_cameragroup_index,SystemConstant.es_cameragroup_mapping,fields,IndexField.ADDRESS_CODE,aggEm.getValue(), null, boolQuery);
			}else {
				searchResponse = ElasticsearchUtils.searchAvgDataPage (SystemConstant.es_user_cameragroup_index,SystemConstant.es_user_cameragroup_mapping,fields,IndexField.ADDRESS_CODE,aggEm.getValue(), null, boolQuery);
			}
		}else {
			if(flag) {
				//超管查询所有数据
				searchResponse = ElasticsearchUtils.searchAvgDataPage (SystemConstant.es_camera_index,SystemConstant.es_camera_mapping,fields,IndexField.ADDRESS_CODE,aggEm.getValue(), null, boolQuery);
			}else {
				searchResponse = ElasticsearchUtils.searchAvgDataPage (SystemConstant.es_cameraPermission_index,SystemConstant.es_cameraPermission_mapping,fields,IndexField.ADDRESS_CODE,aggEm.getValue(), null, boolQuery);
			}
		}
		

		if(searchResponse.status().getStatus() != SystemConstant.success_stauts) {
			return Collections.emptyList();
		}
		//获取摄像机资源权限值
		String permissionValueArr = commonService.getPermissionValue();
		
		LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询总记录数:{}",searchResponse.getHits().totalHits);
		
		
		List<CameraPermissionVo> dataList = new ArrayList<>();
		List<CameraPermissionVo> list = CameraHelper.getInstance().convertCameraPermission(searchResponse,aggEm,flag,permissionValueArr);
		
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		dataList.addAll(list);
		
		//根据分组编码查找对应所属上级单位
		List<CameraPermissionVo> treeList = new ArrayList<>();
		Map<String, CameraPermissionVo> treeMap = new HashMap<>(16);
		if(aggEm.equals(AggFieldEnum.ADDRESS_CODE)) {
			treeList = treeMapper.findAddressTree();
			treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getCode, Function.identity(),(entity1,entity2) -> entity1));
		}else if(aggEm.equals(AggFieldEnum.ORG_CODE)){
			treeList = treeMapper.findOrganizationTree();
			treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getId, Function.identity(),(entity1,entity2) -> entity1));
		}else if(aggEm.equals(AggFieldEnum.PROJECT_NAME)) {
			treeList = treeMapper.findProjectTree();
			treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getId, Function.identity(),(entity1,entity2) -> entity1));
		}else {
			Map<String,Object> groupTreeModel = new HashMap<>();
			groupTreeModel.put("isAdmin", 1);
			treeList = treeMapper.findCameraGroupTree(groupTreeModel);
			treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getId, Function.identity(),(entity1,entity2) -> entity1));
		}
		
		Terms terms = (Terms) searchResponse.getAggregations().get("terms");
		if(terms.getBuckets().size()>0) {
			//根据分组id
			List<CameraPermissionVo> nodeList = new ArrayList<>();
			for(Terms.Bucket bucket:terms.getBuckets()) {
				String key = bucket.getKey().toString();
				CameraPermissionVo currentNode = treeMap.get(key);
				
				nodeList.add(currentNode);
				
				//获取当前节点的上级节点
				reverseSearch(treeList, nodeList, currentNode);
			}
//			LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>nodeList:"+JSON.toJSONString(nodeList));

			dataList.addAll(nodeList);
		}
		
		return dataList;
	}
	/**
	 * 反向查找元素
	 * @param treeList
	 * @param nodeList
	 * @param currentNode
	 */
	private void reverseSearch(List<CameraPermissionVo> treeList,List<CameraPermissionVo> nodeList,CameraPermissionVo currentNode) {
		if(currentNode == null) {
			return;
		}
		//获取上一级节点对象
		if(DEFAULT_PID.equals(currentNode.getPid()) || StringUtils.isEmpty(currentNode.getPid())) {
			return;
		}
		CameraPermissionVo parentVo = treeList.stream().filter(vo->vo.getId().equals(currentNode.getPid())).findFirst().get();
		//元素已存在
		if(nodeList.contains(parentVo)){
			return;
		}
		nodeList.add(parentVo);
		
		//递归查找上一级元素
		reverseSearch(treeList, nodeList, parentVo);
	}
	
	/**
	 * 用户摄像机权限验证
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public List<CheckCameraVo> checkUserCameraRights(String userId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(userId)) {
			return Collections.emptyList();
		}
		return cameraMapper.checkUserCameraRights(userId);
	}

	/**
	 * 用户摄像机权限验证
	 * @param userId 用户id
	 * @param deviceIds 设备id集合
	 * @param type 类型 1： ids为camearId  2:为deviceId
	 * @return
	 */
	@Override
	public Map<String,Boolean> checkUserCameraRights(String userId, List<String> ids,int  type) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(userId) || (ids.isEmpty() || ids.size()==0)) {
			return Collections.emptyMap();
		}
		Map<String, Boolean> resultMap = new HashMap<>(16);
		
		List<CheckCameraVo> dataList = cameraMapper.checkUserCameraRights(userId);
		List<String> selectIds = new ArrayList<>();
		if(type == 1) {
			selectIds = dataList.stream().map(CheckCameraVo::getCamearId).collect(Collectors.toList());
		}else {
			selectIds = dataList.stream().map(CheckCameraVo::getDeviceId).collect(Collectors.toList());
		}
		for(String id:ids){
			resultMap.put(id, selectIds.contains(id));
		}
		return resultMap;
	}


	/**
	 * 树形分组枚举
	 * @author wanshaojian
	 *
	 */
	public enum TreeGroupEnum{
		ADDRESS,ORG,PROJECT,GROUP;
	}

}
