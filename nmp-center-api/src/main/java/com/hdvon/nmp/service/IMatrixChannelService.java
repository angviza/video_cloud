package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.MatrixChannelParamVo;
import com.hdvon.nmp.vo.MatrixChannelVo;
import com.hdvon.nmp.vo.MatrixVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>数字矩阵通道 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IMatrixChannelService{
    /**
     * 保存矩阵通道信息
     * @param userVo
     * @param matrixChannel
     */
    void saveMatrixChannel(UserVo userVo, MatrixChannelParamVo matrixChannelParam);
    /**
     * 分页查询矩阵通道列表
     * @param pp
     * @param search
     * @return
     */
    PageInfo<MatrixChannelVo> getMatrixChannelPages(PageParam pp, Map<String,Object> map);
    /**
     * 查询矩阵通道列表
     * @param search
     * @return
     */
    List<MatrixChannelVo> getMatrixChannelList(String id, String search);
    /**
     * 批量删除矩阵通道
     * @param ids
     */
    void delMatrixChannelsByIds(List<String> ids);
    /**
     * 根据id查询单个矩阵通道信息
     * @param id
     * @return
     */
    MatrixChannelVo getMatrixChannelById(String id);
    
    /**
     * 根据矩阵id、通道编号列表查询矩阵下的通道
     * @param matrixId
     * @param channelNos
     * @return
     */
    List<MatrixChannelVo> getMatrixChannelByChannelNos(String matrixId, List<String> channelNos);
    /**
     * 获取最大编码
     * @param map
     * @return
     */
	String getMaxCodeBycode(Map<String, Object> map);
}
