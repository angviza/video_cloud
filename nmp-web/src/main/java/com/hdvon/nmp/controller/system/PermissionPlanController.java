package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.config.kafka.KafkaMsgProducer;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IPermissionPlanService;
import com.hdvon.nmp.service.IPermissionplanInfoElService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.PermissionPlanParamVo;
import com.hdvon.nmp.vo.PermissionPlanVo;
import com.hdvon.nmp.vo.ResourceroleVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="/permissionPlan",tags="权限预案模块",description="针对权限预案的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/permissionPlan")
public class PermissionPlanController  extends BaseController{
	
	@Reference
	private IPermissionPlanService permissionPlanService;
	
	@Reference
	private IPermissionplanInfoElService permissionPlanInfoElService;
	
	@Autowired
	private KafkaMsgProducer kafkaMsgProducer;
	
	//分页查询权限预案列表
	@ApiOperation(value="分页查询权限预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/page")
	public ApiResponse<PageInfo<PermissionPlanVo>> page(PageParam pp, PermissionPlanParamVo permissionPlanParamVo) {
		 ApiResponse<PageInfo<PermissionPlanVo>> resp = new ApiResponse<PageInfo<PermissionPlanVo>>();
         Map<String,Object> paramMap = new HashMap<String,Object>();
         paramMap.put("id", permissionPlanParamVo.getId());
         paramMap.put("name", StrUtil.isBlank(permissionPlanParamVo.getName())?null:permissionPlanParamVo.getName());
         paramMap.put("status", permissionPlanParamVo.getStatus());
         paramMap.put("userStatus", permissionPlanParamVo.getUserStatus());
         paramMap.put("type", permissionPlanParamVo.getType());
         PageInfo<PermissionPlanVo> pageInfo = permissionPlanService.getPermissionPlanPages(pp, paramMap);

         resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}
	
	//添加权限预案或者编辑权限预案初始化
	@ApiOperation(value = "查询权限预案信息")
	@GetMapping(value="/detail")
	public ApiResponse<PermissionPlanVo> detail(HttpServletRequest request, String id){
		 ApiResponse<PermissionPlanVo> resp = new ApiResponse<PermissionPlanVo>();
		 PermissionPlanVo permissionPlanVo = null;
         if(StrUtil.isNotBlank(id)) {
        	 permissionPlanVo = permissionPlanService.getPermissionPlanById(id);
         }
         resp.ok("查询成功！").setData(permissionPlanVo);
		 return resp;
	}
	
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存权限预案信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, PermissionPlanParamVo permissionPlanParamVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		permissionPlanParamVo.convertTime();
		String validMsg = "";
        if(StrUtil.isBlank(permissionPlanParamVo.getName())) {
            validMsg = "权限预案名称不能为空！";
        }
        if(permissionPlanParamVo.getBgnTime() == null) {
        	validMsg = "预案开始时间不能为空！";
        }
        if(permissionPlanParamVo.getEndTime() == null) {
        	validMsg = "预案结束时间不能为空！";
        }
        if(StrUtil.isBlank(validMsg)) {
        	if(permissionPlanParamVo.getEndTime().compareTo(permissionPlanParamVo.getBgnTime())<=0) {
            	validMsg = "预案结束时间必须大于开始时间！";
            }
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        permissionPlanService.savePermissionPlan(userVo, permissionPlanParamVo);
       
        // 更改状态,是新增授权发送信息
        if(StrUtil.isNotBlank(permissionPlanParamVo.getId()) && permissionPlanParamVo.getUserStatus()==9) {
        	 // 预案关联的用户
            List<UserVo> oldUser=permissionPlanService.getRelateUsersByPermissionPlanId(userVo,permissionPlanParamVo.getId());
            String userIds="";
            for(UserVo user:oldUser) {
            	userIds+=user.getId()+",";
            }
            CameraMappingMsg msg=new CameraMappingMsg();
        	msg.setId(IdGenerator.nextId());
 		   	msg.setRelationId(permissionPlanParamVo.getId());
 		   	msg.setType(2);
 		   	
        	if(permissionPlanParamVo.getStatus()==0) {// 禁用
        		msg.setUpdateIds("10000000000");//不存在用户id
        	}else {// 启用
        		msg.setUpdateIds(StrUtil.isNotBlank(userIds)? userIds.substring(0, userIds.length()-1):"10000000000");
        	}
 		   	kafkaMsgProducer.sendCameraMapping(msg);
        }
        
        resp.ok("保存成功！");

        return resp;
	}
	
