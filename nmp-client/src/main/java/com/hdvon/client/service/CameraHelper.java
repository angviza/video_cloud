package com.hdvon.client.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.BeanUtils;

import com.hdvon.client.enums.AggFieldEnum;
import com.hdvon.client.es.ElasticsearchUtils;
import com.hdvon.client.service.impl.CameraServiceImpl.TreeGroupEnum;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.client.vo.EsCameraGroupVo;
import com.hdvon.client.vo.EsCameraPermissionVo;
import com.hdvon.client.vo.EsCameraVo;
import com.hdvon.client.vo.EsUserCameraGroupVo;
import com.hdvon.nmp.common.SystemConstant;

/**
 * 用户资源授权业务实现
 * 
 * @author wanshaojian
 *
 */
public class CameraHelper {

	private volatile static CameraHelper instance;

	private CameraHelper() {
	};

	public static CameraHelper getInstance() {
		if (instance == null) {
			synchronized (CameraHelper.class) {
				return new CameraHelper();
			}
		}
		return instance;

	}

	/**
	 * 摄像机对象转换
	 * 
	 * @param list
	 *            摄像机列表
	 * @param isSuperAdmin
	 *            是否超管 true 是
	 * @param permissionValue
	 *            摄像机所有权限集合
	 * @return
	 */
	public List<CameraPermissionVo> geoCameraObjectConvert(List<EsCameraVo> list, boolean isSuperAdmin,
			String permissionValue) {
		// TODO Auto-generated method stub
		List<CameraPermissionVo> dataList = new ArrayList<>();

		for (EsCameraVo obj : list) {
			CameraPermissionVo vo = new CameraPermissionVo();

			BeanUtils.copyProperties(obj, vo);

			String[] geo = obj.getLocation().split(",");
			vo.setId(String.valueOf(obj.getDeviceId()));
			vo.setName(obj.getDeviceName());
			vo.setLatitude(Double.parseDouble(geo[0]));
			vo.setLongitude(Double.parseDouble(geo[1]));

			if (isSuperAdmin) {
				vo.setPermissionValue(permissionValue);
			}
			dataList.add(vo);
		}
		return dataList;
	}

	/**
	 * 转换camera索引对象
	 * 
	 * @param boolQuery
	 * @param permissionValue
	 *            摄像机权限集合
	 * @param
	 * @return
	 */
	public List<CameraPermissionVo> convertCameraIndex(BoolQueryBuilder boolQuery, String permissionValue) {
		List<CameraPermissionVo> cameraList = new ArrayList<>();
		// 从ES中获取当前层级的所有摄像机列表
		List<EsCameraVo> esCameraList = ElasticsearchUtils.searchListData(SystemConstant.es_camera_index,
				SystemConstant.es_camera_mapping, EsCameraVo.class, null, boolQuery);
		if (esCameraList.isEmpty() || esCameraList.size() == 0) {
			return Collections.emptyList();
		}
		for (EsCameraVo vo : esCameraList) {
			CameraPermissionVo cameraPermVo = new CameraPermissionVo();
			cameraPermVo.setId(vo.getId());
			cameraPermVo.setCameraId(vo.getCameraId());
			cameraPermVo.setDeviceId(vo.getDeviceId().toString());
			cameraPermVo.setName(vo.getDeviceName());
			cameraPermVo.setDeviceCode(vo.getDeviceCode());
			cameraPermVo.setDeviceType(String.valueOf(vo.getDeviceType()));
			// 地址树类型
			cameraPermVo.setPid(vo.getAddressId() == null ? "" : String.valueOf(vo.getAddressId()));
			cameraPermVo.setPermissionValue(permissionValue);

			cameraList.add(cameraPermVo);
		}
		return cameraList;
	}

