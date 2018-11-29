package com.hdvon.nmp.controller.system;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.hdvon.nmp.service.IAlarmServerService;
import com.hdvon.nmp.service.IDeviceService;
import com.hdvon.nmp.service.IDevicecodeCodeService;
import com.hdvon.nmp.service.IDevicecodeDictionaryService;
import com.hdvon.nmp.service.IDevicecodeOptionService;
import com.hdvon.nmp.service.IEncoderServerService;
import com.hdvon.nmp.service.IGatewayServerService;
import com.hdvon.nmp.service.IMatrixChannelService;
import com.hdvon.nmp.service.IMatrixService;
import com.hdvon.nmp.service.ISigServerService;
import com.hdvon.nmp.service.IStatusServerService;
import com.hdvon.nmp.service.IStoreServerService;
import com.hdvon.nmp.service.ITranspondServerService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DevicecodeDictionaryVo;
import com.hdvon.nmp.vo.DevicecodeOptionVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/devicecodeDict",tags="设备编码字典管理模块",description="针对设备编码字典的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/devicecodeDict")
@Slf4j
public class DevicecodeDictionaryController extends BaseController {

	@Reference
	private IDevicecodeDictionaryService devicecodeDictionaryService;
	@Reference
	private IDeviceService deviceService;
	@Reference
	private IDevicecodeOptionService devicecodeOptionService;
	@Reference
	private IDevicecodeCodeService devicecodeCodeService;
	@Reference
	private IEncoderServerService encoderServerService;
	@Reference
	private IMatrixService matrixService;
	@Reference
	private IMatrixChannelService matrixChannelService;
	@Reference
	private ITranspondServerService transpondServerService;
	@Reference
	private IStoreServerService storeServerService;
	@Reference
	private IStatusServerService statusServerService;
	@Reference
	private IGatewayServerService gatewayServerService;
	@Reference
	private IAlarmServerService alarmServerService;
	@Reference
	private ISigServerService sigServerService;
	
	
	@ApiOperation(value="分页查询生成编码")
    @GetMapping(value = "/getOptionPage")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceCode",value="设备编码",required=false),
		 @ApiImplicitParam(name="province",value="省",required=false), 
		 @ApiImplicitParam(name="city",value="市",required=false), 
		 @ApiImplicitParam(name="area",value="区",required=false), 
		 @ApiImplicitParam(name="basicNnit",value="基层单位",required=false)
	 })
    public ApiResponse<List<DevicecodeOptionVo>> getOptionPage(String deviceCode,String province,
    		String city,String area,String basicNnit,PageParam pageParam) {
		Map<String ,String> param =new HashMap<String ,String>();
		param.put("deviceCode", deviceCode);
		param.put("province", province);
		param.put("city", city);
		param.put("area", area);
		param.put("basicNnit", basicNnit);
		PageInfo<DevicecodeOptionVo> page=devicecodeOptionService.getOptionPage(pageParam,param);
		
	  return new ApiResponse().ok().setData(page);
    }

	  @ApiOperation(value="批量删除生成编码")
      @DeleteMapping(value = "/del")
      @ApiImplicitParam(name = "deviceCodeIds[]", value = "设备编码id", required = true)
      public ApiResponse deleteAddress(@RequestParam(value="deviceCodeIds[]") String[] deviceCodeIds) {
         List<String> deviceCodeList = Arrays.asList(deviceCodeIds);
         devicecodeCodeService.deleteCode(deviceCodeList);
         return new ApiResponse().ok("删除地址成功");
     }
	  
	  
	  @ApiOperation(value="生成编码详情")
	  @ApiImplicitParam(name = "devieCodeId", value = "设备编码id", required = true)
      @GetMapping(value = "/info")
      public ApiResponse<DevicecodeOptionVo> info(String devieCodeId) {
		  if(StrUtil.isBlank(devieCodeId)) {
			  return new ApiResponse().error("设备编码id不能为空！");
		  }
		  DevicecodeOptionVo vo=  devicecodeOptionService.getInfo(devieCodeId);
          return new ApiResponse().ok().setData(vo);
     }
	
	@ControllerLog
	@ApiOperation(value = "导入设备编码字典数据")
	@PostMapping(value="/importDevicecodeDicts")
	public ApiResponse<Object> importOrgnizations(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			String[] titles = new String[] {"position","code","name","pCode","isReserved"};
			List<Map<String,Object>> list = FileUtil.importData(file, titles);
            System.out.println("-------------"+list.size());
            devicecodeDictionaryService.batchInsertDevicecodeDicts(list, titles);
            resp.ok("导入成功！");
        }catch(IOException e) {
            log.error(e.getMessage());
            resp.error("导入失败："+e.getMessage());
        }


		return resp;
	}
	
	 @ApiOperation(value="获取地理树数据")
     @GetMapping(value = "/addr")
     public ApiResponse<List<DevicecodeDictionaryVo>> addr() {
        List<String> list =new ArrayList<String>();
       List<DevicecodeDictionaryVo> listCode=devicecodeDictionaryService.getAddrList(list);
	  return new ApiResponse().ok().setData(listCode);
     }
	

	 @ApiOperation(value="根据pid获取父节点下的子节点列表")
	 @ApiImplicitParam(name="pid",value="父节点的id",required=true)
     @GetMapping(value = "/childrensBypId")
     public ApiResponse<List<DevicecodeDictionaryVo>> getChildrensBypid(String pid) {
		 DevicecodeDictionaryVo vo = new DevicecodeDictionaryVo();
		 vo.setPid(pid);
       List<DevicecodeDictionaryVo> list=devicecodeDictionaryService.getChildrens(vo);
	  return new ApiResponse().ok().setData(list);
     }
	 
	  @ApiOperation(value="更加位置和编码获取父节点下的子节点列表")
	 @ApiImplicitParams({
		 @ApiImplicitParam(name="position",value="父节点的编码位置",required=true), 
		 @ApiImplicitParam(name="code",value="父节点字典编码",required=true) 
	 })
     @GetMapping(value = "/childrensBypCode")
     public ApiResponse<List<DevicecodeDictionaryVo>> getChildrensBypCode(String position,String code) {
		 DevicecodeDictionaryVo vo = new DevicecodeDictionaryVo();
		 vo.setPcode(getpCode(position, code));
       List<DevicecodeDictionaryVo> list=devicecodeDictionaryService.getChildrens(vo);
	  return new ApiResponse().ok().setData(list);
     }
	 
		/**
		 * 根据位置和编码获取当前节点pCode
		 * @param position 位置
		 * @param code 编码
		 * @return
		 */
		private String getpCode(String position,String code) {
			String key = position + "$" + code;
			return key;
		}

	 
	 @ApiOperation(value="获取行业，类型，网络标识初始化接口(列表position值为9,10为行业，11,12,13为类型，14为网络标识,15,16,17,18,19,20 市/区投资..)")
     @GetMapping(value = "/getList")
     public ApiResponse<List<DevicecodeDictionaryVo>> getList() {
       List<DevicecodeDictionaryVo> devicecodeList=devicecodeDictionaryService.getList();
	   return new ApiResponse().ok().setData(devicecodeList);
     }
	 
