package com.hdvon.nmp.controller.system;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.vo.EsCameraPermissionVo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.aop.ControllerRedis;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.generate.FunTypeEnum;
import com.hdvon.nmp.generate.GenerateOrgExcel;
import com.hdvon.nmp.generate.GenerateTemplate;
import com.hdvon.nmp.generate.util.ExcelUtil;
import com.hdvon.nmp.generate.util.PropertyConfig;
import com.hdvon.nmp.generate.util.PropertyUtil;
import com.hdvon.nmp.service.IDictionaryService;
import com.hdvon.nmp.service.IGenerateValidService;
import com.hdvon.nmp.service.IOrganizationService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.OrderedProperties;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.AddressVo;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DictionaryVo;
import com.hdvon.nmp.vo.OrganizationVo;
import com.hdvon.nmp.vo.ValidAttrVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api(value="/orgnization",tags="组织机构管理模块",description="针对组织机构的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/orgnization")
@Slf4j
public class OrgnizationController extends BaseController {
	@Reference
	private IOrganizationService organizationService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	@Reference
	private IDictionaryService dictionaryService;
	
	@Reference
	private IGenerateValidService generateValidService;
	//分页查询组织机构信息
	@ApiOperation(value="分页查询组织机构信息")
	@ApiImplicitParams(
		{
			@ApiImplicitParam(name = "id", value = "组织机构id", required = false),
			@ApiImplicitParam(name = "code", value = "组织机构编号", required = false),
			@ApiImplicitParam(name = "search", value = "组织机构名称查询条件", required = false),
			@ApiImplicitParam(name = "isVirtual", value = "是否虚拟组织（1：是，0：否）", required = false)}
	)
	@GetMapping(value = "/orgnizationPage")
	public ApiResponse<PageInfo<OrganizationVo>> orgnizationPage(PageParam pp,@RequestParam(required = false)String id, @RequestParam(required = false)String code, @RequestParam(required = false)String search, Integer isVirtual) {
		ApiResponse<PageInfo<OrganizationVo>> resp = new ApiResponse<PageInfo<OrganizationVo>>();

		TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(id)) {
			List<TreeNode> orgNodes = treeNodeService.getChildNodesByCode(code, TreeType.ORG.getVal());
			treeNodeChildren.setOrgNodes(orgNodes);
		}
        OrganizationVo orgVo = new OrganizationVo();
        orgVo.setName(search);
        orgVo.setIsVirtual(isVirtual);
        
