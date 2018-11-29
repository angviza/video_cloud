package com.hdvon.nmp.controller.system;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.dto.UserDto;
import com.hdvon.nmp.service.ICameraLogService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.service.IUserService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.RedisHelper;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.CameraParamVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.TreeNodeDepartment;
import com.hdvon.nmp.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="/online",tags="在线管理模块",description="")
@RestController
@RequestMapping("/online")
@Slf4j
public class OnlineController extends BaseController {

    @Reference
    private IUserService userService;
    @Autowired
    private RedisHelper redisHelper;
    @Reference
    private ICameraService cameraService;
    @Reference
    private ICameraLogService cameraLogService;
	@Reference
	private ITreeNodeService treeNodeService;

    @ApiOperation(value="分页查询在线摄像机列表")
    @GetMapping("/cameras")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sbmc", value = "设备名称", required = false),
            @ApiImplicitParam(name = "addressCode", value = "地址编码", required = false),
            @ApiImplicitParam(name = "sbbm", value = "设备编码", required = false),
            @ApiImplicitParam(name = "sbzt", value = "设备状态", required = false)
    })
    public ApiResponse<PageInfo<CameraVo>> cameras(PageParam pageParam, String sbmc ,
    		String addressCode, String sbbm , Integer sbzt) {
    	
    	TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
    	Map<String,Object> param = new HashMap<String,Object>();
        if(StrUtil.isNotBlank(addressCode)) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(addressCode, TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
			param.put("addressIds", treeNodeChildren.getAddressNodeIds());
		}
    	param.put("sbmc", sbmc);
    	param.put("sbbm", sbbm);
    	param.put("sbzt", sbzt);
    	PageInfo<CameraVo> page = cameraLogService.getCameraLogByPage(param, pageParam);
        return new ApiResponse().ok().setData(page);
    }

    @ApiOperation(value="分页查询在线用户列表")
    @GetMapping("/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentCode", value = "部门编码", required = false),
            @ApiImplicitParam(name = "search", value = "用户名/姓名", required = false),
            @ApiImplicitParam(name = "sbbm", value = "设备编码", required = false)
    })
    public ApiResponse<PageInfo<UserVo>> users(PageParam pp,String departmentCode, String search , String sbbm){
        UserVo loginUser = getLoginUser();
        Map<String,Object> param = new HashMap<String,Object>();
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
        if(StrUtil.isNotBlank(departmentCode)) {
			List<TreeNodeDepartment> deptNodes = treeNodeService.getDeptChildNodesByCode(departmentCode, TreeType.DEPARTMENT.getVal());
			if(deptNodes.size()>0) {
				treeNodeChildren.setDeptNodes(deptNodes);
				param.put("departmentIds", treeNodeChildren.getDeptNodeIds());
			}else {
				List<String> ids=new ArrayList<String>();
				ids.add("no");
				param.put("departmentIds",ids);// 没有结果匹配
			}
		}
        param.put("search", search);
        if(StrUtil.isNotBlank(sbbm)) {
        	param.put("sbbm", sbbm);
        }else {
        	param.put("timeOut", 9);//超时9分钟不更细状态默认离线
        }
        // 强制下线用户
        if(redisTemplate.hasKey(WebConstant.USER_LOGOUT_TOKEN)) {
        	 List<String> listToken=(List<String>) redisTemplate.opsForValue().get(WebConstant.USER_LOGOUT_TOKEN);
        	 param.put("listToken", listToken);
        }
        // 查询效率慢，后再优化
        PageInfo<UserVo> page = cameraLogService.getOnlineUsersByPage(pp, param);
        List<UserVo> list = page.getList();
        for (int i = 0; i < list.size(); i++) {
            UserVo userVo = list.get(i);
            userVo.setLevel(userVo.getLevel() == null ? 100 : userVo.getLevel());
            userVo.setAllowOff(loginUser.getLevel().intValue() < userVo.getLevel());//优先值越小，权限越高
            if(userVo.getLastLoginDate() != null){
                Date now = new Date();
                long ms = now.getTime() - userVo.getLastLoginDate().getTime();
                userVo.setLoginTime(formatTime(ms));
            }
            Long timeout = redisHelper.checkUserTimeout(userVo.getId());
            userVo.setLoginStatus("在线");
            if(timeout > 0L){
                userVo.setLoginStatus(timeout+"分钟内自动下线");
            }
        }
        return new ApiResponse().ok().setData(page);
    }

    @ApiOperation(value="注销指定用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "用户id,多个以逗号分隔", required = true),
            @ApiImplicitParam(name = "logoutTime", value = "自动下线时间，单位分钟", required = false),
            @ApiImplicitParam(name = "loginTime", value = "登录时间限制，单位分钟", required = false)
    })
    @PostMapping("/logoutUser")
    public ApiResponse<PageInfo<UserVo>> logoutUser(String userIds , Integer logoutTime , Integer loginTime){
        if(StrUtil.isNotBlank(userIds)){
            String[] ids = userIds.split(",");
            List<String> userIdList = Arrays.asList(ids);

            UserVo curUser = getLoginUser();
            curUser.setLevel(curUser.getLevel() == null ? 100 : curUser.getLevel());

            //检验注销用户的权限等级，只有优先级高才允许注销
            UserDto userDto = new UserDto();
            userDto.setUserIds(userIdList);
            List<String> newUserIdList = new ArrayList<>();
            List<UserVo> userList = userService.getUsersByParam(userDto);
            for (int i = 0; i < userList.size() ; i++) {
                UserVo userVo = userList.get(i);
                if(userVo != null){
                    userVo.setLevel(userVo.getLevel() == null ? 100 : userVo.getLevel());//优先值越小，权限越高
                    if(curUser.getLevel().intValue() < userVo.getLevel().intValue()){
                        newUserIdList.add(userVo.getId());
                    }
                }
            }
            if(userIdList.size() > 0 && newUserIdList.size() == 0){
                return new ApiResponse().error("抱歉，您的系统优先权不够");
            }
            //设置失效时间
            redisHelper.setUserTimeout(newUserIdList , logoutTime);
            //设置登录限制
            redisHelper.setUserLimit(newUserIdList , loginTime);
        }else{
            return new ApiResponse().error("参数错误");
        }
        return new ApiResponse().ok("执行成功");
    }


    private String formatTime(long mss){
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        StringBuffer stringBuffer = new StringBuffer();
        if(days != 0){
            stringBuffer.append(days+"天");
        }
        if(hours != 0){
            stringBuffer.append(hours+"小时");
        }
        if(minutes != 0){
            stringBuffer.append(minutes+"分钟");
        }
        if(stringBuffer.length() == 0){
            stringBuffer.append("0分钟");
        }
        //System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }


}
