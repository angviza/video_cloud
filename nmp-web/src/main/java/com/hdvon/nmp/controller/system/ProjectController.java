package com.hdvon.nmp.controller.system;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IProjectService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.DepartmentProject;
import com.hdvon.nmp.vo.ProjectDepartmentVo;
import com.hdvon.nmp.vo.ProjectParamVo;
import com.hdvon.nmp.vo.ProjectVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.TreeNodeDepartment;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/project",tags="项目管理模块",description="针对项目的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController extends BaseController{
	@Reference
	private IProjectService projectService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	//分页查询项目列表
	@ApiOperation(value="分页查询项目列表")
	@ApiImplicitParam(name = "search", value = "项目名称查询条件", required = false)
	@GetMapping(value = "/projectPage")
	public ApiResponse<PageInfo<ProjectVo>> projectPage(PageParam pp, ProjectParamVo projectParamVo) {
		ApiResponse<PageInfo<ProjectVo>> resp = new ApiResponse<PageInfo<ProjectVo>>();
		 
		TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(projectParamVo.getDeptId())) {
			List<TreeNodeDepartment> deptNodes = treeNodeService.getDeptChildNodesByCode(projectParamVo.getDeptCode(), TreeType.DEPARTMENT.getVal());
			treeNodeChildren.setDeptNodes(deptNodes);
		}
			
        PageInfo<ProjectVo> pageInfo= projectService.getProjectPages(pp, treeNodeChildren, projectParamVo);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}
	//添加项目或者编辑项目初始化
	@ApiOperation(value = "添加项目或者编辑项目初始化")
	@GetMapping(value="/edit")
	public ApiResponse<ProjectVo> edit(HttpServletRequest request, ProjectDepartmentVo projectDepartmentVo){
		 ApiResponse<ProjectVo> resp = new ApiResponse<ProjectVo>();
		 ProjectVo projectVo = null;
         if(projectDepartmentVo!=null) {
             projectVo = projectService.getProjectById(projectDepartmentVo);
         }
         resp.ok("查询成功！").setData(projectVo);

		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存项目信息")
	@PostMapping(value = "/addOrEdit")
	public ApiResponse<Object> addOrEdit(HttpServletRequest request, ProjectVo projectVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(projectVo.getCode())) {
            validMsg = "项目编码不能为空！";
        }
        if(StrUtil.isBlank(projectVo.getName())) {
            validMsg = "项目名称不能为空！";
        }
        if(StrUtil.isBlank(projectVo.getConstructorDepId()) && StrUtil.isBlank(projectVo.getBuilderDepId())) {
            validMsg = "建设单位和承建单位至少填一个！";
        }
        if(StrUtil.isBlank(projectVo.getContactor())) {
            validMsg = "联系人不能为空！";
        }
        if(StrUtil.isBlank(projectVo.getMobile())) {
            validMsg = "手机号不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        projectService.addProject(userVo, projectVo);
        resp.ok("保存成功！");
        return resp;
	}
	//删除项目
	@ApiOperation(value = "删除项目")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        projectService.delProjectsByIds(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
	
	//下载项目模板文件
	@ApiOperation(value = "下载项目模板文件")
	@GetMapping(value="/downloadProjectTemplate")
	public ApiResponse<Object> downloadProjectTemplate(HttpServletRequest request, HttpServletResponse response){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			String templateName = "address_template.xls";
			FileUtil.downloadTemplate(request, response, templateName, null);
			resp.ok("下载成功！");
		}catch (Exception e) {
			log.error(e.getMessage());
			resp.error("下载失败："+e.getMessage());
		}
		return resp;
	}
	//批量导入项目数据
	@ControllerLog
	@ApiOperation(value = "导入地址数据")
	@PostMapping(value="/importProjects")
	public ApiResponse<Object> importProjects(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			String[] titles = new String[] {"name","code","classType","contactor","phone","mobile","mail","builderCode","constructorCode"};
			List<Map<String,Object>> list = FileUtil.importData(file, titles);
			projectService.batchInsertProjects(list, titles);
			resp.ok("导入成功！");
		}catch(IOException e) {
			log.error(e.getMessage());
			resp.error("导入失败："+e.getMessage());
		}
		return resp;
	}
	//批量导出项目列表数据
	@ControllerLog
	@ApiOperation(value = "批量导出项目列表数据")
	@PostMapping(value="/exportProjects")
	public ApiResponse<Object> exportProjects(HttpServletRequest request, HttpServletResponse response){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		return resp;
	}
}
