package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.CameraMapping;
import com.hdvon.nmp.entity.Organization;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.CameraMappingMapper;
import com.hdvon.nmp.mapper.OrganizationMapper;
import com.hdvon.nmp.mapper.TreeNodeMapper;
import com.hdvon.nmp.service.IOrganizationService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.OrganizationVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <br>
 * <b>功能：</b>组织机构表/行政区划(国标)Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class OrganizationServiceImpl implements IOrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;
    /*@Autowired
    private UserMapper userMapper;*/
   /* @Autowired
    private UserOrgnizationMapper userOrganizationMapper;*/
    @Autowired
    private CameraMappingMapper cameraMappingMapper;
    
    @Autowired
	private TreeCodeService treeCodeService;
	
	@Autowired
    private TreeNodeMapper treeNodeMapper;

	@Autowired
    private GenerateTreeService generateTreeService;
	
    @Override
    public void addOrganization(UserVo userVo, OrganizationVo organizationVo){
    	Organization oraOrganization = Convert.convert(Organization.class,organizationVo);
    	
        if(StrUtil.isNotBlank(organizationVo.getId())) {//编辑组织机构
        	Example example = new Example(Organization.class);
    		example.createCriteria().andEqualTo("name", organizationVo.getName()).andNotEqualTo("id", organizationVo.getId());
    		int countName = organizationMapper.selectCountByExample(example);
    		if(countName>0) {
    			throw new ServiceException("组织机构名称已经存在，请修改！");
    		}
    		Example exampleCode = new Example(Organization.class);
    		exampleCode.createCriteria().andEqualTo("orgCode", organizationVo.getOrgCode()).andNotEqualTo("id", organizationVo.getId());
    		int countCode = organizationMapper.selectCountByExample(exampleCode);
    		if(countCode > 0) {
    			throw new ServiceException("组织机构代码已经存在，请修改！");
    		}
    		OrganizationVo org = organizationMapper.selectOrganizationById(organizationVo.getId());
        	if(org==null) {
        		throw new ServiceException("用于编辑的组织机构不存在！");
        	}
        	if(!"0".equals(organizationVo.getPid())&&"0".equals(org.getPid())) {
        		throw new ServiceException("根节点不能修改父机构！");
        	}
        	if(organizationVo.getIsVirtual() != null && org.getIsVirtual() == 1) {
        		Organization parentOrg = organizationMapper.selectByPrimaryKey(oraOrganization.getPid());
        		if(parentOrg.getIsVirtual() == 1 && !parentOrg.getOrgCode().substring(11, 14).equals(oraOrganization.getOrgCode().substring(11, 14))) {
        			throw new ServiceException("虚拟组织编号错误，格式【1-10】【11-13】【14-20】，其中【11-13】同上级虚拟组织！");
        		}
        	}
        	if(organizationVo.getIsVirtual() != null && org.getIsVirtual() == 1 && org.getLevel() != null && org.getLevel() != 1 && StrUtil.isNotBlank(org.getBussGroupId()) && !org.getBussGroupId().equals(organizationVo.getBussGroupId())) {//当前虚拟组织节点是一级虚拟组织，可以修改业务分组，并且对应修改当前虚拟组织下面所有子虚拟组织的业务分组，否则是不能修改业务分组的
        		throw new ServiceException("不能修改"+org.getLevel()+"级虚拟组织的业务分组！");
        	}
        	if(StrUtil.isBlank(org.getBussGroupId())){//业务分组id为空，一定是组织机构
        		oraOrganization.setUpdateTime(new Date());
            	oraOrganization.setUpdateUser(userVo.getAccount());

            	oraOrganization.setName(organizationVo.getName().trim());
            	if(!oraOrganization.getPid().equals(org.getPid())) {//修改了上级组织机构
         	    	String oldCode = org.getOrgCodeSplit();//修改前的当前节点的code
         	    	
         	    	String newPid = oraOrganization.getPid();//修改后的新的父节点id
         	    	String newCode = treeCodeService.genSplitCode(newPid, oraOrganization.getOrgCode(), TreeType.ORG.getVal());//根据新的父节点的id生成当前节点新的code
         	    	
         	    	oraOrganization.setOrgCodeSplit(newCode);
         	    	
         	    	Map<String,Object> paramMap = new HashMap<String,Object>();
         	    	paramMap.put("oldCode", oldCode);
         	    	paramMap.put("newCode", newCode);
         	    	paramMap.put("type", TreeType.ORG.getVal());
         	    	treeNodeMapper.updateChildNodesCode(paramMap);//修改子节点编号
         	    }
            	organizationMapper.updateByPrimaryKeySelective(oraOrganization);
        	}else {
        		TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
    			List<TreeNode> treeNodes = treeCodeService.getOrgChildNodesByCode(oraOrganization.getOrgCodeSplit(), TreeType.ORG.getVal());
    			treeNodeChildren.setOrgNodes(treeNodes);
    			
    			Map<String,Object> map = new HashMap<String,Object>();
    			map.put("orgIds", treeNodeChildren.getOrgNodeIds());
    			List<OrganizationVo> orgs = organizationMapper.selectOrganizationList(map);//查询当前虚拟组织下的所有子节点
    	    	
        		if(organizationVo.getIsVirtual() != null && org.getIsVirtual() == 1) {
            		OrganizationVo newParentOrg = organizationMapper.selectOrganizationById(organizationVo.getPid());
            		Integer newParentlevel = newParentOrg.getLevel() == null ? 0 : newParentOrg.getLevel();//修改后的上级虚拟组织的层级
            		String newParentBussGroup = newParentOrg.getBussGroupId();//修改后的上级虚拟组织的业务分组
            		if(!org.getPid().equals(organizationVo.getPid())) {//修改上级机构，要校验层级，如果修改后的虚拟组织层级没有超过3级，就要重新设置层级，且要对应修改业务分组
                		int countMovelevels = getMoveLevels(orgs);//需要移动的虚拟组织层级数
                		if(newParentlevel+countMovelevels>3) {//校验修改上级组织后的虚拟组织层级是否超过3级
                			throw new ServiceException("虚拟组织最多只能有3级,当前虚拟组织不能修改到此上级组织下！");
                		}else {//没有超过3级，重新设置层级，修改业务分组
                			setLevelAndBussGroup(orgs, org.getId(), newParentlevel, newParentBussGroup);//根据修改后的上级机构，递归设置当前虚拟组织和当前虚拟组织下面子节点的层级和业务分组
                		}
            		}else if(!org.getBussGroupId().equals(organizationVo.getBussGroupId())) {//修改了业务分组,没有修改上级组织
            			setLevelAndBussGroup(orgs, org.getId(), newParentlevel, newParentBussGroup);//根据修改后的上级机构，递归设置当前虚拟组织和当前虚拟组织下面子节点的层级和业务分组
            		}
        		}
        		for(int i=0;i<orgs.size();i++) {
        			Organization org_enty = Convert.convert(Organization.class,orgs.get(i));
        			organizationMapper.updateByPrimaryKeySelective(org_enty);
        		}
        		
        		if(!oraOrganization.getPid().equals(org.getPid())) {//修改了上级组织
         	    	String oldCode = org.getOrgCodeSplit();//修改前的当前节点的code
         	    	
         	    	String newPid = oraOrganization.getPid();//修改后的新的父节点id
         	    	String newCode = treeCodeService.genSplitCode(newPid, oraOrganization.getOrgCode(), TreeType.ORG.getVal());//根据新的父节点的id生成当前节点新的code
         	    	
         	    	oraOrganization.setOrgCodeSplit(newCode);
         	    	
         	    	Map<String,Object> paramMap = new HashMap<String,Object>();
         	    	paramMap.put("oldCode", oldCode);
         	    	paramMap.put("newCode", newCode);
         	    	paramMap.put("type", TreeType.ORG.getVal());
         	    	treeNodeMapper.updateChildNodesCode(paramMap);//修改子节点编号
         	    }
        		organizationMapper.updateByPrimaryKeySelective(oraOrganization);
        	}
        	
        }else if(organizationVo.getPid() !=null && !"0".equals(organizationVo.getPid())){//增加子机构
        	Example example = new Example(Organization.class);
    		example.createCriteria().andEqualTo("name", organizationVo.getName());
    		int countName = organizationMapper.selectCountByExample(example);
    		if(countName>0) {
    			throw new ServiceException("组织机构名称已经存在，请修改！");
    		}
    		Example exampleCode = new Example(Organization.class);
    		exampleCode.createCriteria().andEqualTo("orgCode", organizationVo.getOrgCode());
    		int countCode = organizationMapper.selectCountByExample(exampleCode);
    		if(countCode > 0) {
    			throw new ServiceException("组织机构代码已经存在，请修改！");
    		}
    		OrganizationVo org = organizationMapper.selectOrganizationById(organizationVo.getPid());
    		if(org == null) {
    			throw new ServiceException("用于添加子机构的组织机构不存在！");
    		}
    		if(organizationVo.getIsVirtual() != null && organizationVo.getIsVirtual() == 1) {//虚拟组织
    			if(org.getIsVirtual() == null || org.getIsVirtual() != 1) {//上级组织不是虚拟组织，是行政区划，则当前创建的组织机构为第一级虚拟组织
    				oraOrganization.setLevel(1);
    			}else {//上级组织为虚拟组织，则当前创建的虚拟组织的层级为上级组织的层级+1
    				if(!org.getOrgCode().substring(11, 14).equals(oraOrganization.getOrgCode().substring(11, 14))) {//上级组织的虚拟组织编号的11到13位与子虚拟组织的组织编号11到13位要相同
            			throw new ServiceException("虚拟组织编号错误，格式【1-10】【11-13】【14-20】，其中【11-13】同上级虚拟组织！");
            		}
    				if(org.getLevel() == 3 ) {
    					throw new ServiceException("虚拟组织最多只能有3级！");
    				}else {
    					oraOrganization.setLevel(org.getLevel()+1);
    					if(!oraOrganization.getBussGroupId().equals(org.getBussGroupId())) {
    						throw new ServiceException("上级组织是虚拟组织，不能修改业务分组！");
    					}
    					//oraOrganization.setBussGroupId(org.getBussGroupId());
    				}
    			}
    		}
        	oraOrganization.setId(IdGenerator.nextId());
        	Date date = new Date();
        	oraOrganization.setCreateTime(date);
        	oraOrganization.setCreateUser(userVo.getAccount());
        	oraOrganization.setUpdateTime(date);
        	oraOrganization.setUpdateUser(userVo.getAccount());

        	oraOrganization.setName(organizationVo.getName().trim());
        	
        	String splitCode = treeCodeService.genSplitCode(oraOrganization.getPid(), oraOrganization.getOrgCode(), TreeType.ORG.getVal());
        	oraOrganization.setOrgCodeSplit(splitCode);
        	
            organizationMapper.insertSelective(oraOrganization);
        }else {//添加根机构
        	Example exampleName = new Example(Organization.class);
        	exampleName.createCriteria().andEqualTo("name", organizationVo.getName());
    		int countName = organizationMapper.selectCountByExample(exampleName);
    		if(countName>0) {
    			throw new ServiceException("组织机构名称已经存在，请修改！");
    		}
    		Example exampleCode = new Example(Organization.class);
    		exampleCode.createCriteria().andEqualTo("orgCode", organizationVo.getOrgCode());
    		int countCode = organizationMapper.selectCountByExample(exampleCode);
    		if(countCode > 0) {
    			throw new ServiceException("组织机构代码已经存在，请修改！");
    		}
    		
        	oraOrganization.setId(IdGenerator.nextId());
        	oraOrganization.setPid("0");
        	if(organizationVo.getIsVirtual() == 1) {//虚拟组织
    			oraOrganization.setLevel(1);
    		}
        	oraOrganization.setOrgCodeSplit(organizationVo.getOrgCode());
        	oraOrganization.setCreateTime(new Date());
        	oraOrganization.setCreateUser(userVo.getAccount());

        	oraOrganization.setName(organizationVo.getName().trim());

        	organizationMapper.insertSelective(oraOrganization);
        }
    }
    /**
     * 修改上级组织之后，重新设置级别和业务分组
     * @param orgs
     * @param org
     */
    private void setLevelAndBussGroup(List<OrganizationVo> orgs, String orgId, int parentLevel, String bussGroupId) {
    	for(int i=0;i<orgs.size();i++) {
    		if(orgId.equals(orgs.get(i).getId())) {
    			orgs.get(i).setLevel(parentLevel+1);
    			orgs.get(i).setBussGroupId(bussGroupId);
    			setLevelAndBussGroup(orgs, orgs.get(i).getPid(), parentLevel+1, bussGroupId);
    		}
    	}
    }
    
    /**
     * 移动的节点中有多少个层级
     * @param orgs
     * @return
     */
    private Integer getMoveLevels(List<OrganizationVo> orgs) {
    	List<Integer> list = new ArrayList<Integer>();
    	for(OrganizationVo orgVo : orgs) {
    		if(!list.contains(orgVo.getLevel())) {
    			list.add(orgVo.getLevel());
    		}
    	}
    	return list.size();
    }
    @Override
    public void delOrganizationsById(String curId){
        List<String> ids = new ArrayList<String>();
        ids.add(curId);
        Example example = new Example(Organization.class);
        example.createCriteria().andIn("pid", ids);
        int orgs = organizationMapper.selectCountByExample(example);
        /*UserOrgnizationExample userExample = new UserOrgnizationExample();
        userExample.createCriteria().andOrganizationIdIn(ids);
        int users = userOrganizationMapper.countByExample(userExample);*/
        if(orgs>0) {
            throw new ServiceException("组织机构下面有子机构，不能删除！");
        }
        /*if(users>0) {
            throw new ServiceException("组织机构下面有关联用户，不能删除！");
        }*/
        Example cme = new Example(CameraMapping.class);
        cme.createCriteria().andIn("orgId", ids);
        int cameras = cameraMappingMapper.selectCountByExample(cme);
        if(cameras>0) {
        	throw new ServiceException("组织机构下有关联摄像头，不能删除");
        }
        organizationMapper.deleteByPrimaryKey(curId);
    }

    @Override
    public void updateOrgInfoById(OrganizationVo organizationVo) {
        Organization oraOrganization = Convert.convert(Organization.class,organizationVo);
        organizationMapper.updateByPrimaryKey(oraOrganization);
    }

    @Override
    public PageInfo<OrganizationVo> getOrgPages(PageParam pp, TreeNodeChildren treeNodeChildren, OrganizationVo orgVo) {
        /*OrganizationExample example = new OrganizationExample();
        if(StrUtil.isNotEmpty(search)) {
        	example.createCriteria().andNameLike("%"+search+"%");
        }
        PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        List<OrganizationVo> list = organizationMapper.selectByExample(example);
        return new PageInfo<>(list);*/
    	
    	 Map<String,Object> map = new HashMap<String,Object>();
         map.put("orgIds", treeNodeChildren.getOrgNodeIds());
         map.put("search", orgVo.getName());
         map.put("isVirtual", orgVo.getIsVirtual());
         
    	PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
    	List<OrganizationVo> list = organizationMapper.selectOrganizationList(map);
    	return new PageInfo<OrganizationVo>(list);
    }

    @Override
    public void delOrganizationsByIds(List<String> ids){
        Example example = new Example(Organization.class);
        example.createCriteria().andIn("pid", ids);
        int orgs = organizationMapper.selectCountByExample(example);
        /*UserOrgnizationExample userExample = new UserOrgnizationExample();
        userExample.createCriteria().andOrganizationIdIn(ids);
        int users = userOrganizationMapper.countByExample(userExample);*/
        if(orgs>0) {
            throw new ServiceException("组织机构下面有子机构，不能删除！");
        }
        /*if(users>0) {
            throw new ServiceException("组织机构下面有关联用户，不能删除！");
        }*/
        Example cme = new Example(CameraMapping.class);
        cme.createCriteria().andIn("orgId", ids);
        int cameras = cameraMappingMapper.selectCountByExample(cme);
        if(cameras>0) {
        	throw new ServiceException("组织机构下有关联摄像头，不能删除");
        }
        Example idoe = new Example(Organization.class);
        idoe.createCriteria().andIn("id", ids);
        organizationMapper.deleteByExample(idoe);
    }

    @Override
    public OrganizationVo getOrganizationById(String curId) {
        /*OrganizationVo org = organizationMapper.selectByPrimaryKey(curId);
        return org;*/
    	OrganizationVo org = organizationMapper.selectOrganizationById(curId);
    	return org;
    }

	@Override
	public void batchInsertOrgs(List<Map<String, String>> list, List<CheckAttributeVo> checkVos, String isVirtual, UserVo userVo, Map<String,List<String>> relateIdMap) {
		List<Organization> orgs = transMapToOrgs(list, checkVos, isVirtual, userVo, relateIdMap);
		if(orgs != null && orgs.size()>0) {
			organizationMapper.insertList(orgs);
		}
	}
	
	private List<Organization> transMapToOrgs(List<Map<String, String>> list, List<CheckAttributeVo> checkVos, String isVirtual, UserVo userVo, Map<String,List<String>> relateIdMap){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Organization> orgs = new ArrayList<Organization>();
		
		List<String> orgCodes = new ArrayList<String>();
		List<String> parentOrgCodes = new ArrayList<String>();
		
		List<Integer> parent_org_index = new ArrayList<Integer>();
		
		List<String> bussIdList = relateIdMap.get("bussIdList");
		
		for(int i=0;i<list.size();i++) {
			Map<String,String> map = list.get(i);
			
			Class<Organization> clzz = null;
			Organization org = null;
			try {
				clzz = (Class<Organization>) Class.forName("com.hdvon.nmp.entity.Organization");
				org = clzz.newInstance();
				org.setId(IdGenerator.nextId());

				for(int j=0;j<checkVos.size();j++) {
					CheckAttributeVo checkVo = checkVos.get(j);
					if(checkVo.isMapping()) {
						continue;
					}
					String attr = checkVo.getAttr();
					if("orgCode".equals(checkVo.getCode())) {
						orgCodes.add(map.get(attr));
					}else if("parentOrgCode".equals(checkVo.getCode())){
						parentOrgCodes.add(map.get(attr));
					}
					String methodAttr = attr.substring(0, 1).toUpperCase()+checkVo.getAttr().substring(1);
					Method getMethod = clzz.getMethod("get"+methodAttr);
					Type type = getMethod.getGenericReturnType();
					if("class java.lang.Integer".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Integer.class);
						setMethod.invoke(org, StrUtil.isBlank(map.get(attr))?null:Integer.parseInt(map.get(attr)));
					}else if("class java.lang.Double".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Double.class);
						setMethod.invoke(org, StrUtil.isBlank(map.get(attr))?null:Double.parseDouble(map.get(attr)));
					}else if("class java.lang.String".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, String.class);
						setMethod.invoke(org, map.get(attr));
					}else if("class java.util.Date".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Date.class);
						JSONObject valid = checkVo.getValid();
						if(valid != null) {
							String date = valid.getString("DATE");
							if(StrUtil.isNotBlank(date)) {
								JSONArray formatArray = (JSONArray) JSONObject.parse(date);
								if(formatArray != null && formatArray.size()>0) {
									SimpleDateFormat format = new SimpleDateFormat(formatArray.getString(0));
									try {
										setMethod.invoke(org, format.parse(map.get(attr)));
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
				if("1".equals(isVirtual)) {
					org.setBussGroupId(bussIdList.get(i));
				}
				
				org.setIsVirtual(Integer.parseInt(isVirtual));
				org.setCreateTime(new Date());
				org.setCreateUser(userVo.getAccount());
				org.setUpdateTime(new Date());
				org.setUpdateUser(userVo.getAccount());
				orgs.add(org);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=0;i<parentOrgCodes.size();i++) {
			if("0".equals(parentOrgCodes.get(i))) {
				parent_org_index.add(-1);
			}else {
				if(orgCodes.contains(parentOrgCodes.get(i))) {
					parent_org_index.add(orgCodes.indexOf(parentOrgCodes.get(i)));
				}else {
					parent_org_index.add(null);
				}
			}
		}
		List<Organization> tempExcelOrgs = new ArrayList<Organization>();//父节点在excel中的节点
		List<Organization> tempDataOrgs = new ArrayList<Organization>();//父节点在数据库中的节点
		List<String> tempCodes = new ArrayList<String>();
		List<String> tempExcelCodes = new ArrayList<String>();
		List<String> tempExcelParentCodes = new ArrayList<String>();
		for(int i=0;i<orgs.size();i++) {
			if(parent_org_index.get(i) == null){//excel编号中没有对应的父编号，要根据父编号到数据库中找父节点
				tempCodes.add(orgs.get(i).getParentOrgCode());//缓存excel中父节点在数据库中的所有节点父编号
				tempDataOrgs.add(orgs.get(i));
			}else if(parent_org_index.get(i) == -1) {//父节点
				orgs.get(i).setPid("0");
				tempExcelCodes.add(orgs.get(i).getOrgCode());
				tempExcelParentCodes.add(orgs.get(i).getParentOrgCode());
				tempExcelOrgs.add(orgs.get(i));
			}else {
				orgs.get(i).setPid(orgs.get(parent_org_index.get(i)).getId());
				tempExcelCodes.add(orgs.get(i).getOrgCode());
				tempExcelParentCodes.add(orgs.get(i).getParentOrgCode());
				tempExcelOrgs.add(orgs.get(i));
			}
		}
		Map<String,Object> pcodeMap = new HashMap<String,Object>();
		pcodeMap.put("orgCodes", tempCodes);
		List<OrganizationVo> tempParentOrgs = organizationMapper.selectOrganizationList(pcodeMap);
		
		for(Organization organization : tempDataOrgs) {
			for(OrganizationVo organizationVo : tempParentOrgs) {
				if(organization.getParentOrgCode().equals(organizationVo.getOrgCode())) {//父子关系
					organization.setPid(organizationVo.getId());
					organization.setOrgCodeSplit(organizationVo.getOrgCodeSplit()+"-"+organization.getOrgCode());
				}
			}
		}
		
		List<Organization> result = new ArrayList<Organization>();//返回结果
		if(parentOrgCodes.contains("0")) {//excel中有父编号为0的根节点
			List<Integer> tempParentIndex = new ArrayList<Integer>();
			for(int i=0;i<tempExcelParentCodes.size();i++) {
				if("0".equals(tempExcelParentCodes.get(i))) {
					tempParentIndex.add(-1);
				}else {
					if(tempExcelCodes.contains(tempExcelParentCodes.get(i))) {
						tempParentIndex.add(tempExcelCodes.indexOf(tempExcelParentCodes.get(i)));
					}else {
						tempParentIndex.add(null);
					}
				}
			}
			Organization rootOrg = null;
			for(int i=0;i<tempExcelOrgs.size();i++) {
				Organization org = tempExcelOrgs.get(i);
				if(tempParentIndex.get(i)==null)continue;
				if(tempParentIndex.get(i) == -1) {
					org.setPid("0");
					org.setOrgCodeSplit(org.getOrgCode());
					rootOrg = org;
				}else {
					org.setPid(orgs.get(tempParentIndex.get(i)).getId());
				}
			}
			
			result.add(rootOrg);
			setCodeSplit(rootOrg, tempExcelOrgs, result);
			for(Organization organization : tempDataOrgs) {
				setCodeSplit(organization, tempExcelOrgs, result);
			}
			result.addAll(tempDataOrgs);//使用数据库数据更新excel中父节点在数据库中的节点数据，并添加到excel数据集合中
		}else {//没有根节点，说明没有父节点在excel中的节点，即excel中节点的父节点都在数据库中
			result.addAll(tempDataOrgs);
			
		}
		
		return result;
	}
	/**
	 * @param org
	 * @param orgs
	 * @param newOrgs
	 */
	private void setCodeSplit(Organization org,List<Organization> orgs, List<Organization> newOrgs) {
		for(int i=0;i<orgs.size();i++) {
			if(orgs.get(i) == null) {
				continue;
			}
			if(org.getOrgCode().equals(orgs.get(i).getParentOrgCode())) {
				Organization organization = orgs.get(i);
				organization.setOrgCodeSplit(org.getOrgCodeSplit()+"-"+organization.getOrgCode());
				newOrgs.add(organization);
				orgs.set(i, null);
				setCodeSplit(organization,orgs,newOrgs);
			}
		}
	}
	@Override
	public List<OrganizationVo> getOrgList(Map<String, Object> pram, TreeNodeChildren treeNodeChildren) {
		pram.put("orgIds", treeNodeChildren.getOrgNodeIds());
		List<OrganizationVo> list = organizationMapper.selectOrganizationList(pram);
		return list;
	}



}
