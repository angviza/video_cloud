package com.hdvon.nmp.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.aop.ControllerRedis;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.generate.GenerateDepartmentExcel;
import com.hdvon.nmp.generate.util.FunTypeEnum;
import com.hdvon.nmp.generate.util.PropertyConfig;
import com.hdvon.nmp.generate.util.PropertyUtil;
import com.hdvon.nmp.service.IDepartmentService;
import com.hdvon.nmp.service.IGenerateValidService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;
import com.hdvon.nmp.vo.DepartmentVo;
import com.hdvon.nmp.vo.ValidAttrVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.TreeNodeDepartment;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/department",tags="部门管理模块",description="针对部门的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController extends BaseController{
	@Reference
	private IDepartmentService departmentService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	@Reference
	private IGenerateValidService generateValidService;
	
	//分页查询部门信息
	@ApiOperation(value="分页查询部门信息")
	@ApiImplicitParam(name = "search", value = "部门名称查询条件", required = false)
	@GetMapping(value = "/departmentPage")
	public ApiResponse<PageInfo<DepartmentVo>> departmentPage(PageParam pp,@RequestParam(required = false)String id, @RequestParam(required = false)String code, @RequestParam(required = false)String search) {
		ApiResponse<PageInfo<DepartmentVo>> resp = new ApiResponse<PageInfo<DepartmentVo>>();
		
		TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(id)) {
			List<TreeNodeDepartment> deptNodes = treeNodeService.getDeptChildNodesByCode(code, TreeType.DEPARTMENT.getVal());
			treeNodeChildren.setDeptNodes(deptNodes);
		}
		DepartmentVo deptVo = new DepartmentVo();
		deptVo.setName(search);
		deptVo.setId(id);
		
        PageInfo<DepartmentVo> pageInfo = departmentService.getDeptPages(pp, treeNodeChildren, deptVo);
        resp.ok("查询成功！").setData(pageInfo);

		return resp;
	}
	//查询部门信息
	@ApiOperation(value="查询部门信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "isConstructor", value = "建设部门标识（0：否；1：是）", required = true),
		@ApiImplicitParam(name = "isBuilder", value = "承建部门标识（0：否；1：是）", required = true)
	})
    @GetMapping(value = "/departments")
    public ApiResponse<List<DepartmentVo>> departments(PageParam pp,@RequestParam(required = true)String isConstructor, @RequestParam(required = true)String isBuilder) {
        ApiResponse<List<DepartmentVo>> resp = new ApiResponse<List<DepartmentVo>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("isConstructor", Integer.parseInt(isConstructor));
        map.put("isBuilder", Integer.parseInt(isBuilder));
        List<DepartmentVo> list = departmentService.getDeptList(null);
        resp.ok("查询成功！").setData(list);
        return resp;
    }
	//添加当前机构，添加下级部门或者编辑初始化
	@ApiOperation(value = "查询部门信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "部门id，用于编辑部门", required = false, dataType = "String"),
		@ApiImplicitParam(name = "pid", value = "部门id,用于添加下级部门", required = false, dataType = "String"),
	})
	@GetMapping(value="/edit")
	public ApiResponse<DepartmentVo> edit(HttpServletRequest request,  Model model, String id, String pid){
        ApiResponse<DepartmentVo> resp = new ApiResponse<DepartmentVo>();
		DepartmentVo deptVo = null;

        if(StringUtils.isNotEmpty(id)) {//编辑部门
            deptVo = departmentService.getDepartmentById(id);
            resp.ok("初始化成功！").setData(deptVo);
            return resp;
        }else if(StringUtils.isNotEmpty(pid)) {//添加下级部门
            deptVo = departmentService.getDepartmentById(pid);
            if(deptVo == null) {
                resp.error("用于添加子部门的部门不存在，请刷新页面！");
                return resp;
            }
            resp.ok("初始化成功！").setData(deptVo);
            return resp;
        }
        resp.ok("初始化成功！").setData(deptVo);

		return resp;
	}
	
	//添加或者编辑提交
	@ControllerRedis
	@ControllerLog
	@ApiOperation(value = "保存部门信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request,DepartmentVo department) {
		UserVo userVo = getLoginUser();
		ApiResponse<Object> apiRes = new ApiResponse<Object>();

        if(StrUtil.isBlank(department.getName())) {
            apiRes.error("部门名称不能为空！");
            return apiRes;
        }
        if(StrUtil.isBlank(department.getDepCode())) {
        	apiRes.error("部门编码不能为空！");
        }
        if(StrUtil.isBlank(department.getPid())) {
        	apiRes.error("上级部门不能为空！");
        }
        departmentService.saveDepartment(userVo,department);
        
        apiRes.ok("提交成功！");

		return apiRes;
	}
	
	//删除部门
	@ControllerRedis
	@ApiOperation(value = "删除部门")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();

        List<String> idList = Arrays.asList(ids);
        departmentService.delDepartmentsByIds(idList);
        
        apiRes.ok("删除成功！");
		return apiRes;
	}
	
	@ApiOperation(value = "下载部门文件模板")
	@GetMapping(value="/downloadDepartmentTemplate")
	public ApiResponse<Object> downloadDepartmentTemplate(HttpServletRequest request, HttpServletResponse response){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			String templateName = "department_template.xls";
			FileUtil.downloadTemplate(request, response, templateName, null);
			resp.ok("下载成功！");
		}catch(Exception e) {
			log.error(e.getMessage());
			resp.error("下载失败："+e.getMessage());
		}
		return resp;
	}
	
	//导入部门
	@SuppressWarnings("unchecked")
	@ControllerLog
	@ApiOperation(value="导入部门数据")
	@PostMapping(value = "/importDepartments")
	public ApiResponse<Object> importDepartments(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		try {
			List<CheckAttributeVo> checkAttrs = PropertyUtil.getCheckAttributes(PropertyConfig.PROPERTY_DEPT_IMPORT);
			Map<String,Object> result = GenerateDepartmentExcel.generateDeptData(file, checkAttrs);
			List<Map<String,String>> list = (List<Map<String, String>>) result.get("excelData");
			Map<String,List<String>> treeData = (Map<String, List<String>>) result.get("treeData");
			int[] validCols = (int[]) result.get("validCols");
			Integer cursRow = (Integer) result.get("cursRow");
			Map<String,List<String>> relateIdMap = generateValidService.checkTreeNodeExists(treeData, TreeType.DEPARTMENT.getVal(), new ValidAttrVo(FunTypeEnum.DEPARTMENT.getVal(),validCols),cursRow, null);
			departmentService.batchInsertDepartments(list, checkAttrs, userVo,relateIdMap);
			resp.ok("导入成功！");
		}catch(IOException e) {
			log.error(e.getMessage());
			resp.error("导入失败！");
		}
		return resp;
	}

    //根据用户id查询可授权部门用户树列表
    @ApiOperation(value="根据用户id查询可授权部门用户树列表")
    @GetMapping(value = "/getUserAuthorizeDeptTree")
    public ApiResponse<List<DepartmentUserTreeVo>> getUserAuthorizeDeptTree() {
        UserVo userVo = getLoginUser();
        ApiResponse<List<DepartmentUserTreeVo>> resp = new ApiResponse<List<DepartmentUserTreeVo>>();
        List<DepartmentUserTreeVo> list = departmentService.getUserAuthorizeDeptTree(userVo.getId());
        resp.ok("查询成功！").setData(list);
        return resp;
    }
    
    @ApiOperation(value="导出部门信息")
	@ApiImplicitParam(name = "search", value = "部门名称查询条件", required = false)
	@GetMapping(value = "/exportData")
	public ApiResponse<Object> exportData( @RequestParam(required = false)String id, @RequestParam(required = false)String code, @RequestParam(required = false)String search) {
        ApiResponse<Object> resp = new ApiResponse<Object>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(id)) {
			List<TreeNodeDepartment> deptNodes = treeNodeService.getDeptChildNodesByCode(code, TreeType.DEPARTMENT.getVal());
			treeNodeChildren.setDeptNodes(deptNodes);
		}
		DepartmentVo deptVo = new DepartmentVo();
		deptVo.setName(search);
		deptVo.setId(id);
		
        List<DepartmentVo> list = departmentService.getDepartmentList(deptVo, treeNodeChildren);
       
        String[] titles = new String[] {"depCode","name","parentDepCode","parentDepName","orgName","isConstructor","isBuilder","contactor","telelno","mobile","address","description"};
        String[] titleNames = new String[] {"部门代码","部门名称","父部门代码","父部门名称","所属行政区划","是否建设单位","是否承建单位","联系人","电话号码","手机号","联系地址","说明"};
        try {
        	List<Map<String,Object>> deptList = GenerateDepartmentExcel.transDeptEntityToMap(list);
            GenerateDepartmentExcel.exportDeptData(response, titles, titleNames, "部门信息", deptList);
            resp.ok("导出成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.error("导出失败！");
        }
        return resp;
        
	}
    
//
//	//根据当前登录的用户id查询可授权部门用户树列表
//	@ApiOperation(value="根据当前登录的用户id查询可授权部门用户树列表")
//	@GetMapping(value = "/getUserFromDeptAuthorizeTree")
//	public ApiResponse<List<DepartmentUserTreeVo>> getUserFromDeptAuthorizeTree() {
//		UserVo loginUser = getLoginUser();
//		ApiResponse<List<DepartmentUserTreeVo>> resp = new ApiResponse<List<DepartmentUserTreeVo>>();
//		if(loginUser != null) {
//			try {
//				List<DepartmentUserTreeVo> list = departmentService.getUserAuthorizeDeptTree(loginUser.getId());
//		        resp.ok(ResponseCode.SUCCESS.getMessage()).setData(list);
//			} catch (Exception e) {
//				// TODO: handle exception
//				log.debug("根据当前登录的用户id查询可授权部门用户树列表失败:"+e.getMessage());
//				resp.error(ResponseCode.FAILURE.getMessage());
//			}
//		}
//		return resp;
//	}
	
}
