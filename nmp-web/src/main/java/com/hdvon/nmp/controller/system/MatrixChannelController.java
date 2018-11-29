package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IMatrixChannelService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.MatrixChannelParamVo;
import com.hdvon.nmp.vo.MatrixChannelVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/matrixChannel",tags="矩阵通道管理模块",description="针对矩阵通道的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/matrixChannel")
@Slf4j
public class MatrixChannelController extends BaseController{

	@Reference
	private IMatrixChannelService matrixChannelService;
	
	//分页查询矩阵通道列表
	@ApiOperation(value="分页查询矩阵通道列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "矩阵id", required = false),
		@ApiImplicitParam(name = "search", value = "矩阵通道名称查询条件", required = false)
	})
	@GetMapping(value = "/page")
	public ApiResponse<PageInfo<MatrixChannelVo>> page(PageParam pp,@RequestParam(required = false)String id, @RequestParam(required = false)String search) {
        ApiResponse<PageInfo<MatrixChannelVo>> resp = new ApiResponse<PageInfo<MatrixChannelVo>>();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("matrixId", id);
        paramMap.put("search", search);
        PageInfo<MatrixChannelVo> pageInfo = matrixChannelService.getMatrixChannelPages(pp, paramMap);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}
	//查询矩阵通道列表
	@ApiOperation(value="查询矩阵通道列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "矩阵id", required = false),
		@ApiImplicitParam(name = "search", value = "矩阵通道名称查询条件", required = false)
	})
	@GetMapping(value = "/list")
	public ApiResponse<List<MatrixChannelVo>> list(@RequestParam(required = false)String id, @RequestParam(required = false)String search) {
		 ApiResponse<List<MatrixChannelVo>> resp = new ApiResponse<List<MatrixChannelVo>>();
		 try {
			 
			 
			 List<MatrixChannelVo> matrixChannelVos = matrixChannelService.getMatrixChannelList(id, search);

			 resp.ok("查询成功！").setData(matrixChannelVos);
		 }catch (Exception e) {
			log.error(e.getMessage());
			resp.error("查询失败："+e.getMessage());
		}
		 return resp;
	}
	//添加矩阵或者编辑矩阵通道初始化
	@ApiOperation(value = "添加矩阵或者编辑矩阵通道初始化")
	@GetMapping(value="/detail")
	public ApiResponse<MatrixChannelVo> detail(HttpServletRequest request, String id){
		 ApiResponse<MatrixChannelVo> resp = new ApiResponse<MatrixChannelVo>();
		 MatrixChannelVo matrixChannelVo = null;
         if(StrUtil.isNotBlank(id)) {
             matrixChannelVo = matrixChannelService.getMatrixChannelById(id);
         }
         resp.ok("查询成功！").setData(matrixChannelVo);

		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存矩阵通道信息")
	@PostMapping(value = "/add")
	public ApiResponse<Object> add(HttpServletRequest request, MatrixChannelParamVo matrixChannelParamVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(matrixChannelParamVo.getName())) {
            validMsg = "数字矩阵通道名称不能为空！";
        }
        if(StrUtil.isBlank(matrixChannelParamVo.getDevicesNo())) {
            validMsg = "数字矩阵通道设备编号不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        matrixChannelService.saveMatrixChannel(userVo, matrixChannelParamVo);
        resp.ok("保存成功！");
		return resp;
	}
	//删除矩阵通道
	@ApiOperation(value = "删除矩阵通道")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        matrixChannelService.delMatrixChannelsByIds(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
	
}
