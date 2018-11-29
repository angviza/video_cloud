package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.MatrixParamVo;
import com.hdvon.nmp.vo.MatrixVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>矩阵服务器表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IMatrixService{
    /**
     * 保存矩阵服务器信息
     * @param userVo
     * @param matrixVo
     */
    void saveMatrix(UserVo userVo, MatrixVo matrixVo);
    /**
     * 分页查询矩阵服务器列表
     * @param pp
     * @param search
     * @return
     */
    PageInfo<MatrixVo> getMatrixPages(PageParam pp, TreeNodeChildren treeNodeChildren, MatrixParamVo matrixParmVo);
    /**
     * 查询矩阵服务器列表
     * @param map
     * @return
     */
    List<MatrixVo> getMatrixList(Map<String,Object> map);
    /**
     * 批量删除矩阵服务器
     * @param userVo
     * @param ids
     */
    void delMatrixsByIds(UserVo userVo,List<String> ids);
    /**
     * 根据id查询单个矩阵服务器信息
     * @param id
     * @return
     */
    MatrixVo getMatrixById(String id);
    /**
     * 根据id及userVo查询单个矩阵服务器信息
     * @param id
     * @return
     */
    MatrixVo getMatrixById(UserVo userVo,String id);
    /**
     * 获取最大编码
     * @param map
     * @return
     */
	String getMaxCodeBycode(Map<String, Object> map);
}
