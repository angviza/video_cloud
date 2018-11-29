package com.hdvon.nmp.controller.system;


import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.face.FaceConstant;
import com.hdvon.face.service.FaceService;
import com.hdvon.face.vo.HDFaceInfoVo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.config.WebAppConfig;
import com.hdvon.nmp.config.kafka.KafkaMsgProducer;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.dto.UserDto;
import com.hdvon.nmp.enums.EOperationType;
import com.hdvon.nmp.enums.EYesNo;
import com.hdvon.nmp.service.*;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.util.*;
import com.hdvon.nmp.vo.*;
import com.hdvon.sip.video.service.IVideoSipService;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Api(value="/user",tags="用户管理模块",description="")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@Reference
    private IUserService userService;
    @Reference
    private IResourceroleService resourceroleService;
	@Reference
    private ISysroleService sysroleService;
	@Reference
    private ISysmenuService sysmenuService;
	@Autowired
    private RedisHelper redisHelper;
	@Reference
	private IUserSysmenuService userSysmenuService;
	@Reference
    private ILimitLoginService limitLoginService;

	@Autowired
    private WebAppConfig webAppConfig;
	 
	@Reference
	private IUserPictureService userPictureService;
	
	@Reference
	private IDictionaryService dictionaryService;
	
	@Reference
	private FaceService faceService;
	
	@Reference
	private IUserLogService userLogService;
	/*
	@Reference
	private ISyncIndexDataService syncIndexDataService;
	*/
	@Reference
	private ICameraLogService cameraLogService;
	@Reference
	private ITreeNodeService treeNodeService;
	
	@Reference
	private IVideoSipService videoSipService;
	
	@Autowired
	private KafkaMsgProducer kafkaMsgProducer;
	

    @ApiOperation(value="用户登录", notes = "展示首页信息")
    @PostMapping(value = "/login")
    public ApiResponse<LoginUserVo> login(String username , String password) {
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return new ApiResponse().error("账号密码不允许为空");
        }
//        password = WebConstant.getPasswordByMd5(password);
        //用户登录
        UserVo userVo = userService.login(username,password);


        Integer minutes = redisHelper.checkUserLimit(userVo.getId());
        if(minutes != null){
            return new ApiResponse().error("抱歉，按管理员要求，当前账号" + minutes + "分钟内限制登录本系统");
        }
        
        //检验ip和mac地址是否符合登录条件
        if(!userVo.isAdmin()){
        	String error= checkLogin(userVo);
            if(StrUtil.isNotBlank(error)) {
            	return new ApiResponse().error(error);
            }
        }
        
        String tokenid = JwtUtil.createToken(userVo);

        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setTokenid(tokenid);
        loginUserVo.setUserId(userVo.getId());
        loginUserVo.setAccount(userVo.getAccount());
        loginUserVo.setName(userVo.getName());
        loginUserVo.setAdmin(userVo.isAdmin());
        loginUserVo.setLevel(userVo.getLevel());
        loginUserVo.setDepartmentId(userVo.getDepartmentId());
        loginUserVo.setDepartmentName(userVo.getDepartmentName());


        //设置用户菜单权限
        List<SysmenuVo> menuList = sysmenuService.getMenuVoByUserId(userVo);
        loginUserVo.setMenuList(menuList);

        //设置用户菜单map
        List<String> menuStrList = new ArrayList<>();
        for (SysmenuVo menu : menuList) {
            if(menu.getType() != null && menu.getType().intValue() == 2 ){
                String url = menu.getUrl();
                if(StrUtil.isNotBlank(url)) {
	            	 if(url.lastIndexOf("/") != url.length() -1){
	                     url += "/";
	                 }
                }
                menuStrList.add(url+"_"+menu.getMethod());
            }
        }
        userVo.setMenuPermission(String.join(",",menuStrList));
        //设置缓存
        redisHelper.setUserByToken(tokenid , userVo);
