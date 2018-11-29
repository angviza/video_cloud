package com.hdvon.client.mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.client.vo.CameraPermissionVo;
/**
 * 获取摄像机资源树列表
 * @author Administrator
 *
 */
public interface TreeMapper {
	
	/**
	 * 层级获取地址列表
	 * @return
	 */
	List<CameraPermissionVo> findAddressTree();
	
	
	/**
	 * 层级获取行政区域树列表
	 * @return
	 */
	List<CameraPermissionVo> findOrganizationTree();
	
	
	/**
	 * 层级获取地址列表
	 * @param map
	 * @return
	 */
	List<CameraPermissionVo> findCameraGroupTree(Map<String,Object> map);
	
	
	
	
	
	/**
	 * 层级获取行政区域树列表
	 * @return
	 */
	List<CameraPermissionVo> findProjectTree();
	
	
	
	
	/**
	 * 层级获取地址列表
	 * @param map 查询集合
	 * @return
	 */
	List<CameraPermissionVo> findChildAddress(Map<String,Object> map);

	/**
	 * 层级获取行政区域列表
	 * @param map 查询集合
	 * @return
	 */
	List<CameraPermissionVo> findChildOrganization(Map<String,Object> map);
	
	
	
	/**
	 * 层级获取自定义项目分组列表
	 * @param map 查询集合
	 * @return
	 */
	List<CameraPermissionVo> findChildCameraGroup(Map<String,Object> map);
	
	/**
	 * 获取项目分组顶级部门列表【根据项目去获取其顶级部门】
	 * @param map
	 * @return
	 */
	List<CameraPermissionVo> findTopDept(Map<String,String> map);
	
	/**
	 * 根据部门pid获取下级部门列表和项目列表
	 * @param map 查询集合
	 * @return
	 */
	List<CameraPermissionVo> findChildProjectOrDept(Map<String,Object> map);
	
	
	
	


}