//	 @ControllerLog
	 @ApiOperation(value="根据选项生成设备编码")
	 @ApiImplicitParam(name="isUser",value="是否直接使用该编码(0-否，-是)",required=true)
	 @PostMapping(value = "/generateCode")
	 public ApiResponse<Object> generateCode(DevicecodeOptionVo param,String isUser) {
		UserVo userVo = getLoginUser();
		String error=isNotBlant(param);
		String maxCode=null;
		if(StrUtil.isNotBlank(error)) {
			return new ApiResponse().error(error);
		}
		//市公安的规则
		 List<BigDecimal> list = new ArrayList<BigDecimal>(); 
		 if(param.getArea().equals("440100")) {
			 String baseCode=param.getArea()+param.getBasicNnit()+param.getIndustry()+param.getType()+param.getInternet() +param.getRegion();
			 maxCode=selectMaxCode(baseCode);
			 try {
				list=cerateSGACode(baseCode,maxCode,param.getNumber());
			 }catch (Exception e) {
				 return new ApiResponse().error(e.getMessage());
			 }
		 }else {
			 //非市公安的规则
			 if("08,09,13,14,15".contains(param.getIndustry())) {//行业编码为08,09,13,14,15
				 String baseCode=param.getArea()+param.getBasicNnit()+param.getIndustry()+param.getType()+param.getInternet();
				 maxCode=selectMaxCode(baseCode);
				 try {
					 list=cerateSCZCode(baseCode,maxCode,param.getIndustryName(),param.getNumber());
				 }catch (Exception e) {
					 return new ApiResponse().error(e.getMessage());
				 }
			 }else {
				 String baseCode=param.getArea()+param.getBasicNnit()+param.getIndustry()+param.getType()+param.getInternet();
				// String maxCode=deviceService.getMaxCodeBycode(baseCode+param.getConstruction().substring(0, 3));
				 maxCode=selectMaxCode(baseCode+param.getConstruction().substring(0, 3));
				 try {
					 list= cerateTZCode(baseCode,maxCode,param.getConstruction(),param.getNumber());
				 }catch (Exception e) {
					 return new ApiResponse().error(e.getMessage());
				}
			 }
		 }
		 
		  devicecodeOptionService.saveDeviceCode(list,param,userVo,isUser);
		 return new ApiResponse().ok().setData(list);
	 }
	 
	 
	 @ApiOperation(value="编码复用生成编码")
	 @ApiImplicitParam(name="userType",value="使用编码类型:1-摄像机编码；2-编码器编码；3-数字矩阵编号；4-矩阵通道编号"
	 		+ "5-转发服务器编码；6-存储服务器编码；7-状态服务器编码；8-网关服务器编码 ；9-报警设备编号；10-中心信令服务器编码；",required=true)
	 @PostMapping(value = "/createCode")
	 public ApiResponse<Object> createCode(DevicecodeOptionVo param,String userType) {
		if(StrUtil.isBlank(userType)) {
			return new ApiResponse().error("使用编码类型不能为空！");
		}
		if(StrUtil.isBlank(param.getProvince())) {
			return new ApiResponse().error("省/市/区不能为空！");
		}
		if(StrUtil.isBlank(param.getCity())) {
			return new ApiResponse().error("省/市/区不能为空！");
		}
		if(StrUtil.isBlank(param.getArea())) {
			return new ApiResponse().error("省/市/区不能为空！");
		}
		if(StrUtil.isBlank(param.getBasicNnit())) {
			return new ApiResponse().error("基层单位不能为空！");
		}
		if(StrUtil.isBlank(param.getIndustry())) {
			return new ApiResponse().error("行业不能为空！");
		}
		if(StrUtil.isBlank(param.getType())) {
			return new ApiResponse().error("类型不能为空！");
		}
		if(StrUtil.isBlank(param.getInternet())) {
			return new ApiResponse().error("网络标识不能为空！");
		}
		
		String code=param.getArea().trim()+param.getBasicNnit().trim()+param.getIndustry().trim()+
				param.getType().trim()+param.getInternet().trim();
		
		 String maxCode=null;
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("code", code);//查询用like,最大配置
		// 公安监控类型和监控区域
		if(StrUtil.isNotBlank(param.getContorlType())) {
			code+=param.getContorlType().trim();
			if(StrUtil.isNotBlank(param.getRegion())) {
				code+=param.getRegion().trim();
			}
		}    
		// 投资单位
		if(StrUtil.isNotBlank(param.getInvestment())) {
			code+=param.getInvestment().trim();
		}
		//承建单位单位
		if(StrUtil.isNotBlank(param.getConstruction())) {
			code+=param.getConstruction().trim();
		}
		 if(userType.equals("1")) {// 摄像机编码
			 maxCode=deviceService.getMaxCodeBycode(code);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("2")) {//编码器编码
			 maxCode=encoderServerService.getMaxCodeByParam(map);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("3")) {//数字矩阵编号
			 maxCode=matrixService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("4")) {//矩阵通道编号
			 maxCode=matrixChannelService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("5")) {//转发服务器编码
			 maxCode=transpondServerService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("6")) {//存储服务器编码
			 maxCode=storeServerService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("7")) {//状态服务器编码
			 maxCode=statusServerService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("8")) {//网关服务器编码
			 maxCode=gatewayServerService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
			 
		 }else if(userType.equals("9")) {//报警设备编号
			 maxCode=alarmServerService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
		 }else if(userType.equals("10")) {//中心信令服务器编码
			 maxCode=sigServerService.getMaxCodeBycode(map);
			 maxCode=createMaxCode(code,maxCode);
		 }
		 
		 return new ApiResponse().ok().setData(maxCode);
	 }

	
	 private String createMaxCode(String code,String maxCode) {
		 
		 if(StrUtil.isNotBlank(maxCode)) {
			 BigDecimal max=new BigDecimal(maxCode);
			 max= max.add(new BigDecimal(1));// 最大编码加1
			 maxCode= max.toString();
		 }else {
			 // 符合编码
			 int length=code.length();
			 String mix="000001";
			 if(length==20) {
				 return code;
			 }else {
				 maxCode= code+mix.substring(0, (20-length));
			 }
			// maxCode= code+"000001" ;
		 } 
		return maxCode;
	}

	/**
	  * 获取本系统的最大编码
	  * @param baseCode
	  * @return
	  */
	 private String selectMaxCode(String baseCode) {
		 String maxCode=null;
		//本地生成的编码
		 maxCode=devicecodeCodeService.getMaxCodeBycode(baseCode);
		 if(StrUtil.isBlank(maxCode)) {
			 //设备表存在的编码
			 maxCode=deviceService.getMaxCodeBycode(baseCode);
		 }
		 return maxCode;
	 }
	//判断是否符合生成规则条件
	// 设备编码 =中心编码（8位） + 行业编码（2位） + 类型编码（3位） + 序号（7位）
	private String isNotBlant(DevicecodeOptionVo param) {
		if(StrUtil.isBlank(param.getArea())) {
			 return "地区！";
		 }
		 if(StrUtil.isBlank(param.getBasicNnit())){
			 return "请选择基层单位！";
		 }
		 //行业2位
		 if(StrUtil.isBlank(param.getIndustry())) {
			 return "请选行业！";
		 }
		 //设备类型 3位
		 if(StrUtil.isBlank(param.getType())) {
			 return "请选类型！";
		 }else {
			if(param.getType().length()>4) {
				return "选择类型暂时不能使用！"; //是预留位，暂时不能使用
			}
		 }
		 // 网络标识 1位
		 if(StrUtil.isBlank(param.getInternet())) {
			 return "请选网络标识！";
		 }
		 
		 if(param.getNumber()==0) {
			 return "生成数量不能为空！";
		 }
		 
		 //行业决定是否选择行业名称
		 if("08,09,13,14,15".contains(param.getIndustry())) {
			 //行业编码影响15-20位，不同的行业名称有不同的编码
			 if(StrUtil.isBlank(param.getIndustryName())) {
				 return "该行业下单位不允许为空";
			 }
			 //不是事公安管的
		 }else if(! param.getArea().equals("440100")){
			 if(StrUtil.isBlank(param.getInvestment())) {
				 return "投资单位不能为空！";
			 }else if(param.getInvestment().equals("000001-100000")) {
				 if(StrUtil.isBlank(param.getConstruction())) {
					 return "市财政投资的社会治安类视频项目承建单位不能为空！";
				 }
			 }
		 }
		 
		 //市公安
		 if(param.getArea().equals("440100")) {
			 if( ! param.getInternet().equals("5")) {
				 return "市公安直属单位必须选择接入方式为为公安网！";
			 }
			 if(StrUtil.isBlank(param.getContorlType())) {
				 return "监控范围不能为空！";
			 }
			 if(StrUtil.isBlank(param.getRegion())) {
				 return "区域不能为空！";
			 }
		 }else {
			 //非市公安直属单位
			 if(param.getInternet().equals("5")) {
				 return "非市公安直属单位不能选择接入方式为为公安网！";
			 }
		 }
		 
		return null;
	}

	//市公安
	private List<BigDecimal> cerateSGACode(String baseCode, String maxCode, Integer number) throws Exception {
		List<BigDecimal> list = new ArrayList<BigDecimal>(); 
		BigDecimal max=new BigDecimal(baseCode+"999");
		BigDecimal mix= new BigDecimal(baseCode+"000");
		BigDecimal maxC=null;
		for(int i=1;i<number+1;i++) {
			if(StrUtil.isBlank(maxCode)) {
				 list.add(mix.add(new BigDecimal(i)));
			 }else {
				 maxC=new BigDecimal(maxCode);
				 if(max.compareTo(maxC.add(new BigDecimal(i)))!=-1) {
					 list.add(maxC.add(new BigDecimal(i)));
				 }else {
					 throw new Exception("已经是最大值！");
				 }
		    }
		}
		return list;
	}
	
	//行业编码为08,09,13,14,15
	 private List<BigDecimal> cerateSCZCode(String baseCode, String maxCode, String industryName, Integer number) throws Exception {
		 String[] soce=industryName.split("-");
		 List<BigDecimal> list = new ArrayList<BigDecimal>(); 
		 BigDecimal max=new BigDecimal(baseCode+soce[1]);
		 BigDecimal mix= new BigDecimal(baseCode+soce[0]);
		 BigDecimal maxC=null;
		 for(int i=0;i<number;i++) {
			if(StrUtil.isBlank(maxCode)) {
				 list.add(mix.add(new BigDecimal(i)));
			 }else {
				 maxC=new BigDecimal(maxCode);
				 if(max.compareTo(maxC.add(new BigDecimal(i+1)))!=-1) {
					 list.add(maxC.add(new BigDecimal(i+1)));
				 }else {
					 throw new Exception("已经是最大值！");
				 }
		    }
		 }
		 return list;
	 }
	 
	 //市或者区投资的摄像机
	 private List<BigDecimal> cerateTZCode(String baseCode, String maxCode, String construction, Integer number) throws Exception {
		 String[] soce=construction.split("-");
		 List<BigDecimal> list = new ArrayList<BigDecimal>(); 
		 BigDecimal max=new BigDecimal(baseCode+soce[1]);
		 BigDecimal mix= new BigDecimal(baseCode+soce[0]);
		 BigDecimal maxC=null;
		 for(int i=0;i<number;i++) {
			if(StrUtil.isBlank(maxCode)) {
				 list.add(mix.add(new BigDecimal(i)));
			 }else {
				 maxC=new BigDecimal(maxCode);
				 if(max.compareTo(maxC.add(new BigDecimal(i+1)))!=-1) {
					 list.add(maxC.add(new BigDecimal(i+1)));
				 }else {
					 throw new Exception("已经是最大值！");
				 }
		    }
		}
		 return list;
			
	 }
	
	 /*private DevicecodeParam newparam() {
		 DevicecodeParam param= new DevicecodeParam();
		 param.setAddress("440101");
		 param.setBasicNnit("01");
		 param.setIndustry("08");//行业
		 param.setIndustryName("000001-020000");
		 param.setType("111");
		 param.setInternet("6");
		 param.setContorlType("100");
		 param.setRegion("101");
		 param.setInvestment("电信");
		 param.setConstruction("000001-010000");
		 param.setNumber(3);
		return param;
	}*/
	 
	
}
