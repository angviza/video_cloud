package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.DepartmentMatrix;
import com.hdvon.nmp.entity.Matrix;
import com.hdvon.nmp.entity.MatrixChannel;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IMatrixService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>矩阵服务器表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class MatrixServiceImpl implements IMatrixService {

	@Autowired
	private MatrixMapper matrixMapper;
	
	@Autowired
	private MatrixChannelMapper matrixChannelMapper;
	
	@Autowired
	private DepartmentMatrixMapper departmentMatrixMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private TreeNodeMapper treeNodeMapper;

	@Override
	public void saveMatrix(UserVo userVo, MatrixVo matrixVo){
		Matrix matrix = Convert.convert(Matrix.class,matrixVo);
		Example meName = new Example(Matrix.class);

		if(userVo.getDepartmentId() != null && matrixVo.getDepartmentId() != null && (matrixVo.getDepartmentId()).equals(userVo.getDepartmentId())) {
			if (StrUtil.isNotBlank(matrixVo.getId())) {
				meName.clear();
				meName.createCriteria().andEqualTo("name", matrixVo.getName()).andNotEqualTo("id", matrixVo.getId());
				int countName = matrixMapper.selectCountByExample(meName);
				if (countName > 0) {
					throw new ServiceException("矩阵服务器名称已经存在！");
				}

				meName.clear();
				meName.createCriteria().andEqualTo("devicesNo", matrixVo.getDevicesNo()).andNotEqualTo("id", matrixVo.getId());
				int countDevice = matrixMapper.selectCountByExample(meName);
				if (countDevice > 0) {
					throw new ServiceException("矩阵服务器编号已经存在！");
				}
				matrix.setUpdateTime(new Date());
				matrix.setUpdateUser(userVo.getAccount());
				matrixMapper.updateByPrimaryKeySelective(matrix);

				DepartmentMatrix dm = new DepartmentMatrix();
				dm.setDepartmentId(matrixVo.getDepartmentId());
				Example dme = new Example(DepartmentMatrix.class);
				dme.createCriteria().andEqualTo("matrixId", matrix.getId());
				departmentMatrixMapper.updateByExampleSelective(dm, dme);
			} else {
				meName.clear();
				meName.createCriteria().andEqualTo("name", matrixVo.getName());
				int countName = matrixMapper.selectCountByExample(meName);
				if (countName > 0) {
					throw new ServiceException("矩阵服务器名称已经存在！");
				}

				meName.clear();
				meName.createCriteria().andEqualTo("devicesNo", matrixVo.getDevicesNo());
				int countDevice = matrixMapper.selectCountByExample(meName);
				if (countDevice > 0) {
					throw new ServiceException("矩阵服务器编号已经存在！");
				}

				matrix.setId(IdGenerator.nextId());
				Date date = new Date();
				matrix.setCreateTime(date);
				matrix.setCreateUser(userVo.getAccount());
				matrix.setUpdateTime(date);
				matrix.setUpdateUser(userVo.getAccount());
				matrixMapper.insertSelective(matrix);

				DepartmentMatrix dm = new DepartmentMatrix();
				dm.setId(IdGenerator.nextId());
				dm.setMatrixId(matrix.getId());
				dm.setDepartmentId(matrixVo.getDepartmentId());
				departmentMatrixMapper.insertSelective(dm);
			}
		}else{
			throw new ServiceException("用户只能选择自己的本部门信息！");
		}
	}

	@Override
	public PageInfo<MatrixVo> getMatrixPages(PageParam pp, TreeNodeChildren treeNodeChildren, MatrixParamVo matrixParmVo) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("name", matrixParmVo.getName());
        paramMap.put("deviceNo", matrixParmVo.getDeviceNo());
        paramMap.put("registerUser", matrixParmVo.getRegisterUser());
        paramMap.put("channelName", matrixParmVo.getChannelName());
        paramMap.put("deptIds", treeNodeChildren.getDeptNodeIds());
        
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
    	List<MatrixVo> list = matrixMapper.selectMatrixPage(paramMap);
    	return new PageInfo<MatrixVo>(list);
	}

	@Override
	public List<MatrixVo> getMatrixList(Map<String,Object> map) {
		List<MatrixVo> list = matrixMapper.selectMatrixList(map);
    	return list;
	}

	@Override
	public void delMatrixsByIds(UserVo userVo,List<String> ids){
		// 检验是否是是本部门矩阵
		for(String id :ids){
			MatrixVo matrixVo = matrixMapper.selectMatrixDetailById(id);
			if(matrixVo.getDepartmentId() != null && !(matrixVo.getDepartmentId()).equals(userVo.getDepartmentId())){
				throw new ServiceException("只能删除本部门矩阵信息！");
			}
		}

		Example meL = new Example(MatrixChannel.class);
		meL.createCriteria().andIn("matrixId", ids);
		int count = matrixChannelMapper.selectCountByExample(meL);
		if(count>0) {
			throw new ServiceException("矩阵下有通道，不能删除！");
		}
		Example me = new Example(Matrix.class);
		me.createCriteria().andIn("id", ids);
		matrixMapper.deleteByExample(me);
		
		//删除矩阵与部门的关联
		Example dme = new Example(DepartmentMatrix.class);
		dme.createCriteria().andIn("matrixId", ids);
		departmentMatrixMapper.deleteByExample(dme);
		
	}

	@Override
	public MatrixVo getMatrixById(String id) {
		MatrixVo matrixVo = matrixMapper.selectMatrixDetailById(id);
		return matrixVo;
	}

	@Override
	public MatrixVo getMatrixById(UserVo userVo,String id) {
		String userDepartId = userVo.getDepartmentId();
		MatrixVo matrixVo = matrixMapper.selectMatrixDetailById(id);
		// 查找用户部门及子部门
		Map<String,Object> param = new HashMap<>();
		param.put("code", userVo.getDepCodeSplit());
		param.put("type", TreeType.DEPARTMENT.getVal());
		List<TreeNodeDepartment> list = treeNodeMapper.selectDeptChildNodesByCode(param);

		if(list !=null && matrixVo.getDepartmentId() !=null){
			List<String> templist = new ArrayList<>();
			for(TreeNodeDepartment item : list){
				if(!(matrixVo.getDepartmentId().trim()).equals(item.getId().trim())){
					templist.add(item.getId());
				}
			}
			if(list.size() == templist.size()){
				throw new ServiceException("非本部门用户不能查看本部及子部门矩阵信息！");
			}
		}else{
			return null;
		}
		return matrixVo;
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		
		return matrixMapper.selectMaxCodeBycode(map);
	}
}
