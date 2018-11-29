package com.hdvon.client.service;

import java.util.List;
import java.util.Map;

import com.hdvon.client.enums.AggFieldEnum;
import com.hdvon.client.exception.CameraException;
import com.hdvon.client.exception.UserException;
import com.hdvon.client.form.CameraForm;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.client.vo.CameraVo;
import com.hdvon.client.vo.CheckCameraVo;
import com.hdvon.client.vo.EsCameraVo;
import com.hdvon.client.vo.EsPage;
import com.hdvon.client.vo.PointVo;
/**
 * <br>
 * <b>功能：</b>摄像机服务接口<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-5-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICameraService {

	
	
	/**
	 * 获取当前地址节点的拥有的摄像机资源
	 * @param userId 用户id
	 * @param pid 父节点id
	 * @param isAdmin 是否管理员
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getAddressCameraPermissionTree(String userId,String pid,Integer isAdmin) throws CameraException;
	
	

	
	/**
	 * 获取当前行政节点的拥有的摄像机资源
	 * @param userId 用户id
	 * @param pid 父节点id
	 * @param isAdmin 是否管理员
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getOrganizationCameraPermission(String userId,String pid,Integer isAdmin) throws CameraException;
	

	
	/**
	 * 获取项目分组节点的拥有的摄像机资源
	 * @param userId 用户id
	 * @param pid 父节点id
	 * @param isAdmin 是否管理员
	 * @param isProject 是否项目 1:是
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getProjectCameraPermission(String userId,String pid,Integer isAdmin,Integer isProject) throws CameraException;
	

	
	/**
	 * 获取当前自定义分组节点的拥有的摄像机资源
	 * @param userId 用户id
	 * @param account 用户账号
	 * @param pid 父节点id
	 * @param isAdmin 是否管理员
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getCameraGroupPermission(String userId,String account,String pid,Integer isAdmin) throws CameraException;
	
	
	/**
	 * 获取当前角色的拥有的摄像机资源
	 * @param roleid 角色id
	 * @param isAdmin 是否管理员
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getRoleCameraPermissionTree(String roleid,Integer isAdmin) throws CameraException;
	
	/**
	 * 获取热点摄像机
	 * @param pageSize 返回条数
	 * @return
	 * @throws CameraException
	 */
	List<CameraPermissionVo> getHotsCamera(int pageSize) throws CameraException;
	
	
	/**
	 * 获取热点摄像机
	 * @param account 用户账号
	 * @return
	 * @throws CameraException
	 */
	List<CameraPermissionVo> getCollectCameraList(String account) throws CameraException;
	
	/**
	 * 根据范围获取附近的摄像机列表
	 * @param form 查询对象
     * @param latitude 纬度
     * @param longitude 经度
     * @param distance 搜索半径（距离 米）
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @return
	 */
	EsPage<EsCameraVo> getDistanceRangeCameraList(CameraForm form,double latitude, double longitude,int distance,int currentPage,int pageSize)throws UserException;
	
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
	EsPage<EsCameraVo> getBoundingBoxCameraList(CameraForm form,double topleft_lat, double topleft_lon,double  bottomRight_lat,double bottomRight_lon,int currentPage,int pageSize)throws UserException;
	
	
	/**
	 * 根据选定多边形获取摄像机列表
	 * @param form 查询对象
	 * @param points 地址列表
	 * @return
	 */
	EsPage<EsCameraVo> getPolygonCameraList(CameraForm form,List<PointVo> points,int currentPage,int pageSize)throws UserException;

	/**
	 * 根据条件获取摄像机列表【查询ES】
	 * @param form 查询对象
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @return
	 */
	EsPage<EsCameraVo> searchCameraList(CameraForm form,int currentPage,int pageSize)throws UserException;
	/**
	 * 根据条件获取摄像机列表【查询ES】
	 * @param form 查询对象
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @param fields 返回字段 ,多个已“,”分割  null 返回所有字段 [参考EsCameraVo字段名称] 
	 * @param sortField 排序字段 [参考EsCameraVo字段名称]  
	 * @return
	 */
	EsPage<EsCameraVo> searchCameraList(CameraForm form,int currentPage,int pageSize,String fields,String sortField)throws UserException;
	
	/**
	 * es高级搜索
	 * @param form 查询对象
	 * @param fields 返回字段 ,多个已“,”分割  null 返回所有字段 [参考EsCameraVo字段名称] 
	 * @param aggEm 分组字段枚举类型
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo>  searchHighCameraList(CameraForm form,String fields,AggFieldEnum aggEm)throws UserException;
	
	

	
	/**
	 * es普通搜索【整合权限预案】
	 * @param queryStr 查询字段
	 * @param userId 用户id
	 * @param fields 返回字段 ,多个已“,”分割  null 返回所有字段 [参考EsCameraVo字段名称] 
	 * @param aggEm 分组字段枚举类型
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo>  searchNomalCameraList(String queryStrsss,String userId,String fields,AggFieldEnum aggEm)throws UserException;
	/**
	 * 根据设备id获取摄像机详情
	 * @param deviceId 设备id
	 * @return
	 */
	CameraVo findCameraRecord(String deviceId); 
	

	/**
	 * 用户摄像机权限验证
	 * @param userId 用户id
	 * @return
	 */
	List<CheckCameraVo> checkUserCameraRights(String userId);
	
	
	/**
	 * 用户摄像机权限验证
	 * @param userId 用户id
	 * @param deviceIds 设备id集合
	 * @param type 类型 1： ids为camearId  2:为deviceId
	 * @return
	 */
	Map<String,Boolean> checkUserCameraRights(String userId, List<String> ids,int  type);
}
