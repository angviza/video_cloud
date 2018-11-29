package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdvon.nmp.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.MatrixChannel;
import com.hdvon.nmp.mapper.MatrixChannelMapper;
import com.hdvon.nmp.service.IMatrixChannelService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.MatrixChannelParamVo;
import com.hdvon.nmp.vo.MatrixChannelVo;
import com.hdvon.nmp.vo.MatrixVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>数字矩阵通道Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class MatrixChannelServiceImpl implements IMatrixChannelService {

	@Autowired
	private MatrixChannelMapper matrixChannelMapper;

	@Override
	public void saveMatrixChannel(UserVo userVo, MatrixChannelParamVo matrixChannelParamVo) {
		MatrixChannel matrixChannel = Convert.convert(MatrixChannel.class, matrixChannelParamVo);
		
		if(StrUtil.isNotBlank(matrixChannelParamVo.getId())) {
			Example mce = new Example(MatrixChannel.class);
			mce.createCriteria().andEqualTo("name", matrixChannelParamVo.getName()).andNotEqualTo("id", matrixChannelParamVo.getId());
			int countName = matrixChannelMapper.selectCountByExample(mce);
			if(countName > 0) {
				throw new ServiceException("矩阵通道名称已经存在！");
			}
			mce.clear();
			mce.createCriteria().andEqualTo("devicesNo", matrixChannelParamVo.getDevicesNo()).andNotEqualTo("id", matrixChannelParamVo.getId());
			int countDevice = matrixChannelMapper.selectCountByExample(mce);
			if(countDevice > 0) {
				throw new ServiceException("矩阵通道编码已经存在！");
			}
			
			matrixChannel.setUpdateTime(new Date());
			matrixChannel.setUpdateUser(userVo.getAccount());
			
			matrixChannelMapper.updateByPrimaryKeySelective(matrixChannel);
		}else {
			Example mce = new Example(MatrixChannel.class);
			mce.createCriteria().andEqualTo("name", matrixChannelParamVo.getName());
			int countName = matrixChannelMapper.selectCountByExample(mce);
			if(countName > 0) {
				throw new ServiceException("矩阵通道名称已经存在！");
			}
			mce.clear();
			
			mce.createCriteria().andEqualTo("devicesNo", matrixChannelParamVo.getDevicesNo());
			int countDevice = matrixChannelMapper.selectCountByExample(mce);
			if(countDevice > 0) {
				throw new ServiceException("矩阵通道编码已经存在！");
			}
			
			matrixChannel.setId(IdGenerator.nextId());
			Date date = new Date();
			matrixChannel.setCreateTime(date);
			matrixChannel.setCreateUser(userVo.getAccount());
			matrixChannel.setUpdateTime(date);
			matrixChannel.setUpdateUser(userVo.getAccount());
			
			matrixChannelMapper.insertSelective(matrixChannel);
		}
		
	}

	@Override
	public PageInfo<MatrixChannelVo> getMatrixChannelPages(PageParam pp, Map<String,Object> map) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
    	List<MatrixChannelVo> list = matrixChannelMapper.selectMatrixChannelList(map);
    	return new PageInfo<MatrixChannelVo>(list);
	}

	@Override
	public List<MatrixChannelVo> getMatrixChannelList(String id, String search) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("matrixId", id);
		 paramMap.put("search", search);
		List<MatrixChannelVo> list = matrixChannelMapper.selectMatrixChannelList(paramMap);
    	return list;
	}

	@Override
	public void delMatrixChannelsByIds(List<String> ids) {
		Example mce = new Example(MatrixChannel.class);
		mce.createCriteria().andIn("id", ids);
		
		matrixChannelMapper.deleteByExample(mce);
	}

	@Override
	public MatrixChannelVo getMatrixChannelById(String id) {
		MatrixChannelVo matrixChannelVo = matrixChannelMapper.selectMatrixChannelById(id);
		return matrixChannelVo;
	}

	@Override
	public List<MatrixChannelVo> getMatrixChannelByChannelNos(String matrixId, List<String> channelNos) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("matrixId", matrixId);
		map.put("channelNos", channelNos);
		List<MatrixChannelVo> matrixChannelVos = matrixChannelMapper.selectMatrixChannelList(map);
		return matrixChannelVos;
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		
		return matrixChannelMapper.selectMaxCodeBycode(map);
	}
}