	/**
	 * 转换cameraGroup索引对象
	 * 
	 * @param boolQuery
	 * @return
	 */
	public List<CameraPermissionVo> convertCameraGroupIndex(BoolQueryBuilder boolQuery, String permissionValue) {
		List<CameraPermissionVo> cameraList = new ArrayList<>();
		// 从ES中获取当前层级的所有摄像机列表
		List<EsCameraGroupVo> esCameraList = ElasticsearchUtils.searchListData(SystemConstant.es_cameragroup_index,
				SystemConstant.es_cameragroup_mapping, EsCameraGroupVo.class, null, boolQuery);
		if (esCameraList.isEmpty() || esCameraList.size() == 0) {
			return Collections.emptyList();
		}

		for (EsCameraGroupVo vo : esCameraList) {
			CameraPermissionVo cameraPermVo = new CameraPermissionVo();
			cameraPermVo.setId(vo.getId());
			cameraPermVo.setCameraId(vo.getCameraId());
			cameraPermVo.setDeviceId(vo.getDeviceId().toString());
			cameraPermVo.setName(vo.getDeviceName());
			cameraPermVo.setDeviceCode(vo.getDeviceCode());
			cameraPermVo.setDeviceType(String.valueOf(vo.getDeviceType()));
			// 地址树类型
			cameraPermVo.setPid(vo.getAddressId() == null ? "" : String.valueOf(vo.getAddressId()));
			cameraPermVo.setPermissionValue(permissionValue);

			cameraList.add(cameraPermVo);
		}
		return cameraList;
	}

	/**
	 * 转换cameraPermission索引对象
	 * 
	 * @param type
	 * @param boolQuery
	 * @return
	 */
	public List<CameraPermissionVo> convertCameraPermissionIndex(TreeGroupEnum treeEm, BoolQueryBuilder boolQuery) {
		List<CameraPermissionVo> cameraList = new ArrayList<>();
		// 从ES中获取当前层级的所有摄像机列表
		List<EsCameraPermissionVo> esCameraList = ElasticsearchUtils.searchListData(
				SystemConstant.es_cameraPermission_index, SystemConstant.es_cameraPermission_mapping,
				EsCameraPermissionVo.class, null, boolQuery);
		if (esCameraList.isEmpty() || esCameraList.size() == 0) {
			return Collections.emptyList();
		}
		for (EsCameraPermissionVo vo : esCameraList) {
			CameraPermissionVo cameraPermVo = new CameraPermissionVo();
			cameraPermVo.setId(vo.getId());
			cameraPermVo.setCameraId(vo.getCameraId());
			cameraPermVo.setDeviceId(vo.getDeviceId().toString());
			cameraPermVo.setName(vo.getDeviceName());
			cameraPermVo.setDeviceCode(vo.getDeviceCode());
			cameraPermVo.setDeviceType(String.valueOf(vo.getDeviceType()));
			int isProject = 0;
			// 地址树类型
			if (treeEm.equals(TreeGroupEnum.ADDRESS)) {
				cameraPermVo.setPid(vo.getAddressId().toString());
			} else if (treeEm.equals(TreeGroupEnum.ORG)) {
				// 新政区域
				cameraPermVo.setPid(vo.getOrgId().toString());
			} else if (treeEm.equals(TreeGroupEnum.PROJECT)) {
				// 项目分组
				isProject = 1;
				cameraPermVo.setPid(vo.getProjectId().toString());
			}
			cameraPermVo.setIsProject(isProject);
			cameraPermVo.setPermissionValue(vo.getPermissionValue());

			cameraList.add(cameraPermVo);
		}
		return cameraList;
	}

	/**
	 * 数据转换
	 * 
	 * @param searchResponse
	 * @param aggEm
	 * @param flag
	 *            是否超管
	 * @param permissionValue
	 *            摄像机权限集合
	 */
	public List<CameraPermissionVo> convertCameraPermission(SearchResponse searchResponse, AggFieldEnum aggEm,
			boolean flag, String permissionValue) {
		List<CameraPermissionVo> sourceList = new ArrayList<CameraPermissionVo>();

		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			searchHit.getSourceAsMap().put("id", searchHit.getId());
			Map<String, Object> sourceMap = searchHit.getSourceAsMap();

			String deviceId = sourceMap.get("deviceId").toString();
			String deviceName = sourceMap.get("deviceName").toString();
			String cameraId = sourceMap.get("cameraId").toString();
			String deviceCode = sourceMap.get("deviceCode").toString();
			String deviceType = sourceMap.get("deviceType").toString();
			String prmsValue = null;
			if (!flag) {
				prmsValue = sourceMap.get("permissionValue").toString();
			}

			String pid = null;
			if (AggFieldEnum.ADDRESS_CODE.equals(aggEm)) {
				pid = sourceMap.containsKey("addressId") ? sourceMap.get("addressId").toString() : null;
			} else if (AggFieldEnum.ORG_CODE.equals(aggEm)) {
				pid = sourceMap.get("orgId") == null ? null : sourceMap.get("orgId").toString();
			} else if (AggFieldEnum.PROJECT_NAME.equals(aggEm)) {
				pid = sourceMap.containsKey("projectId") ? sourceMap.get("projectId").toString() : null;
			} else {
				pid = sourceMap.containsKey("groupId") ? sourceMap.get("groupId").toString() : null;
			}

			CameraPermissionVo cameraPermVo = new CameraPermissionVo();
			cameraPermVo.setId(deviceId);
			cameraPermVo.setCameraId(cameraId);
			cameraPermVo.setDeviceId(deviceId);
			cameraPermVo.setName(deviceName);
			cameraPermVo.setDeviceCode(deviceCode);
			cameraPermVo.setDeviceType(deviceType);
			cameraPermVo.setPid(pid);
			cameraPermVo.setIsProject(0);
			cameraPermVo.setPermissionValue(flag ? permissionValue : prmsValue);

			sourceList.add(cameraPermVo);
		}