//        redisHelper.setCurrentToken(userVo.getId(),tokenid);
        saveLog(userVo,tokenid);
        return new ApiResponse().ok("登录成功").setData(loginUserVo);
    }


	/**
	 * 人脸识别
	 * @param base64Data
     * @param userAccount
	 * @return
	 */
    @ApiOperation(value="人脸识别登录")
    @PostMapping(value="/faceLogin")
    public ApiResponse<LoginUserVo> faceLogin(@RequestParam String base64Data,@RequestParam String userAccount) {
         try {
             //用户登录
             List<UserVo> userList = userService.getUserByAccount(userAccount);
             if(userList == null || userList.size()==0) {
            	 return new ApiResponse().error("用户不存在");
             }
             //获取当前用户的特征值
             UserVo userVo = userList.get(0);
             UserPictureVo userPictVo = userPictureService.queryRecord(userVo.getId());
             if(userPictVo == null) {
            	 return new ApiResponse().error("图片库不存在头像，请先上传头像");
             }
             
             //获取图片的特征值
             HDFaceInfoVo feat1 = faceService.getFaceFeatureBase64(base64Data, FaceConstant.cpuType);
             if(feat1 == null) {
              	return new ApiResponse().error("头像无法识别,请重新识别！");
              }
             List<Float> featList = JSON.parseArray(userPictVo.getFeatures(), Float.class);
             float[] feat2 = new float[featList.size()];
             for(int i=0;i<featList.size();i++) {
            	 feat2[i] = featList.get(i);
             }
             float similarity = faceService.getSimilarity(feat1.feature, feat2);

             Float faceSimilarity = webAppConfig.getFaceSimilarity();
             
             if(similarity < faceSimilarity) {
            	 return new ApiResponse().error("头像不匹配,请重新识别！");
             }
             Integer minutes = redisHelper.checkUserLimit(userVo.getId());
             if(minutes != null){
                 return new ApiResponse().error("抱歉，按管理员要求，当前账号" + minutes + "分钟内限制登录本系统");
             }
             
             //检验ip和mac地址是否符合登录条件(管理员返回所有)
             if(! userVo.isAdmin()){
             	String error= checkLogin(userVo);
                 if(StrUtil.isNotBlank(error)) {
                 	return new ApiResponse().error(error);
                 }
             }
             
             String tokenid = JwtUtil.createToken(userVo);

             LoginUserVo loginUserVo = new LoginUserVo();
             loginUserVo.setTokenid(tokenid);
             loginUserVo.setUserId(userVo.getId());
             loginUserVo.setAccount(userVo.getAccount());
             loginUserVo.setName(userVo.getName());
             loginUserVo.setAdmin(userVo.isAdmin());
             loginUserVo.setLevel(userVo.getLevel());
             loginUserVo.setDepartmentId(userVo.getDepartmentId());
             loginUserVo.setDepartmentName(userVo.getDepartmentName());

             //设置用户菜单权限
             List<SysmenuVo> menuList = sysmenuService.getMenuVoByUserId(userVo);
             loginUserVo.setMenuList(menuList);

             //设置用户菜单map
             List<String> menuStrList = new ArrayList<>();
             for (SysmenuVo menu : menuList) {
                 if(menu.getType() != null && menu.getType().intValue() == 2 ){
                     String url = menu.getUrl();
                     if(url.lastIndexOf("/") != url.length() -1){
                         url += "/";
                     }
                     menuStrList.add(url+"_"+menu.getMethod());
                 }
             }
             userVo.setMenuPermission(String.join(",",menuStrList));

             //设置缓存
             redisHelper.setUserByToken(tokenid , userVo);

             saveLog(userVo,tokenid);
             return new ApiResponse().ok("登录成功").setData(loginUserVo);

         } catch (Exception e) {
             return new ApiResponse().error("用户上传头像异常！");
         }

    }    
    
	@ApiOperation(value="用户退出")
    @ApiImplicitParam(name = "token", value = "token", required = true)
    @PostMapping(value = "/logout")
    public ApiResponse<LoginUserVo> logout() {
		String token = request.getHeader(WebConstant.TOKEN_HEADER);
		UserVo userVo = (UserVo) redisHelper.getUserByToken(token);
        if(StrUtil.isNotBlank(this.token)){
            redisTemplate.delete(this.token);
        }
        // 更新用户 登录日志
    	if(userVo ==null) {
    		return null;
    	}
    	//处理未正常关闭的视频流
//    	Map<String,String> param = new HashMap<String,String>();
//    	param.put("userId", userVo.getId());
//    	param.put("userIp", ClientUtil.getClientIp(request));
//    	
//    	List<CameraLogVo> list=cameraLogService.deleteByParam(param);
        return new ApiResponse().ok("用户退出成功");
    }

    @ApiOperation(value="分页查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentId", value = "部门id", required = false),
            @ApiImplicitParam(name = "search", value = "模糊查询", required = false)
    })
    @GetMapping(value = "/queryUserPage")
    public ApiResponse<PageInfo<UserVo>> queryUserPage(PageParam pp, String deptCode, UserVo userVo){
    	ApiResponse<PageInfo<UserVo>> resp = new ApiResponse<PageInfo<UserVo>>();
    	
    	List<TreeNodeDepartment> deptNodes =  null;
    	TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
    	
    	UserDto userDto = new UserDto();
        if(StrUtil.isNotBlank(userVo.getDepartmentId())) {
        	if(StrUtil.isBlank(deptCode)) {
        		return resp.error("部门编号不能为空！");
        	}
        	deptNodes =  treeNodeService.getDeptChildNodesByCode(deptCode, TreeType.DEPARTMENT.getVal());
        	treeNodeChildren.setDeptNodes(deptNodes);
        }
        userDto.setAccount(userVo.getAccount());
        userDto.setName(userVo.getName());
        PageInfo<UserVo> pageInfo = userService.getUsersByPage(pp, treeNodeChildren, userDto);
        resp.ok().setData(pageInfo);
    	return resp;
    }

    @ApiOperation(value="查询用户详细信息")
    @GetMapping(value = "/queryUserInfo")
    public ApiResponse<UserVo> queryUserInfo(String id){
    	ApiResponse<UserVo> resp = new ApiResponse<>();
    	if(StrUtil.isBlank(id)){
            return resp.error("用户id不允许为空！");
        }
        UserVo userVo = userService.getUserInfoById(id);
        if(userVo == null){
            return resp.error("找不到用户信息！");
        }
        return resp.ok().setData(userVo);
    }

    @ApiOperation(value="保存用户")
    @PostMapping(value = "/saveUser")
    public ApiResponse<Object> saveUser(UserParamVo userVo){
    	UserVo loginUser = getLoginUser();
    	ApiResponse<Object> resp = new ApiResponse<Object>();

    	if(StrUtil.isBlank(userVo.getName())) {
            return resp.error("用户名称不能为空！");
        }

        if(StrUtil.isBlank(userVo.getId())){//创建用户的时候，需要输入密码及账号
            if(StrUtil.isBlank(userVo.getAccount())) {
                return resp.error("用户账号不能为空！");
            }
            if(StrUtil.isBlank(userVo.getPassword()) || StrUtil.isBlank(userVo.getConfirmPassword())) {
                return resp.error("用户密码不能为空！");
            }
            if(!userVo.getPassword().equals(userVo.getConfirmPassword())) {
                return resp.error("用户密码和确认密码不一致！");
            }
            userVo.setPassword(WebConstant.getPasswordByMd5(userVo.getPassword()));
            userVo.setAccount(userVo.getAccount().trim());
        }else{//修改用户信息的时候，不允许修改密码及账号
            userVo.setPassword(null);
            userVo.setAccount(null);
        }

        if(StrUtil.isBlank(userVo.getDepartmentId())){
            return resp.error("所属部门不允许为空！");
        }

        userService.saveUser(loginUser, userVo);
        return resp.ok("保存成功！");
    }

    @ApiOperation(value="更新用户状态")
    @PostMapping(value = "/updateUserStatus")
    public ApiResponse<Object> updateUserStatus(UserVo userVo){
        UserVo loginUser = getLoginUser();
        ApiResponse<Object> resp = new ApiResponse<Object>();

        if(StrUtil.isBlank(userVo.getId())) {
            return resp.error("用户id不能为空！");
        }
        if(userVo.getEnable() == null) {
            return resp.error("用户状态不能为空！");
        }
        userService.updateUserStatus(loginUser, userVo);
        return resp.ok("保存成功！");
    }

    @ApiOperation(value="修改密码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户id", required = true),
        @ApiImplicitParam(name = "password", value = "密码", required = true),
        @ApiImplicitParam(name = "confirmPassword", value = "确认密码", required = true),
    })
    @PostMapping(value = "/modifyPassword")
    public ApiResponse<Object> modifyPassword(String id , String password , String confirmPassword){
    	ApiResponse<Object> resp = new ApiResponse<Object>();
        if(StrUtil.isBlank(id)) {
            return resp.error("用户id不能为空！");
        }
        if(StrUtil.isBlank(password)||StrUtil.isBlank(confirmPassword) ) {
            return resp.error("密码不能为空！");
        }
        if(!password.equals(confirmPassword)) {
            return resp.error("用户密码和确认密码不一致！");
        }

        password = WebConstant.getPasswordByMd5(password);
        userService.updateUserPassword(id , password);
        return resp.ok("修改成功");
    }

    @ApiOperation(value="删除用户")
    @DeleteMapping(value = "/delUser")
    public ApiResponse<Object> delUser(@RequestParam(value="ids[]") String[] ids){
    	ApiResponse<Object> resp = new ApiResponse<Object>();
    	UserVo loginUser = getLoginUser();
        List<String> idList = Arrays.asList(ids);
        userService.delUsers(loginUser,idList);
        resp.ok("删除成功！");
    	return resp;
    }
    
    @ApiOperation(value="查询用户资源角色")
    @GetMapping(value = "/queryResourcerolesByUser")
    public ApiResponse<List<ResourceroleVo>> queryResourcerolesByUser(String userIds) {
    	ApiResponse<List<ResourceroleVo>> resp = new ApiResponse<List<ResourceroleVo>>();
        String[] userIdArr = userIds.split(",");
        List<String> userIdList = Arrays.asList(userIdArr);
        List<ResourceroleVo> resourceRoles = userService.getResourceRolesByUserId(userIdList);
        resp.ok().setData(resourceRoles);
        return resp;
    }

    @ApiOperation(value="查询用户系统角色")
    @GetMapping(value = "/querySysrolesByUser")
    public ApiResponse<List<SysroleVo>> querySysrolesByUser(String userIds) {
    	ApiResponse<List<SysroleVo>> resp = new ApiResponse<>();
        String[] userIdArr = userIds.split(",");
        List<String> userIdList = Arrays.asList(userIdArr);
        List<SysroleVo> roleList =  userService.getSysrolesByUserId(userIdList);
        resp.ok().setData(roleList);
		return resp;
    }

    @ApiOperation(value="授予用户资源角色")
    @PostMapping(value = "/grantResourcerolesToUser")
    @ApiImplicitParam(name = "ids", value = "资源角色ids")
    public ApiResponse<List<UserVo>> grantResourcerolesToUser(String userIds, String ids) {
        UserVo loginUser = getLoginUser();
    	ApiResponse<List<UserVo>> resp = new ApiResponse<List<UserVo>>();
        if(StrUtil.isBlank(userIds)){
            resp.error("用户id不允许为空！");
        }
        if(StrUtil.isBlank(ids)){
            ids = "";
        }
        String[] userIdArr = userIds.split(",");
        List<String> userIdList = Arrays.asList(userIdArr);
        String[] idsArr = ids.split(",");
        List<String> resourceRoles = Arrays.asList(idsArr);
        // 根据用ids查找 资源角色
        Map<String,Object> param =new HashMap<String,Object>();
        param.put("userId", userIdList);
        //userids 以前的资源角色
        List<ResourceroleVo> resRoles = resourceroleService.getResourceByParam(param);
        // 先保存再发送消息，不然数据存在误差
        userService.grantResourceRole(loginUser,userIdList, resourceRoles);
        /*
         * 用户授权 同步到ES,用户管理页面授权资源
         */
        for(String userId :userIdList) {
        	String deleteIds="";
        	 for(ResourceroleVo vo :resRoles) {
        		 if(vo.getUserId().equals(userId)) {
        			 deleteIds+=vo.getId()+",";
        		 }
             }
            CameraMappingMsg msg=new CameraMappingMsg();
    	  	msg.setId(IdGenerator.nextId());
    	  	msg.setUpdateIds(ids);//更新的资源角色
    	  	msg.setDeleteIds(StrUtil.isNotBlank(deleteIds)? deleteIds.substring(0, deleteIds.length()-1): null);// 删除的资源角色
    	  	msg.setRelationId(userId);//用户id
    	  	msg.setType(4);
    	  	
    	  	kafkaMsgProducer.sendCameraMapping(msg);
        } 
        
       	
        resp.ok("授予资源角色成功！");
		return resp;
    }

    //授予用户系统角色
    @ApiOperation(value="授予用户系统角色")
    @PostMapping(value = "/grantSysrolesToUser")
    @ApiImplicitParam(name = "ids", value = "资源角色id列表")
    public ApiResponse<List<UserVo>> grantSysrolesToUser(String userIds, String ids) {
    	ApiResponse<List<UserVo>> resp = new ApiResponse<List<UserVo>>();
        UserVo loginUser = getLoginUser();
        if(StrUtil.isBlank(userIds)){
            resp.error("用户id不允许为空！");
        }
        if(StrUtil.isBlank(ids)){
            ids = "";
        }
        String[] userIdArr = userIds.split(",");
        List<String> userIdList = Arrays.asList(userIdArr);
        String[] idsArr = ids.split(",");
        List<String> sysRoles = Arrays.asList(idsArr);
        userService.grantSysRole(loginUser,userIdList, sysRoles);
        return resp.ok("授予系统角色成功！");
    }

    /**
     * 查询自定义菜单列表
     * @param
     * @return
     */
    @ApiOperation(value="查询自定义菜单列表", notes = "首页的自定义菜单列表")
    @GetMapping(value = "/findOptionMenu")
    public ApiResponse<List<SysmenuVo>> findOptionMenu() {
    	UserVo user = getLoginUser();
        //用户菜单权限
        List<SysmenuVo> menuList = sysmenuService.getCustomerMenu(user);
        return new ApiResponse<List<SysmenuVo>>().ok("查询自定义菜单列表成功").setData(menuList);
    }

    /**
     * 修改自定义菜单项。
     * @param type {@link EOperationType}
     * @param userId
     * @param pid 恢复操作时非必输。其它操作必输。
     * @param menuId 恢复操作时非必输。其它操作必输。
     * @param menuName 恢复操作时非必输。其它操作必输。
     * @return
     */
    @ApiOperation(value="修改自定义菜单项", notes = "修改自定义菜单项")
    @PostMapping(value = "/editOptionMenu")
    public ApiResponse<Object> editOptionMenu(
    		@RequestParam(required = true) @ApiParam(value = "操作类型(add：增加。edit：修改名称。del：删除。renew：恢复。)", allowableValues = "add, edit, del, renew")
    			String type,
    		@RequestParam(required = true) @ApiParam(value = "用户id")
    			String userId,
    		@RequestParam(required = false) @ApiParam(value = "父菜单id(恢复操作时非必输。其它操作必输)")
    			String pid,
    		@RequestParam(required = false) @ApiParam(value = "子菜单id(恢复操作时非必输。其它操作必输)")
    			String menuId,
    		@RequestParam(required = false) @ApiParam(value = "子菜单名称(恢复操作时非必输。其它操作必输)")
    			String menuName) {
    	ApiResponse<Object> resp = new ApiResponse<>();
    	if (StrUtil.isEmpty(userId)) {
            return resp.error("用户id不允许为空！");
        } // if
    	if ((! EOperationType.RENEW.getVal().equals(type)) && (
	    			StrUtil.isEmpty(pid) ||
	    			StrUtil.isEmpty(menuId) ||
	    			StrUtil.isEmpty(menuName)
    			)) {
            return resp.error("非恢复操作时，父菜单id、子菜单id、子菜单名称均不允许为空！");
        } // if
    	int effect = 0;
    	boolean succ = false;
    	UserSysmenuVo vo = new UserSysmenuVo();
    	vo.setSysmenuId(menuId);
    	vo.setSysmenuName(menuName);
    	vo.setSysmenuPid(pid);
    	vo.setUserId(userId);
    	// 若是增加。
    	if (EOperationType.ADD.getVal().equals(type)) {
    		vo.setHide(EYesNo.NO.getVal());
    		if (this.userSysmenuService.existByUseridAndMenuid(userId, menuId)) {
    			effect = this.userSysmenuService.modify(vo);
    		}
    		else {
    			effect = this.userSysmenuService.save(vo);
    		}
    		succ = (effect > 0);
    	}
    	// 若是删除。
    	else if (EOperationType.DEL.getVal().equals(type)) {
    		vo.setHide(EYesNo.YES.getVal());
    		if (this.userSysmenuService.existByUseridAndMenuid(userId, menuId)) {
    			effect = this.userSysmenuService.modify(vo);
    		}
    		succ = true;
    	}
    	// 若是修改。
    	else if (EOperationType.EDIT.getVal().equals(type)) {
    		vo.setHide(EYesNo.NO.getVal());
    		if (this.userSysmenuService.existByUseridAndMenuid(userId, menuId)) {
    			effect = this.userSysmenuService.modify(vo);
    		}
    		else {
    			effect = this.userSysmenuService.save(vo);
    		}
    		succ = (effect > 0);
    	}
    	// 若是恢复。
    	else if (EOperationType.RENEW.getVal().equals(type)) {
    		effect = this.userSysmenuService.deleteByUserid(userId);
    		succ = true;
    	}
        return succ?
        		new ApiResponse<>().ok("操作自定义菜单项成功"):
        			new ApiResponse<>().ok("操作自定义菜单项失败");
    }

    /**
     * 在自定义菜单区域内的拖动
     */
    @ApiOperation(value="在自定义菜单区域内的拖动", notes = "在自定义菜单区域内的拖动")
    @PostMapping(value = "/dragOptionMenu")
    public ApiResponse<Object> dragOptionMenu(
    		@RequestParam(required = true) @ApiParam(value = "用户id")
    			String userId,
    		@RequestParam(required = true) @ApiParam(value = "父菜单id")
    			String pid,
    		@RequestBody
    			List<OptMenuVo> list) {
    	ApiResponse<Object> resp = new ApiResponse<>();
    	if (StrUtil.isEmpty(userId) ||
    			StrUtil.isEmpty(pid) ||
    			(list == null) ||
    			list.isEmpty()) {
	        return resp.error("用户id、父菜单id、子菜单列表均不允许为空！");
	    } // if
    	this.sysmenuService.drag(userId, pid, list);
        return new ApiResponse<>().ok("操作自定义菜单项成功");
    }
    
    
    
    @ApiOperation(value="用户添加可管理部门")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userIds", value = "用户ids",required=true),
    	@ApiImplicitParam(name = "departmentIds", value = "可部门ids")
    })
    @PostMapping(value = "/departmentToUser")
    public ApiResponse<List<UserVo>> departmentToUser(String userIds, String departmentIds) {
    	ApiResponse<List<UserVo>> resp = new ApiResponse<List<UserVo>>();
        UserVo loginUser = getLoginUser();
    	if(StrUtil.isBlank(userIds)){
            userIds = "";
        }
        String[] userIdArr = userIds.split(",");
        String[] ids = departmentIds.split(",");
        List<String> userIdList = Arrays.asList(userIdArr);
        userService.departmentToUser(loginUser,userIdList,ids);
        resp.ok();
		return resp;
    }
    
    
    @ApiOperation(value="获取用户已关联的可管理部门")
    @ApiImplicitParam(name = "userId", value = "用户id",required=true)
    @GetMapping(value = "/getDepartmentData")
    public ApiResponse<List<DepartmentVo>> getDepartmentUser(String userId) {
    	ApiResponse<List<DepartmentVo>> resp = new ApiResponse<>();
    	if(StrUtil.isBlank(userId)){
            return resp.error("用户id不允许为空！");
        }
        List<DepartmentVo> list = userService.getDepartmentUser(userId);
    	return resp.ok().setData(list);
    }
    
    
    /**
     * 校验用户限制登录条件
     * @param userVo
     * @return
     */
    private String checkLogin(UserVo userVo) {
    	 String mac=ClientUtil.getClientMac();
    	//校验用户mac和当前登录的mac是否匹配
    	if(StrUtil.isNotBlank(userVo.getMac())) {
    	   if(! mac.equals(userVo.getMac())) {
    		  return "您的 mac地址与用户名不匹配，请在您本机电脑登录或联系管理员调整MAC限制";
    	   }
    	}
    	//是否是限制mac
    	Map<String ,Object> param =new HashMap<String ,Object>();
    	param.put("state", "1");//获取开启状态
    	param.put("type", "2");//是mac地址
    	param.put("macName", mac);
    	int count= limitLoginService.seletCount(param);
    	if(count >0 ) {
    		return "您本机MAC被限制登录,请联系管理员调整MAC限制";
    	}
    	
    	String localIp= ClientUtil.getClientIp(request);
    	//String localIp="192.168.211.106";
    	long local=Long.parseLong(localIp.replace(".", ""));
    	Map<String ,Object> paramIp =new HashMap<String ,Object>();
    	paramIp.put("state", "1");//获取开启状态
    	paramIp.put("type", "1");//允许ip
    	int countIp=limitLoginService.seletCount(paramIp);
    	//通过自己ip最大匹配：截取最后.前面的字符串
    	paramIp.put("searchIp", localIp.substring(0, localIp.lastIndexOf(".")+1));
    	boolean isExits=false;//是否在允许登录区间
    	
    	if(countIp < 1) {
    		//没有设置和启用“允许的IP”，则除“限制的IP”、“限制的MAC”外，其他IP皆允许访问
    		paramIp.put("type", "0");//限制的IP
    		List<LimitLoginVo> limits=limitLoginService.getLimitLoginList(paramIp);
    		for(LimitLoginVo vo:limits) {
    			//非区间ip
    			if(vo.getIsRegasion().equals("0")) {
    				if(vo.getStartRegaion().equals(localIp)) {
    					return "您本机IP被限制登录,请联系管理员调整IP限制";
    				}
    			}else {
    				//区间ip
    				String startRegaion=vo.getStartRegaion().replace(".", "");
    				String endRegaion=vo.getEndRegaion().replace(".", "");
    				long min =Long.parseLong(startRegaion);
    				long max=Long.parseLong(endRegaion);
    				if((local >= min) && (local <= max)) {
    					return "您本机IP在限制登录区间,请联系管理员调整IP限制";
    				}
    			}
    		}
    		
    	}else {
    		//设置并启用“允许的IP”，则只允许出现在“允许的IP”中的、且未出现在“限制的IP”、“限制的MAC”中的IP访问，其他IP皆禁止访问
    		paramIp.remove("type");
    		paramIp.put("removeMac", "1");//排除mac地址
    		List<LimitLoginVo> limitAll=limitLoginService.getLimitLoginList(paramIp);
    		for(LimitLoginVo vo :limitAll) {
    			String type =vo.getType();
    			//非区间ip
    			if(vo.getIsRegasion().equals("0")) {
    				//是限制ip
    				if(type.equals("0") ) {
    					if(vo.getStartRegaion().equals(localIp)) {
    						return "您本机IP被限制登录,请联系管理员调整IP限制";
    					}
    				}else {
    					//只要满足允许ip一个条件即可
    					if( vo.getStartRegaion().equals(localIp)) {
    						return null;
    					}
    				    isExits=true;
    				}
    			}else {
    				//区间ip
    				String startRegaion=vo.getStartRegaion().replace(".", "");
    				String endRegaion=vo.getEndRegaion().replace(".", "");
    				long min =Long.parseLong(startRegaion);
    				long max=Long.parseLong(endRegaion);
    				//限制ip
    				if(type.equals("0") ) {
    					if((local >= min) && (local <= max)) {
        					return "您本机IP在限制登录区间,请联系管理员调整IP限制";
        				}
    				}else {
    					//只要满足允许登录的ip段，都可以正常登录
    					if((local >= min) && (local <= max)) {
    						//isExits=false;
    						return null;
        				}
    				}
    			   isExits=true;
    			}
    		}
    	}
    	if(isExits) {
    		return "您本机IP在不在登录区间,请联系管理员调整IP限制";
    	}
		return null;
	}

	/**
	 * 上传用户头像
	 * @param base64Data
	 * @return
	 */
    @ApiOperation(value="上传用户头像")
	@PostMapping(value="/uploadHeadPortrait")
    @ApiImplicitParam(name = "base64Data", value = "base64图片",required=true)
	@ResponseBody
	public ApiResponse<String> uploadHeadPortrait(@RequestParam String base64Data)  {
    	 ApiResponse<String> resp = new ApiResponse<String>();
     
         try{
             //使用apache提供的工具类操作流
             UserVo user = getLoginUser();
             //获取图片的特征值
             HDFaceInfoVo feat = faceService.getFaceFeatureBase64(base64Data, FaceConstant.cpuType);
             if(feat==null) {
             	return new ApiResponse().error("头像无法识别,请重新上传头像！");
             }
             UserPictureVo vo = new UserPictureVo();
             vo.setUserId(user.getId());
             vo.setFeatures(JSON.toJSONString(feat.feature));
             
             userPictureService.save(vo);
             
             resp.ok().setData(base64Data);
             return resp;
         }catch(Exception ee){
        	 return resp.error("用户上传头像异常！");
         }	           	

     }    
    
    
    //授予用户系统角色
    @ApiOperation(value="用户锁屏")
    @PostMapping(value = "/lockScreen")
    public ApiResponse<String> lockScreen() {
    	ApiResponse<String> resp = new ApiResponse<String>();
    	
        UserVo user = getLoginUser();
        boolean flag = userService.lockScreen(user.getId(), this.token);
        if(!flag) {
        	return new ApiResponse().error("锁屏失败!");
        }
        resp.ok().setData("锁屏成功！");
        
    	return resp;
    }
    
    //授予用户系统角色
    @ApiOperation(value="用户密码解锁")
    @PostMapping(value = "/unLockScreenPasswd")
    @ApiImplicitParam(name = "passwd", value = "用户密码")
    public ApiResponse<String> unLockScreenpassword(String passwd) {
    	ApiResponse<String> resp = new ApiResponse<String>();

        if(StrUtil.isBlank(passwd)){
            return new ApiResponse().error("解锁失败，密码不允许为空");
        }
        passwd = WebConstant.getPasswordByMd5(passwd);
        //用户登录
        UserVo userVo = getLoginUser();
        UserVo userVo1 = userService.login(userVo.getAccount(),passwd);
        if(userVo1 == null) {
        	return new ApiResponse().error("解锁失败，密码错误！");
        }
        boolean flag = userService.unLockScreen(userVo.getId());
        if(!flag) {
        	return new ApiResponse().error("解锁失败！");
        }
        resp.ok().setData("解锁成功！");
    	return resp;
    }
    
    
  //授予用户系统角色
    @ApiOperation(value="用户人脸解锁")
    @PostMapping(value = "/unLockScreenFace")
    @ApiImplicitParam(name = "base64Data", value = "base64图片",required=true)
    public ApiResponse<String> unLockScreenFace(@RequestParam String base64Data) {
    	ApiResponse<String> resp = new ApiResponse<String>();
        
        //获取当前用户的特征值
        UserVo userVo = getLoginUser();

        UserPictureVo userPictVo = userPictureService.queryRecord(userVo.getId());
        if(userPictVo == null) {
        	return new ApiResponse().error("解锁失败，图片库不存在头像，请先上传头像");
        }
        try {
       	 
        	HDFaceInfoVo feat1 = faceService.getFaceFeatureBase64(base64Data,FaceConstant.cpuType);
            if(feat1==null) {
            	return new ApiResponse().error("解锁失败，头像不匹配,请重新识别！");
            }
            List<Float> featList = JSON.parseArray(userPictVo.getFeatures(), Float.class);
            float[] feat2 = new float[featList.size()];
            for(int i=0;i<featList.size();i++) {
           	 feat2[i] = featList.get(i);
            }
            float similarity = faceService.getSimilarity(feat1.feature, feat2);

            Float faceSimilarity = webAppConfig.getFaceSimilarity();
            
            if(similarity < faceSimilarity) {
           	 	return new ApiResponse().error("解锁失败，头像不匹配,请重新识别！");
            }
            

        } catch (Exception e) {
            return new ApiResponse().error("用户头像解锁异常！");
        }
        boolean flag = userService.unLockScreen(userVo.getId());
        if(!flag) {
        	return new ApiResponse().error("解锁失败！");
        }
        resp.ok().setData("解锁成功！");
    	return resp;
    }
    
	@ControllerLog
	@ApiOperation(value="保存用户验证类型设置")
	@PostMapping(value = "/saveValidType")
    @ApiImplicitParam(name = "validType", value = "用户验证类型id", required = true)
    public ApiResponse<String> saveValidType(@RequestParam(value = "validType",required=true)Integer validType) {
		UserVo user = getLoginUser();
		
		userService.saveUserValidType(validType, user);
		
		return new ApiResponse().ok().setData("保存成功！");
    }
	
	@ApiOperation(value="获取用户验证类型列表")
	@GetMapping(value = "/getValidTypeList")    
    public ApiResponse<List<ValidTypeVo>> getValidTypeList() {
		UserVo user = getLoginUser();
		
		//根据用户信息去查询用户
		UserVo userVo = userService.getUserInfoById(user.getId());
		Integer validType = userVo.getValidType();

		String[] searchType = {"YHYZFS"};
        List<DictionaryVo> list = dictionaryService.getDictionaryList(searchType);
		if(list.isEmpty() || list.size() ==0) {
			return new ApiResponse().ok().setData(Collections.emptyList());
		}
		List<ValidTypeVo> result = new ArrayList<>();
		list.stream().forEach(dict->{
					ValidTypeVo vo = new ValidTypeVo();
					vo.setChName(dict.getChName());
					vo.setValue(dict.getValue());
					
					Integer type = Integer.parseInt(dict.getValue());
					boolean isSelect = type.equals(validType) ? true : false;
					vo.setSelected(isSelect);
					result.add(vo);
				});
		return new ApiResponse().ok().setData(result);
    }

	@ApiOperation(value="获取用户设置验证类型")
	@GetMapping(value = "/settingValidType")    
    public ApiResponse<Map<String, Object>> settingValidType() {
		UserVo user = getLoginUser();
		
		//根据用户信息去查询用户
		UserVo userVo = userService.getUserInfoById(user.getId());
		Integer validType = userVo.getValidType();
		
		ValidTypeVo vo = new ValidTypeVo();
		String[] searchType = {"YHYZFS"};
        List<DictionaryVo> list = dictionaryService.getDictionaryList(searchType);
		if(validType!=null || !list.isEmpty()) {
			list.stream().filter(dict->dict.getValue().equals(String.valueOf(validType)))
			.forEach(dict->{
				vo.setChName(dict.getChName());
				vo.setValue(dict.getValue());
				vo.setSelected(true);
			});
		}
		//判断用户是否设置人脸识别
		UserPictureVo pictureVo = userPictureService.queryRecord(user.getId());
		boolean isFace = false;
		if(pictureVo != null) {
			isFace = true;
		}


		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("isFace", isFace);
		resultMap.put("validType", vo);
		return new ApiResponse().ok().setData(resultMap);
    }
	
	/**
	 * 前端定时任务 
	 * @return
	 */
	@ApiOperation(value="检测用户是否在线")
	@GetMapping(value = "/userState") 
	public ApiResponse<Object> userState() {  
    	String token = request.getHeader(WebConstant.TOKEN_HEADER);
    	UserVo userVo = (UserVo) redisHelper.getUserByToken(token);
    	if(userVo ==null) {
    		return new ApiResponse().ok();
    	}
		userService.updateByParam(userVo);
		return new ApiResponse().ok();
	 } 
	
	/**
	 * 用户登录日志
	 * @param userVo
	 * @param tokenid
	 */
	private void saveLog(UserVo userVo,String tokenid) {
		UserLogVo log = new UserLogVo();
        log.setAccount(userVo.getAccount());
        log.setName(userVo.getAccount());
        log.setTokenId(tokenid);
        log.setType(WebConstant.USER_CONTORL_5);//用户登录
        log.setOperationTime(new Date());
        Map<String, Object> param = new HashMap<>();
        List<SysmenuVo> menuList = sysmenuService.getMenuFunctionByParam(param);
    	// jdk8新特性
        List<SysmenuVo> list=menuList.stream().filter(vo-> vo.getUrl().contains("/user/") ).collect(Collectors.toList());
        if(list.size() >0) {
        	log.setMenuId(list.get(0).getPid());
        }
        log.setContent("用户登录系统");
        userLogService.saveUserLog(log);
		
		
	}
}
