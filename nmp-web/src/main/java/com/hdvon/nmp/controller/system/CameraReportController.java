//package com.hdvon.nmp.controller.system;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.github.pagehelper.PageInfo;
//import com.hdvon.nmp.controller.BaseController;
//import com.hdvon.nmp.service.ICameraReportService;
//import com.hdvon.nmp.service.ICameragrouopService;
//import com.hdvon.nmp.util.ApiResponse;
//import com.hdvon.nmp.util.PageParam;
//import com.hdvon.nmp.vo.ReportResponseVo;
//import com.hdvon.nmp.vo.UserVo;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//
//
//@Api(value="/cameraReport",tags="报表管理模块",description="报表地址树模块")
//@RestController
//@RequestMapping("/cameraReport")
//
//public class CameraReportController extends BaseController {
//	
//	@Reference
//	private ICameraReportService cameraReportService;
//	
//	 @ApiOperation(value="摄像机分组访问热度")
//	 @ApiImplicitParams({
//		 @ApiImplicitParam(name="cameragrouopIds",value="分组id",required=false),
//		 @ApiImplicitParam(name="startDate",value="开始时间",required=false),
//		 @ApiImplicitParam(name="endDate",value="接受时间",required=false) 
//	 })
//	 @GetMapping(value = "/cameraGrouop")
//	 public ApiResponse<List<ReportResponseVo>> cameraGrouop(String cameragrouopIds,String startDate,String endDate,PageParam pageParam) {
//			UserVo userVo = getLoginUser();
//			Map<String,Object> param = new HashMap<String,Object>();
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//			if(startDate ==null) {
//				Calendar ca=Calendar.getInstance();
//				ca.add(Calendar.DATE, -7);
//				param.put("startDate", format.format(ca.getTime()));
//			}else {
//				param.put("startDate", format.format(startDate));
//			}
//			
//			if(endDate ==null ) {
//				param.put("endDate", format.format(new Date()));
//			}else {
//				param.put("endDate", format.format(endDate));
//			}
//			
//			param.put("cameragrouopIds", cameragrouopIds);
//			param.put("startDate", startDate);
//			param.put("endDate", endDate);
//			PageInfo<ReportResponseVo> page = cameraReportService.getcameraGrouopByPage(param,pageParam);
//	 	    return new ApiResponse().ok().setData("");
//	 }
//
//}
