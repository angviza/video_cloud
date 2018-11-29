package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.CameraMapping;
import com.hdvon.nmp.entity.EncoderServer;
import com.hdvon.nmp.entity.EncodeserverMapping;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IEncoderServerService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.EncoderServerVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>编码器表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class EncoderServerServiceImpl implements IEncoderServerService {

	@Autowired
	private EncoderServerMapper encoderServerMapper;

	@Autowired
	private CameraMapper cameraMapper;

	@Autowired
	private CameraMappingMapper cameraMappingMapper;

	@Autowired
	private EncodeserverMappingMapper encodeserverMappingMapper;
	
	@Autowired
	private ProjectMapper projectMapper;

	@Override
	public void editEncoderServer(UserVo userVo, EncoderServerVo encoderVo){
		encoderVo.convertProjectId();
		EncoderServer encoderServer = Convert.convert(EncoderServer.class,encoderVo);
		
		if(StrUtil.isNotBlank(encoderServer.getId())) {
			EncoderServer encoderServerVo = encoderServerMapper.selectByPrimaryKey(encoderServer.getId());
			if(encoderServerVo == null) {
				throw new ServiceException("编码器不存在，请刷新页面！");
			}
			String projectId = encoderVo.getProjectId();
			Project project = projectMapper.selectByPrimaryKey(projectId);
			if(project == null) {
				throw new ServiceException("没有正确选择所属项目！");
			}
			
			encoderServer.setUpdateTime(new Date());
			encoderServer.setUpdateUser(userVo.getAccount());

			encoderServer.setName(encoderVo.getName().trim());

			encoderServerMapper.updateByPrimaryKeySelective(encoderServer);
			
			Example eme = new Example(EncodeserverMapping.class);
			eme.createCriteria().andEqualTo("encodeserverId", encoderVo.getId());
			EncodeserverMapping em = new EncodeserverMapping();
			em.setEncodeserverId(encoderVo.getId());
			em.setAddressId(encoderVo.getAddressId());
			em.setProjectId(encoderVo.getProjectId());
			encodeserverMappingMapper.updateByExampleSelective(em, eme);
		}else {
			Example encoder = new Example(EncoderServer.class);
			encoder.createCriteria().andEqualTo("name",encoderVo.getName());
			List<EncoderServer> temp = encoderServerMapper.selectByExample(encoder);
			if(temp.size() >0 ){
				throw new ServiceException("编码器名称重复，请重新输入！");
			}

			String projectId = encoderVo.getProjectId();
			Project project = projectMapper.selectByPrimaryKey(projectId);
			if(project == null) {
				throw new ServiceException("没有正确选择所属项目！");
			}
			
			encoderServer.setId(IdGenerator.nextId());
			Date date = new Date();
			encoderServer.setCreateTime(date);
			encoderServer.setCreateUser(userVo.getAccount());
			encoderServer.setUpdateUser(userVo.getAccount());
			encoderServer.setUpdateTime(date);
			encoderServer.setName(encoderVo.getName().trim());
			encoderServerMapper.insertSelective(encoderServer);

			EncodeserverMapping esm = new EncodeserverMapping();
			esm.setId(IdGenerator.nextId());
			esm.setEncodeserverId(encoderServer.getId());
			esm.setAddressId(encoderVo.getAddressId());
			esm.setProjectId(encoderVo.getProjectId());
			encodeserverMappingMapper.insertSelective(esm);
		}
	}

	@Override
	public void delEncoderServers(List<String> ids) {
		Example de = new Example(EncoderServer.class);
		de.createCriteria().andIn("id", ids);
		encoderServerMapper.deleteByExample(de);//删除编码器

		Example em = new Example(EncodeserverMapping.class);
		em.createCriteria().andIn("encodeserverId", ids);
		encodeserverMappingMapper.deleteByExample(em);//删除编码器的关联
	}

	@Override
	public EncoderServerVo getEncoderServerById(String id) {
	    Map<String,Object> map = new HashMap<>();
	    //避免前端传null值，后台全表查询
	    if(id == null){
	        id = "";
        }
	    map.put("id",id);
		List<EncoderServerVo> list = encoderServerMapper.selectEncoderServerList(map);
		if(list.size() > 0){
		    return list.get(0);
        }else{
		    return null;
        }
	}

	@Override
	public PageInfo<EncoderServerVo> getEncoderServerPage(PageParam pp, EncoderServerVo encoderServerVo, TreeNodeChildren treeNodeChildren) {
        PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("addrIds", treeNodeChildren.getAddressNodeIds());
		paramMap.put("projectIds", treeNodeChildren.getProjectNodeIds());
		paramMap.put("search", encoderServerVo.getName());
        List<EncoderServerVo> list = encoderServerMapper.selectEncoderServerList(paramMap);
        return new PageInfo<EncoderServerVo>(list);
	}

	@Override
	public PageInfo<CameraVo> getCamerasByEncodeId(PageParam pp, String encodeId) {
		 PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        List<CameraVo> list = cameraMapper.queryCamerasByEncodeId(encodeId);
        return new PageInfo<CameraVo>(list);
	}

	@Override
	public void delCamerasReferEncoder(String encodeId, List<String> cameraIds) {
		Example cme = new Example(CameraMapping.class);
		cme.createCriteria().andEqualTo("encoderServerId", encodeId).andIn("cameraId", cameraIds);
		cameraMappingMapper.deleteByExample(cme);
	}

	@Override
	public PageInfo<EncoderServerVo> getEncoderServerPageByAddrId(PageParam pp, String addressId) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> map = new HashMap<>();
        //避免前端传null值，后台全表查询
        if(addressId == null){
            addressId = "";
        }
        map.put("addrId",addressId);
        List<EncoderServerVo> list = encoderServerMapper.selectEncoderServerList(map);
		return new PageInfo<EncoderServerVo>(list);
	}

	@Override
	public List<EncoderServerVo> getEncoderServerList(EncoderServerVo encoderServerVo, TreeNodeChildren treeNodeChildren) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("addrIds", treeNodeChildren.getAddressNodeIds());
		paramMap.put("projectIds", treeNodeChildren.getProjectNodeIds());
		paramMap.put("search", encoderServerVo.getName());
		 List<EncoderServerVo> list = encoderServerMapper.selectEncoderServerList(paramMap);
         return list;
	}

//	@Override
//	public List<.> getAddressEncoderServerTree(Map<String, Object> map) {
//		List<CombinTreeVo> list = encoderServerMapper.selectAddressEncoderServerTree(map);
//		return list;
//	}

	@Override
	public String getMaxCodeByParam(Map<String, Object> map) {
		return encoderServerMapper.selectMaxCodeByParam(map);
	}
	
}