		return sourceList;
	}

	/**
	 * 转换userCameraGroup索引对象
	 * 
	 * @param boolQuery
	 * @return
	 */
	public List<CameraPermissionVo> convertUserCameraGroupIndex(BoolQueryBuilder boolQuery) {
		List<CameraPermissionVo> cameraList = new ArrayList<>();
		// 从ES中获取当前层级的所有摄像机列表
		List<EsUserCameraGroupVo> esCameraList = ElasticsearchUtils.searchListData(
				SystemConstant.es_user_cameragroup_index, SystemConstant.es_user_cameragroup_mapping,
				EsUserCameraGroupVo.class, null, boolQuery);
		if (esCameraList.isEmpty() || esCameraList.size() == 0) {
			return Collections.emptyList();
		}
		for (EsUserCameraGroupVo vo : esCameraList) {
			CameraPermissionVo cameraPermVo = new CameraPermissionVo();
			cameraPermVo.setId(vo.getId());
			cameraPermVo.setCameraId(vo.getCameraId());
			cameraPermVo.setDeviceId(vo.getDeviceId().toString());
			cameraPermVo.setName(vo.getDeviceName());
			cameraPermVo.setDeviceCode(vo.getDeviceCode());
			cameraPermVo.setDeviceType(String.valueOf(vo.getDeviceType()));
			cameraPermVo.setPid(String.valueOf(vo.getGroupId()));
			cameraPermVo.setPermissionValue(vo.getPermissionValue());

			cameraList.add(cameraPermVo);
		}
		return cameraList;

	}

	/**
	 * 字符串转化为Long的list集合
	 * 
	 * @param str
	 * @return
	 */
	public static List<Long> convertIdList(String str) {
		String[] idArr = str.split(",");
		List<Long> idList = new ArrayList<>();
		for (String id : idArr) {
			if (!id.isEmpty() && id != null) {
				idList.add(Long.parseLong(id));
			}
		}
		return idList;
	}

	/**
	 * Returns a String where those characters that QueryParser expects to be
	 * escaped are escaped by a preceding <code>\</code>.
	 */
	public static String escape(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			// 过滤 c == '+' and c == '-' 
			if (c == '\\' ||  c == '!' || c == '(' ||  c == ':' || c == '^'
					|| c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
					|| c == '|' || c == '&' || c == '/') {
				sb.append('\\').append(c);
			}else if(c == '（' || c == ')') {
				sb.append("");
			}else{
				sb.append(c);
			}
			
		}
		return sb.toString();
	}

	/**
     * 根据属性名获取属性值
     * 
     * @param fieldName
     * @param object
     * @return
     */
    public static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return  (String)field.get(object);
        } catch (Exception e) {
            return null;
        } 
    }

	/**
     * 根据属性名获取属性值
     * 
     * @param fieldName
     * @param object
     * @return
     */
    public static Long getFieldLongValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return  (Long)field.get(object);
        } catch (Exception e) {
            return null;
        } 
    }
//	public static void main(String[] args) {
//		System.out.println(CameraHelper.escape("70027公园前站（一号线)东站车尾"));
//	}
}
