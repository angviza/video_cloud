package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.ILimitLoginService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.LimitLoginVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/limitLogin",tags="用户登录允许/限制管理",description="用户登录限制增、删、改、查")
@RestController
@RequestMapping("/limitLogin")
@Slf4j
public class LimitLoginController extends BaseController {
	
	@Reference
    private ILimitLoginService limitLoginService;
	
	
	@ApiOperation(value="分页查询限制列表")
	@ApiImplicitParam(name="type",value="限制类型（0.限制的IP，1.允许的IP，2 限制mac地址）",required=true)
    @GetMapping(value = "/page")
	public ApiResponse<List<LimitLoginVo>> getPage(PageParam pageParam,String type) {
    	Map<String,Object> param =new HashMap<String,Object>();
    	param.put("type", type);
    	PageInfo<LimitLoginVo>	page =limitLoginService.getLimitByPage(pageParam,param);
    	return new ApiResponse().ok().setData(page);
    }
	
	
	@ApiOperation(value="保存限制")
    @PostMapping(value = "/save")
	public ApiResponse<LimitLoginVo> limitIpSave(LimitLoginVo vo) {
		if(StrUtil.isBlank(vo.getType())) {
			return new ApiResponse().error("限制类型不能空！");
		}
		if(StrUtil.isBlank(vo.getState())) {
			return new ApiResponse().error("是否开启状态不能空！");
		}
		if(vo.getType().equals("2")) {
			//mac地址
			if(StrUtil.isBlank(vo.getMacName())) {
				return new ApiResponse().error("mac地址不能空！");
			}else {
				vo.setIsRegasion(null);// mac 没有区间
				vo.setMacName(vo.getMacName().trim());
			}
			
		}else {
			//是ip地址
			if(StrUtil.isBlank(vo.getIsRegasion())) {
				return new ApiResponse().error("是否是区域不能空！");
			} 
			//是否是区间ip
			if(vo.getIsRegasion().equals("1")) {
				if(StrUtil.isBlank(vo.getStartRegaion())) {
		    		return new ApiResponse().error("ip区间开始段不能空！");
		    	}else {
		    		if("0.0.0.0".equals(vo.getStartRegaion()) ) {
		    			return new ApiResponse().error("ip格式！");
		    		}
		    	}
				if(StrUtil.isBlank(vo.getEndRegaion())) {
		    		return new ApiResponse().error("ip区间结束段不能空！");
		    	}else {
		    		if("0.0.0.0".equals(vo.getEndRegaion())) {
		    			return new ApiResponse().error("ip格式！");
		    		}
		    	}
				if(vo.getEndRegaion().length() >15) {
					return new ApiResponse().error("ip区间结束段格式！");
				}
				//是否在同一个网段
				String start=vo.getStartRegaion().substring(0,vo.getStartRegaion().lastIndexOf(".")+1);
				String end=vo.getEndRegaion().substring(0,vo.getEndRegaion().lastIndexOf(".")+1);
				if(! start.equals(end)) {
					return new ApiResponse().error("ip区间格式错误,开始区间和结束区间不在同一网段！");
				}
				try {
					long min =Long.parseLong(vo.getStartRegaion().substring(vo.getStartRegaion().lastIndexOf(".")+1));
					long max=Long.parseLong(vo.getEndRegaion().substring(vo.getEndRegaion().lastIndexOf(".")+1));
					if(min >max) {
						return new ApiResponse().error("ip区间格式错误,开始区间大于结束区间！");
					}
				}catch (Exception e) {
					return new ApiResponse().error("ip格式错误");
				}
				
				
			}else {
				//非区间
				if(StrUtil.isBlank(vo.getStartRegaion())) {
		    		return new ApiResponse().error("ip地址不能空！");
		    	}else {
		    		if("0.0.0.0".equals(vo.getStartRegaion())) {
		    			return new ApiResponse().error("ip格式！");
		    		}
		    	}
				try {
					Long.parseLong(vo.getStartRegaion().replace(".", ""));
				}catch (Exception e) {
					return new ApiResponse().error("ip格式错误");
				}
				
			}
			
			if(vo.getStartRegaion().length() >15) {
				return new ApiResponse().error("ip区间开始段格式！");
			}
		}
		
		UserVo userVo = getLoginUser();
		limitLoginService.limitLoginSave(vo,userVo);
    	return new ApiResponse().ok("保存成功");
    }
	
	@ApiOperation(value="更改状态")
    @PostMapping(value = "/updateState")
	public ApiResponse<LimitLoginVo> updateState(LimitLoginVo vo) {
		UserVo userVo = getLoginUser();
		limitLoginService.updateState(vo,userVo);
    	return new ApiResponse().ok("保存成功");
    }
	
	@ApiOperation(value="查看限制详细信息")
	@ApiImplicitParam(name="id",value="限制类型",required=true)
    @GetMapping(value = "/getInfo")
	public ApiResponse<LimitLoginVo> getInfo(String id) {
		LimitLoginVo vo =limitLoginService.getInfo(id);
    	return new ApiResponse().ok().setData(vo);
    }
	
	@ApiOperation(value="批量删除限制")
    @DeleteMapping(value = "/delete")
    @ApiImplicitParam(name = "ids[]", value = "限制id数组", required = true)
    public ApiResponse delete(@RequestParam(value="ids[]") String[] ids) {
        List<String> idList = Arrays.asList(ids);
        limitLoginService.deleteLimitIp(idList);
        return new ApiResponse().ok("删除成功");
    }
	

}