	//删除权限预案
	@ApiOperation(value = "删除权限预案")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        permissionPlanService.delPermissionPlansByIds(idList);
      
        for(int i=0;i<ids.length;i++) {
            CameraMappingMsg msg=new CameraMappingMsg();
 		   	msg.setId(IdGenerator.nextId());
 			msg.setUpdateIds("10000000000");
 		   	msg.setRelationId(ids[i]);
 		   	msg.setType(2);
 		   	kafkaMsgProducer.sendCameraMapping(msg);
        }
        
        apiRes.ok("删除成功！");

		return apiRes;
	}
	
	//编辑权限预案关联资源角色界面初始化
	@ApiOperation(value = "查询权限预案关联的资源角色与用户列表")
	@GetMapping(value="/getResourceroles")
	public ApiResponse<PermissionPlanVo> getResourceroles(HttpServletRequest request, String permissionplanId){
		 UserVo userVo = getLoginUser();
		 List<ResourceroleVo> resourceroles = permissionPlanService.selectResourcerolesByPermissionPlanId(userVo, permissionplanId);
		 List<UserVo> users = permissionPlanService.getRelateUsersByPermissionPlanId(userVo, permissionplanId);
		 PermissionPlanVo permissionPlanVo = new PermissionPlanVo();
		 permissionPlanVo.setResourceRoles(resourceroles);
		 permissionPlanVo.setUserVos(users);
		 return new ApiResponse<PermissionPlanVo> ().ok().setData(permissionPlanVo);
	}
	

	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存权限预案关联资源角色信息")
	@PostMapping(value = "/saveUserResourceroles")
	public ApiResponse<Object> saveUserResourceroles(HttpServletRequest request, String permissionplanId, String resourceroleIds, String userIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		String validMsg = "";
        if(StrUtil.isBlank(permissionplanId)) {
            validMsg = "权限预案id不能为空！";
        }
        if(StrUtil.isBlank(userIds)) {
            validMsg = "权限预案必须选择用户！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        String[] resourceroleIdArr = resourceroleIds.split(",");
        List<String> resourceroleIdList = Arrays.asList(resourceroleIdArr);
        Map<String,Object> param= new HashMap<>();
        param.put("permissionplanId", permissionplanId);

        permissionPlanService.saveRelateResourceroles(permissionplanId, resourceroleIdList);//保存权限预案关联的资源角色

        String[] userIdArr = userIds.split(",");
        List<String> userIdList = Arrays.asList(userIdArr);
        
        permissionPlanService.saveRelateUsers(permissionplanId, userIdList);//保存权限预案关联的用户
        
        PermissionPlanVo vo= permissionPlanService.getPermissionPlanById(permissionplanId);
        if(vo !=null) {
        	//更新ES权限预案 新增授权才发送消息
            if(StrUtil.isNotBlank(vo.getId()) && vo.getUserStatus()==9) {
            	CameraMappingMsg msg=new CameraMappingMsg();
     		   	msg.setId(IdGenerator.nextId());
     		   	msg.setUpdateIds(StrUtil.isNotBlank(resourceroleIds)?userIds :"10000000000");
     		   	msg.setRelationId(permissionplanId);
     		   	msg.setType(2);
     		   
     		   	kafkaMsgProducer.sendCameraMapping(msg);
      		 
         	} 
        }
        
        resp.ok("保存成功！");
        return resp;
	}
}
