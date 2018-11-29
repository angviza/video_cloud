package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.EncoderServerVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>编码器表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IEncoderServerService{

	/**
	 * 增加或者编辑编码器信息提交
	 * @param encoderVo 编码器vo
	 */
	void editEncoderServer(UserVo userVo, EncoderServerVo encoderVo);

	/**
	 * 根据主键集合批量删除编码器信息
	 * @param ids 编码器主键id集合
	 */
	void delEncoderServers(List<String> ids);
	/**
	 * 根据主键id查询编码器信息
	 * @param id 编码器主键id
	 * @return
	 */
	EncoderServerVo getEncoderServerById(String id);
	/**
	 * 分页查询条件分页查询编码器信息
	 * @param pp
	 * @param encoderServerVo
	 * @param treeNodeChildren
	 * @return
	 */
	PageInfo<EncoderServerVo> getEncoderServerPage(PageParam pp, EncoderServerVo encoderServerVo, TreeNodeChildren treeNodeChildren);
	/**
	 * 查询编码器列表
	 * @param paramMap
	 * @return
	 */
	/**
	 * @param encoderServerVo
	 * @return
	 */
	List<EncoderServerVo> getEncoderServerList(EncoderServerVo encoderServerVo, TreeNodeChildren treeNodeChildren);
	/**
	 * 分页查询关联到编码器的所有摄像机
	 * @param pp	分页参数
	 * @param encodeId 编码器id
	 * @return
	 */
	PageInfo<CameraVo> getCamerasByEncodeId(PageParam pp, String encodeId);

	/**
	 * 批量删除关联编码器的摄像机
	 * @param encodeId	关联的编码器id
	 * @param cameraIds 选中要删除的摄像机id集合
	 */
	void delCamerasReferEncoder(String encodeId, List<String> cameraIds);
	
	/**
	 * 根据地址id分页查询
	 * @param addressId
	 * @return
	 */
	PageInfo<EncoderServerVo> getEncoderServerPageByAddrId(PageParam pp, String addressId);
	
//	List<CombinTreeVo> getAddressEncoderServerTree(Map<String,Object> map);

	/**
	 * 获取编码器最大编码
	 * @param map
	 * @return
	 */
	String getMaxCodeByParam(Map<String, Object> map);
}
