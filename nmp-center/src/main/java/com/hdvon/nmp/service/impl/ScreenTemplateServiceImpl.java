package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdvon.nmp.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.entity.ScreenTemplate;
import com.hdvon.nmp.entity.ScreenTemplateCellinfo;
import com.hdvon.nmp.mapper.ScreenTemplateCellinfoMapper;
import com.hdvon.nmp.mapper.ScreenTemplateMapper;
import com.hdvon.nmp.service.IScreenTemplateService;
import com.hdvon.nmp.util.BeanHelper;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.ProjectVo;
import com.hdvon.nmp.vo.ScreenTemplateCellinfoVo;
import com.hdvon.nmp.vo.ScreenTemplateVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>分屏管理模板Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ScreenTemplateServiceImpl implements IScreenTemplateService {

	@Autowired
	private ScreenTemplateMapper screenTemplateMapper;
	
	@Autowired
	private ScreenTemplateCellinfoMapper screenTemplateCellinfoMapper;
	

	@Override
	public void saveScreenTemplate(UserVo userVo, ScreenTemplateVo templateVo){
		 ScreenTemplate screenTemplate = Convert.convert(ScreenTemplate.class,templateVo);
		 List<ScreenTemplateCellinfoVo> cellinfoVos =  templateVo.getCellinfos();
		 List<ScreenTemplateCellinfo> cellinfos = new ArrayList<ScreenTemplateCellinfo>();
		 for(ScreenTemplateCellinfoVo cellinfoVo : cellinfoVos) {
			 ScreenTemplateCellinfo cellinfo = Convert.convert(ScreenTemplateCellinfo.class,cellinfoVo);
			 cellinfos.add(cellinfo);
		 }
		 if(StrUtil.isNotBlank(templateVo.getId())) {//编辑项目
			 Example ste = new Example(ScreenTemplate.class);
	         ste.createCriteria().andEqualTo("name", templateVo.getName()).andNotEqualTo("id", templateVo.getId());
	         int countName = screenTemplateMapper.selectCountByExample(ste);
	         if(countName > 0) {
        		throw new ServiceException("模板名称已经存在！");
	         }
	         
			 screenTemplate.setUpdateTime(new Date());
			 screenTemplate.setUpdateUser(userVo.getAccount());

			 screenTemplateMapper.updateByPrimaryKeySelective(screenTemplate);
			 
			 //删除模板中所有旧的单元格
			 Example oldInfoStce = new Example(ScreenTemplateCellinfo.class);
			 oldInfoStce.createCriteria().andEqualTo("templateId",screenTemplate.getId());
			 screenTemplateCellinfoMapper.deleteByExample(oldInfoStce);
	         
	         //新增新的cellinfos
	         for(ScreenTemplateCellinfo cellinfo : cellinfos) {
	        	 cellinfo.setId(IdGenerator.nextId());
	        	 cellinfo.setTemplateId(screenTemplate.getId());
	        	 cellinfo.setCreateTime(new Date());
	        	 cellinfo.setCreateUser(userVo.getAccount());
	        	 cellinfo.setUpdateTime(new Date());
	        	 cellinfo.setUpdateUser(userVo.getAccount());
	         }
	         
	         screenTemplateCellinfoMapper.insertList(cellinfos);
        }else{//新增分屏模板
        	Example ste = new Example(ScreenTemplate.class);
        	ste.createCriteria().andEqualTo("name", templateVo.getName());
        	int countName = screenTemplateMapper.selectCountByExample(ste);
        	if(countName > 0) {
        		throw new ServiceException("模板名称已经存在！");
        	}
        	
        	screenTemplate.setId(IdGenerator.nextId());
        	Date date = new Date();
        	screenTemplate.setCreateTime(date);
        	screenTemplate.setCreateUser(userVo.getAccount());
        	screenTemplate.setUpdateTime(date);
        	screenTemplate.setUpdateUser(userVo.getAccount());
        	screenTemplateMapper.insertSelective(screenTemplate);
        	
        	for(ScreenTemplateCellinfo cellinfo : cellinfos) {
        		cellinfo.setId(IdGenerator.nextId());
        		cellinfo.setTemplateId(screenTemplate.getId());
        		cellinfo.setCreateTime(new Date());
        		cellinfo.setCreateUser(userVo.getAccount());
        		cellinfo.setUpdateTime(new Date());
        		cellinfo.setUpdateUser(userVo.getAccount());
        	}
        	
        	//screenTemplateCellinfoMapper.insertBatch(cellinfos);
        	screenTemplateCellinfoMapper.insertList(cellinfos);
        }
	}

	@Override
	public PageInfo<ScreenTemplateVo> getScreenTemplatePages(PageParam pp, Map<String,String> map) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		/*Example ste = new Example(ScreenTemplate.class);
		String search = map.get("templateName");
		if(StrUtil.isNotBlank(search)) {
			ste.createCriteria().andLike("name", "%"+search+"%");
		}else {
			ste.createCriteria();
		}
    	List<ScreenTemplate> list = screenTemplateMapper.selectByExample(ste);
    	
    	List<ScreenTemplateVo> screenTemplateVos = BeanHelper.convertList(ScreenTemplateVo.class, list);*/
		List<ScreenTemplateVo> screenTemplateVos =screenTemplateMapper.selectByParam(map);
    	return new PageInfo<ScreenTemplateVo>(screenTemplateVos);
	}

	@Override
	public List<ScreenTemplateVo> getScreenTemplateList(Map<String,String> map) {
		/*Example ste = new Example(ScreenTemplate.class);
		String search = map.get("templateName");
		if(StrUtil.isNotBlank(search)) {
			ste.createCriteria().andLike("name", "%"+search+"%");
		}else {
			ste.createCriteria();
		}
    	List<ScreenTemplate> list = screenTemplateMapper.selectByExample(ste);
    	
    	List<ScreenTemplateVo> screenTemplateVos = BeanHelper.convertList(ScreenTemplateVo.class, list); */
		List<ScreenTemplateVo> screenTemplateVos =screenTemplateMapper.selectByParam(map);
    	return screenTemplateVos;
	}

	@Override
	public void delScreenTemplatesByIds(List<String> ids) {
		Example ste = new Example(ScreenTemplate.class);
		ste.createCriteria().andIn("id", ids);
		screenTemplateMapper.deleteByExample(ste);
		
		//同时删除模板下的cell列表
		Example stce = new Example(ScreenTemplateCellinfo.class);
		stce.createCriteria().andIn("templateId", ids);
		screenTemplateCellinfoMapper.deleteByExample(stce);
		
	}

	@Override
	public ScreenTemplateVo getScreenTemplateById(String id) {
		ScreenTemplate screenTemplate = screenTemplateMapper.selectByPrimaryKey(id);
		ScreenTemplateVo screenTemplateVo = Convert.convert(ScreenTemplateVo.class,screenTemplate);
		
		/*Example stce = new Example(ScreenTemplateCellinfo.class);
		List<ScreenTemplateCellinfo> cellinfos =  screenTemplateCellinfoMapper.selectByExample(stce);
		
		List<ScreenTemplateCellinfoVo> cellinfoVos = BeanHelper.convertList(ScreenTemplateCellinfoVo.class, cellinfos);
		screenTemplateVo.setCellinfos(cellinfoVos);
		*/
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("templateId", screenTemplate.getId());
		List<ScreenTemplateCellinfoVo> cellinfoVos =screenTemplateCellinfoMapper.selectByParam(param);
		screenTemplateVo.setCellinfos(cellinfoVos);
		return screenTemplateVo;
	}

}