        PageInfo<OrganizationVo> pageInfo = organizationService.getOrgPages(pp, treeNodeChildren, orgVo);
        resp.ok("查询成功！").setData(pageInfo);
		return resp;
	}

	//添加当前机构，添加下级机构或者编辑初始化
	@ApiOperation(value = "查询组织机构信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "组织机构id，用于编辑组织机构", required = false, dataType = "String"),
		@ApiImplicitParam(name = "pid", value = "组织机构id,用于添加下级机构", required = false, dataType = "String"),
	})
	@GetMapping(value="/edit")
	public ApiResponse<OrganizationVo> edit(HttpServletRequest request,  Model model, String id, String pid){
        ApiResponse<OrganizationVo> resp = new ApiResponse<OrganizationVo>();
		OrganizationVo org = null;

        if(StringUtils.isNotEmpty(id)) {//编辑组织机构
            org = organizationService.getOrganizationById(id);
            resp.ok("初始化成功！").setData(org);
            return resp;
        }else if(StringUtils.isNotEmpty(pid)) {//添加下级机构
            org = organizationService.getOrganizationById(pid);
            if(org == null) {
                resp.error("用于添加子机构的组织机构不存在，请刷新页面！");
                return resp;
            }
            resp.ok("初始化成功！").setData(org);
            return resp;
        }
        resp.ok("初始化成功！").setData(org);

		return resp;
	}
	
	//添加或者编辑提交
	@ControllerRedis
	@ControllerLog
	@ApiOperation(value = "保存组织机构信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request,OrganizationVo org) {
		UserVo userVo = getLoginUser();
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        if(StrUtil.isBlank(org.getName())) {
            apiRes.error("组织机构名称不能为空！");
            return apiRes;
        }
        if(StrUtil.isBlank(org.getPid())) {
            apiRes.error("上级组织机构不能为空！");
            return apiRes;
        }
        if(StrUtil.isBlank(org.getOrgCode())) {
            apiRes.error("组织机构代码不能为空！");
            return apiRes;
        }
        if(org.getIsVirtual() != 1 && org.getOrgType() == null) {//行政区划
        	apiRes.error("机构类型不能为空！");
        	return apiRes;
        }
        if(org.getIsVirtual() == 1 && StrUtil.isBlank(org.getBussGroupId())) {//虚拟组织校验
            apiRes.error("虚拟组织的业务分组不能为空！");
            return apiRes;
        }
        if(StrUtil.isNotBlank(org.getId()) && org.getPid().equals(org.getId())) {
        	return apiRes.error("行政区划的上级机构不能是自身！");
        }
        if(org.getIsVirtual() == 1) {
        	if(org.getOrgCode().length() != 20) {
        		apiRes.error("虚拟组织编号长度不是20！");
        		return apiRes;
        	}
        }
        
        String parentOrgCode = org.getParentOrgCode();
        if(StrUtil.isNotBlank(parentOrgCode)) {
    		if(org.getIsVirtual() == 0){
    			if(!parentOrgCode.equals(org.getOrgCode().substring(0, parentOrgCode.length()))) {
    				return apiRes.error("组织机构代码错误！");
    			}
    		}
        }
        
        organizationService.addOrganization(userVo,org);
		
        apiRes.ok("保存成功！");
		return apiRes;
	}
	//删除组织机构
	@ControllerRedis
	@ApiOperation(value = "删除组织机构")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids, Integer isVirtual) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        organizationService.delOrganizationsByIds(idList);
       
        apiRes.ok("删除成功！");
		return apiRes;
	}

	@ApiOperation(value = "下载组织机构文件模板")
	@ApiImplicitParam(name = "isVirtual", value = "是否虚拟组织（1：是，0：否）", required = true)
	@GetMapping(value="/downloadOrgnizationTemplate")
	public ApiResponse<Object> downloadOrgnizationTemplate(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="isVirtual",required=true) Integer isVirtual){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			String templateName = "organization_template.xls";
			if(isVirtual == 1) {
				templateName = "virtual_organization_template.xls";
				FileUtil.downloadTemplate(request, response, templateName, null);
				resp.ok("下载成功！");
			}
			List<String> dicKeys = new ArrayList<String>();
			OrderedProperties dicPreperties = PropertyUtil.getProperties(PropertyConfig.PROPERTY_ORG_DIC);
			Set<String> dicEn = dicPreperties.stringPropertyNames();
			Iterator<String> dicIt = dicEn.iterator();
			while(dicIt.hasNext()) {
				String dicKey = (String) dicIt.next();
				String dicVal = dicPreperties.getProperty(dicKey);
				dicKeys.add(dicVal);
			}
			
			Map<String,List<DictionaryVo>> dicMap = dictionaryService.getDictionaryMap(dicKeys);
			OrderedProperties cameraProperties = PropertyUtil.getProperties(PropertyConfig.PROPERTY_ORG);
			String cols = null;
			if(isVirtual == 1) {
				cols = cameraProperties.getProperty("virtualMaxCols");
			}else {
				cols = cameraProperties.getProperty("orgMaxCols");
			}
			
			String colWidth = cameraProperties.getProperty("colWidth");
			String explainRowNo = cameraProperties.getProperty("explainRowNo");
			
			GenerateTemplate.downloadTemplate(request, response, templateName, null, dicMap, cols, Integer.parseInt(colWidth), Integer.parseInt(explainRowNo));
			resp.ok("下载成功！");
		}catch(Exception e) {
			log.error(e.getMessage());
			resp.error("下载失败："+e.getMessage());
		}
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@ControllerLog
	@ApiOperation(value = "导入组织机构数据")
	@PostMapping(value="/importOrgnizations")
	public ApiResponse<Object> importOrgnizations(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String isVirtual){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		List<CheckAttributeVo> checkAttrs = null;
		try {
			if("0".equals(isVirtual)) {
				checkAttrs = PropertyUtil.getCheckAttributes(PropertyConfig.PROPERTY_ORG_IMPORT);
			}else {
				checkAttrs = PropertyUtil.getCheckAttributes(PropertyConfig.PROPERTY_ORG_VIR_IMPORT);
			}
			/*List<String> param = new ArrayList<String>();
        	param.add("ORGTYPE");
        	List<DictionaryVo> dicVos = dictionaryService.getDictionaryList(param);
        	Map<String,List<String>> dicMap = dicVos.stream().collect(Collectors.groupingBy(DictionaryVo::getTypeEhName,Collectors.mapping(DictionaryVo::getValue,Collectors.toList())));*/
			
			List<String> dicKeys = new ArrayList<String>();
			Map<String,String> dicKeyMap = new HashMap<String,String>();
			OrderedProperties dicPreperties = PropertyUtil.getProperties(PropertyConfig.PROPERTY_ORG_DIC);
			Set<String> dicEn = dicPreperties.stringPropertyNames();
			Iterator<String> dicIt = dicEn.iterator();
			while(dicIt.hasNext()) {
				String dicKey = (String) dicIt.next();
				String dicVal = dicPreperties.getProperty(dicKey);
				dicKeys.add(dicVal);
				dicKeyMap.put(dicKey, dicVal);
			}
			Map<String,List<DictionaryVo>> dicMap = dictionaryService.getDictionaryMap(dicKeys);
    		
			List<String> orgCodes = new ArrayList<String>();
    		Map<String,Object> param = new HashMap<String,Object>();
        	param.put("isVirtual", Integer.parseInt(isVirtual));
        	TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
        	List<OrganizationVo> orgVos = organizationService.getOrgList(param, treeNodeChildren);
        	
        	for(OrganizationVo orgVo : orgVos) {
        		orgCodes.add(orgVo.getOrgCode());
        	}
        	
        	Map<String,Object> result = GenerateOrgExcel.generateOrgExcel(file, checkAttrs, dicMap, dicKeyMap, isVirtual, orgCodes);
        	
			List<Map<String,String>> list = (List<Map<String, String>>) result.get("excelData");
			Map<String,List<String>> treeData = (Map<String, List<String>>) result.get("treeData");
			int[] validCols = (int[]) result.get("validCols");
			Integer cursRow = (Integer) result.get("cursRow");
			Map<String,List<String>> relateIdMap = generateValidService.checkTreeNodeExists(treeData, TreeType.ORG.getVal(), new ValidAttrVo(FunTypeEnum.ORG.getVal(),validCols), cursRow, isVirtual);;
        	organizationService.batchInsertOrgs(list, checkAttrs, isVirtual, userVo, relateIdMap);
			resp.ok("导入成功！");
		}catch(IOException e) {
			log.error(e.getMessage());
			resp.error("导入失败："+e.getMessage());
		}
		return resp;
	}
	
	
	@ApiOperation(value="导出组织机构信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "search", value = "组织机构名称查询条件", required = false),
		@ApiImplicitParam(name = "isVirtual", value = "是否虚拟组织（1：是，0：否）", required = true)
	})
	@GetMapping(value = "/exportData")
	public ApiResponse<Object> exportData( @RequestParam(required = false)String id, @RequestParam(required = false)String code, @RequestParam(required = false)String search, Integer isVirtual) {
        ApiResponse<Object> resp = new ApiResponse<Object>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(id)) {
			List<TreeNode> orgNodes = treeNodeService.getChildNodesByCode(code, TreeType.ORG.getVal());
			treeNodeChildren.setOrgNodes(orgNodes);
		}
        
        Map<String,Object> pram = new HashMap<String,Object>();
        pram.put("search", search);
        pram.put("isVirtual",isVirtual);
        List<OrganizationVo> list = organizationService.getOrgList(pram, treeNodeChildren);
        
        String[] titles = null;
        String[] titleNames = null;
        if(isVirtual == 0) {
        	 titles = new String[] {"orgCode","name","parentOrgCode","parentOrgName","isVirtual","orgType","description"};
        	 titleNames = new String[] {"机构代码","机构名称","父机构代码","父机构名称","是否虚拟组织","机构类型","说明"};
        }else {
        	 titles = new String[] {"orgCode","name","parentOrgCode","parentOrgName","isVirtual","bussGroupName","description"};
        	 titleNames = new String[] {"机构代码","机构名称","父机构代码","父机构名称","是否虚拟组织","所属业务分组","说明"};
        }
        try {
        	List<String> param = new ArrayList<String>();
        	param.add("ORGTYPE");
        	List<DictionaryVo> dicVos = dictionaryService.getDictionaryList(param);
        	
        	List<Map<String,Object>> orgList = GenerateOrgExcel.transOrgEntityToMap(list, isVirtual);
            GenerateOrgExcel.exportOrgData(response, isVirtual, "组织机构", titles, titleNames, orgList, dicVos);
            resp.ok("导出成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.error("导出失败！");
        }
        return resp;
        
	}
}
