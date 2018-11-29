package com.hdvon.nmp.controller.system;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IMatrixService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.service.IUserDepartmentService;
import com.hdvon.nmp.service.IUserService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(value="/matrix",tags="矩阵管理模块",description="针对矩阵的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/matrix")
@Slf4j
public class MatrixController extends BaseController{
	
	@Reference
	private IMatrixService matrixService;
	
	@Reference
	private IUserService userService;
	
	@Reference
	private IUserDepartmentService userDepartmentService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	//分页查询矩阵列表
	@ApiOperation(value="分页查询矩阵列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "deptId", value = "部门id", required = false),
		@ApiImplicitParam(name = "deptCode", value = "部门编号", required = false),
	})
	@GetMapping(value = "/page")
	public ApiResponse<PageInfo<MatrixVo>> page(PageParam pp, MatrixParamVo matrixParmVo) {
		 ApiResponse<PageInfo<MatrixVo>> resp = new ApiResponse<PageInfo<MatrixVo>>();
		 UserVo userVo = getLoginUser();
		 TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		 PageInfo<MatrixVo> pageInfo = null;
		 
		 if(userVo.getAccount().toLowerCase().equals("admin")){//管理员返回所有
			 String code =matrixParmVo.getDeptCode();
			 if(StrUtil.isNotBlank(code)) {
				 List<TreeNodeDepartment> searchDeptNodes= treeNodeService.getDeptChildNodesByCode(code, TreeType.DEPARTMENT.getVal());
				 treeNodeChildren.setDeptNodes(searchDeptNodes);
			 }
			 pageInfo = matrixService.getMatrixPages(pp, treeNodeChildren, matrixParmVo);
			 
	      }else {
	    	  // 本部门
	    	  List<TreeNodeDepartment> deptNodes = treeNodeService.getDeptChildNodesByCode(userVo.getDepCodeSplit(), TreeType.DEPARTMENT.getVal());
	 		  //  使用getDeptId() 为了使 depId 与code 保持一致
	 		  String code = StrUtil.isEmpty(matrixParmVo.getDeptId()) ? userVo.getDepCodeSplit() : matrixParmVo.getDeptCode();
	 		  if(StrUtil.isNotBlank(code)) {
		 		  List<TreeNodeDepartment> searchDeptNodes= treeNodeService.getDeptChildNodesByCode(code, TreeType.DEPARTMENT.getVal());
				  treeNodeChildren.setDeptNodes(searchDeptNodes);
				  if(isContains(deptNodes,searchDeptNodes)){
					 pageInfo = matrixService.getMatrixPages(pp, treeNodeChildren, matrixParmVo);
				  }
			  }
	      }
		 return resp.ok("查询成功！").setData(pageInfo);
	}
	//查询矩阵列表
	@ApiOperation(value="查询矩阵列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "部门id", required = false)
	})
	@GetMapping(value = "/list")
	public ApiResponse<List<MatrixVo>> list(PageParam pp,@RequestParam(required = false)String id, MatrixParamVo matrixParmVo) {
		 ApiResponse<List<MatrixVo>> resp = new ApiResponse<List<MatrixVo>>();
         Map<String,Object> paramMap = new HashMap<String,Object>();
         paramMap.put("deptId", id);
         paramMap.put("name", matrixParmVo.getName());
         paramMap.put("deviceNo", matrixParmVo.getDeviceNo());
         paramMap.put("registerUser", matrixParmVo.getRegisterUser());
         paramMap.put("channelName", matrixParmVo.getChannelName());
         List<MatrixVo> list = matrixService.getMatrixList(paramMap);

         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	//添加矩阵或者编辑矩阵初始化
	@ApiOperation(value = "添加矩阵或者编辑矩阵初始化")
	@GetMapping(value="/detail")
	public ApiResponse<MatrixVo> detail(HttpServletRequest request, String id){
		 ApiResponse<MatrixVo> resp = new ApiResponse<MatrixVo>();
         UserVo userVo = getLoginUser();
		 MatrixVo matrixVo = null;
         if(StrUtil.isNotBlank(id)) {
             matrixVo = matrixService.getMatrixById(userVo,id);
         }
         resp.ok("查询成功！").setData(matrixVo);
		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存矩阵信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, MatrixVo matrixVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(matrixVo.getName())) {
            validMsg = "数字矩阵名称不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getDevicesNo())) {
            validMsg = "数字矩阵编号不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getIp())) {
            validMsg = "数字矩阵ip地址不能为空！";
        }
        if(matrixVo.getPort()==null) {
            validMsg = "数字矩阵端口不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getRegisterUser())) {
        	validMsg = "数字矩阵注册用户名不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getRegisterPassword())) {
        	validMsg = "数字矩阵注册用户密码不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getRegisterServerIp())) {
        	validMsg = "数字矩阵注册服务器ip不能为空！";
        }
        if(matrixVo.getRegisterServerPort()==null) {
        	validMsg = "数字矩阵注册服务器端口号不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getRegisterServerId())) {
        	validMsg = "数字矩阵注册服务器ID不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getRegisterServerDomain())) {
        	validMsg = "数字矩阵注册服务器域名不能为空！";
        }
        if(StrUtil.isBlank(matrixVo.getDepartmentId())) {
            validMsg = "所属部门不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        matrixService.saveMatrix(userVo, matrixVo);
        resp.ok("保存成功！");

        return resp;
	}
	//删除矩阵
	@ApiOperation(value = "删除矩阵")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        UserVo userVo = getLoginUser();
        List<String> idList = Arrays.asList(ids);
        matrixService.delMatrixsByIds(userVo,idList);
        apiRes.ok("删除成功！");

		return apiRes;
	}

	private boolean isContains(List<TreeNodeDepartment> A,List<TreeNodeDepartment> B){
		List<String> a_Ids = new ArrayList<>();
		for(TreeNodeDepartment item : A){
			a_Ids.add(item.getId());
		}

		List<String> b_Ids = new ArrayList<>();
		for(TreeNodeDepartment item : B){
			b_Ids.add(item.getId());
		}

		return a_Ids.containsAll(b_Ids);
	}
}
