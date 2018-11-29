package com.hdvon.nmp.controller.system;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.aop.ControllerRedis;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IAddressService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.AddressVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Api(value="/address",tags="地址树管理模块",description="管理地址树模块")
@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController extends BaseController{

    @Reference
    private IAddressService addressService;
    
	@Reference
	private ITreeNodeService treeNodeService;


    @ApiOperation(value="获取单个地址信息")
    @ApiImplicitParam(name = "id", value = "地址id", required = true)
    @GetMapping(value = "/{id}")
    public ApiResponse<AddressVo> getAddressById(@PathVariable String id) {
        if(StrUtil.isBlank(id)){
            return new ApiResponse().error("id参数不允许为空");
        }
        AddressVo addressVo = addressService.getAddressById(id);
        if(addressVo == null){
            return new ApiResponse().error("该地址不存在");
        }
        return new ApiResponse().ok().setData(addressVo);
    }

    @ApiOperation(value="分页获取地址子节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchText", value = "模糊条件", required = false),
            @ApiImplicitParam(name = "id", value = "节点id", required = false),
    })
    @GetMapping(value = "/children")
    public ApiResponse<PageInfo<AddressVo>> getRoleChildrens(PageParam pageParam , AddressVo addressVo) {

        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(addressVo.getId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(addressVo.getCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
        PageInfo<AddressVo> page = addressService.getAddressListByPage(addressVo, treeNodeChildren , pageParam);
        return new ApiResponse().ok().setData(page);
    }

    @ControllerRedis
    @ControllerLog
    @ApiOperation(value="保存地址")
    @PostMapping(value = "/")
    public ApiResponse<AddressVo> saveAddress(AddressVo addressVo) {
        UserVo userVo = getLoginUser();
        if(StrUtil.isBlank(addressVo.getName())){
            return new ApiResponse().error("地址名称不允许为空");
        }
        if(StrUtil.isNotBlank(addressVo.getId()) && addressVo.getPid().equals(addressVo.getId())) {
        	return new ApiResponse().error("地址的上级地址不能是自身！");
        }
       addressService.saveAddress(userVo,addressVo);
        
        return new ApiResponse().ok("保存地址成功");
    }

    @ControllerRedis
    @ApiOperation(value="批量删除地址")
    @DeleteMapping(value = "/")
    @ApiImplicitParam(name = "addressIds[]", value = "地址id数组", required = true)
    public ApiResponse deleteAddress(@RequestParam(value="addressIds[]") String[] addressIds) {
        List<String> addressIdList = Arrays.asList(addressIds);
        addressService.deleteAddress(getLoginUser() , addressIdList);
        
        return new ApiResponse().ok("删除地址成功");
    }
    
    @ApiOperation(value = "下载地址文件模板")
	@GetMapping(value="/downloadAddressTemplate")
	public ApiResponse<Object> downloadAddressTemplate(HttpServletRequest request, HttpServletResponse response){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			String templateName = "address_template.xls";
			FileUtil.downloadTemplate(request, response, templateName, null);
			resp.ok("下载成功！");
		}catch(Exception e) {
			log.error(e.getMessage());
			resp.error("下载失败："+e.getMessage());
		}
		return resp;
	}
    
    @ApiOperation(value = "导入地址数据")
	@PostMapping(value="/importAddresses")
	public ApiResponse<Object> importAddresses(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
            // 模板
            String[] titles = new String[] {"name","code","description"};
            List<Map<String,Object>> list = FileUtil.importData(file, titles);
            // 字段
            String[] colums = new String[] {"id","name","pid","code","description"};
            List<Map<String,Object>> glist =addressService.transpCodeToId(list,colums);

			addressService.batchInsertAddresses(glist,colums);
			resp.ok("导入成功！");
		}catch(IOException e) {
			log.error(e.getMessage());
			resp.error("导入失败："+e.getMessage());
		}
		return resp;
	}

    @ApiOperation(value = "导出地址数据")
    @GetMapping(value="/exportAddresses")
    public ApiResponse<Object> exportAddresses(HttpServletResponse response,AddressVo addressVo){
        ApiResponse<Object> resp = new ApiResponse<Object>();

        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
        if(StrUtil.isNotBlank(addressVo.getId())) {
            List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(addressVo.getCode(), TreeType.ADDRESS.getVal());
            treeNodeChildren.setAddressNodes(addressNodes);
        }

        String[] titles = new String[] {"id","name","code","pid","pcode","description"};
        List<Map<String,Object>> objList = new ArrayList<>();

        List<AddressVo> list = addressService.exportAddresses(addressVo.getName(),treeNodeChildren);
        for(AddressVo item : list){
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("id", item.getId());
            tempMap.put("name", item.getName());
            tempMap.put("code", item.getCode());
            tempMap.put("pid", item.getPid());
            tempMap.put("pcode", item.getPcode());
            tempMap.put("description", item.getDescription());
            objList.add(tempMap);
        }
        try {
            FileUtil.exportExcel(response,"地址树",titles,objList);
            resp.ok("导出成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.error("导出失败！");
        }
        return resp;
    }
    
    @ApiOperation(value="获取带状态的所有地址列表")
    @GetMapping(value = "/getStatusAddressList")
    public ApiResponse<List<AddressVo>> getStatusAddressList(String addressId) {
    	List<AddressVo> list = addressService.getStatusAddressList(addressId);
		return new ApiResponse<List<AddressVo>>().ok().setData(list);
    	
    }
    
}
