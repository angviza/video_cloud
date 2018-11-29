package com.hdvon.nmp.controller.system;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.config.WebAppConfig;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.IAttachFileService;
import com.hdvon.nmp.service.INoticeService;
import com.hdvon.nmp.service.INoticeTypeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.ResponseCode;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Api(value="/notice",tags="通知公告管理模块",description="针对公告的新增,删除,查看等操作")
@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController extends BaseController {
	@Reference
	private INoticeService noticeService;

	@Reference
	private IAttachFileService attachFileService;

	 @Autowired
     private WebAppConfig webAppConfig;

	@Reference
	private INoticeTypeService noticeTypeService;

	@ApiOperation(value="查找全部公告类型")
	@GetMapping(value = "/queryNoticeType")
	public ApiResponse<List<NoticeTypeVo>> queryMessageFromNotice(){
		ApiResponse<List<NoticeTypeVo>> resp = new ApiResponse<>();
		List<NoticeTypeVo> noticeTypeVos = noticeTypeService.selectNoticeType();
		return resp.ok().setData(noticeTypeVos);
	}

	//分页查询公告信息
	@ApiOperation(value="分页查询公告信息（发件箱）")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", value = "收件人名称", required = false, dataType = "String"),
		@ApiImplicitParam(name = "type", value = "公告类型", required = false, dataType = "Integer"),
		@ApiImplicitParam(name = "theme", value = "公告主题", required = false, dataType = "Integer")
	})
	@GetMapping(value = "/getNoticePage")
	public ApiResponse<PageInfo<NoticeVo>> getNoticePage(PageParam pp, String username, Integer type, String theme) {
		UserVo loginUser = getLoginUser();
		ApiResponse<PageInfo<NoticeVo>> resp = new ApiResponse<PageInfo<NoticeVo>>();
		try {
			PageInfo<NoticeVo> pageInfo = noticeService.getNoticePage(pp, username , type, theme, loginUser.getName());
			resp.ok(ResponseCode.SUCCESS.getMessage()).setData(pageInfo);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("[通知公告]==> 分页查询公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	//分页查询公告信息
	@ApiOperation(value="分页查询公告信息（收件箱）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "发件人名称", required = false, dataType = "String"),
			@ApiImplicitParam(name = "type", value = "公告类型", required = false, dataType = "Integer"),
			@ApiImplicitParam(name = "theme", value = "公告主题", required = false, dataType = "Integer")
	})
	@GetMapping(value = "/getReceiveMessagePage")
	public ApiResponse<PageInfo<NoticeVo>> getReceiveMessagePage(PageParam pp, String username, Integer type, String theme) {
		UserVo loginUser = getLoginUser();
		ApiResponse<PageInfo<NoticeVo>> resp = new ApiResponse<PageInfo<NoticeVo>>();
		try {
			PageInfo<NoticeVo> pageInfo = noticeService.getReceiveMessagePage(pp, username , type, theme, loginUser.getId());
			resp.ok(ResponseCode.SUCCESS.getMessage()).setData(pageInfo);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("[通知公告]==> 分页查询公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	@ApiOperation(value="上传文件")
	@PostMapping(value = "/uploadFiles")
	public ApiResponse<AttachFileVo> uploadFiles(HttpServletRequest request) {
		ApiResponse<AttachFileVo> resp = new ApiResponse<AttachFileVo>();
		UserVo loginUser = getLoginUser();
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		MultipartFile file = null;
		if (files.size() > 0) {
			for (int i = 0; i < files.size(); ++i) {
				file = files.get(i);
				AttachFileVo attachFileVo = new AttachFileVo();
				if (file == null) {
					return resp.error("附件上传失败,请重新上传");
				}
				String originalFileName = file.getOriginalFilename();
				// 文件存放文件路径
				String path = webAppConfig.getUploadRoot() + "/notice/";
				File file2 = new File(path);
				if (!file2.exists()) {
					file2.mkdirs();
				}
				Calendar calendar = Calendar.getInstance();
				String pathTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
				String uploadTargetPath = path + pathTime + "/";
//				String fileType = originalFileName.substring(originalFileName.indexOf(".") + 1);
//				String newFileName = UUID.randomUUID().toString() + "." + fileType;

				File targetFile = new File(uploadTargetPath);
				if (!targetFile.exists()) {
					new File(uploadTargetPath).mkdirs();
				}

				//String pathForDb = WebAppConfig.getReceiveRoot()+ uploadTargetPath + newFileName; // 获取下载路径
				String relativePath = uploadTargetPath.substring(uploadTargetPath.indexOf("notice")) + originalFileName;//截取希望保存到数据库的路径
				attachFileVo.setId(IdGenerator.nextId());
				attachFileVo.setFileName(originalFileName);
				attachFileVo.setFilePath(relativePath);
				try {
					file.transferTo(new File(uploadTargetPath + originalFileName));
					AttachFileVo aFile = attachFileService.saveAttachFile(loginUser, attachFileVo);
					resp.ok().setData(aFile);
				} catch (Exception e) {
					log.error("[通知公告]==> 上传附近失败，异常：", e);
					resp.error(ResponseCode.FAILURE.getMessage());
				}
			}
		}
		return resp;
	}
	//添加
	@ApiOperation(value = "保存公告信息")
	@PostMapping(value = "/saveNotice")
	public ApiResponse<Object> saveNotice(NoticeVo noticeVo) {
		UserVo userVo = getLoginUser();
		ApiResponse<Object> resp = new ApiResponse<Object>();
		if(noticeVo != null){
			String[] userIds = noticeVo.getUserIds().split(",");
			if(userIds.length < 1) {
				return resp.error("收件人不能为空！");
			}
			if(StrUtil.isBlank(noticeVo.getTheme())) {
				return resp.error("公告主题不能为空！");
			}
			if(StrUtil.isBlank(noticeVo.getContent())) {
				return resp.error("公告正文不能为空！");
			}

			try {
				if(noticeVo.getSettingType() == 0){
					noticeVo.setTimeSetting(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}
				noticeService.saveNotice(userVo,noticeVo);
				resp.ok("保存成功");
			} catch (ServiceException e) {
				log.error("[通知公告]==> 保存公告信息失败，异常：", e);
				resp.error(e.getMessage());
			}
		}else {
			resp.error("请填写通知公告信息，再提交！");
		}
		return resp;
	}

	@ApiOperation(value = "更新公告信息状态")
	@PostMapping(value = "/updateNoticeFlag")
	public ApiResponse<Object> updateNoticeFlag(String ids) {
		UserVo userVo = getLoginUser();
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			noticeService.updateNoticeFlag(userVo.getId(),ids);
			resp.ok("更新成功");
		} catch (Exception e) {
			log.error("[通知公告]==> 更新用户公告信息失败，异常：", e);
			resp.error(e.getMessage());
		}

		return resp;
	}

	@ApiOperation(value="当前登录的用户公告信息提示")
	@GetMapping(value = "/queryMessageByNoticeType")
	public ApiResponse<List<NoticeTypeVo>> queryMessageByNoticeType(Integer flag){
		UserVo loginUser = getLoginUser();
		ApiResponse<List<NoticeTypeVo>> resp = new ApiResponse<>();
		try {
			List<NoticeTypeVo> list = noticeService.queryMessageByNoticeType(loginUser.getId(),flag);
			resp.ok().setData(list);
		} catch (Exception e) {
			log.error("[通知公告]==>当前登录的用户公告信息提示(所有 已读 未读)，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	@ApiOperation(value="当前登录的未读数及最新信息")
	@GetMapping(value = "/queryNoticeUnRead")
	public ApiResponse<NoticeUnReadVo> queryNoticeUnRead(){
		Integer flag = 0;
		UserVo loginUser = getLoginUser();
		ApiResponse<NoticeUnReadVo> resp = new ApiResponse<>();
		try {
			NoticeUnReadVo unVo = noticeService.queryNoticeUnRead(loginUser.getId(),flag);
			resp.ok().setData(unVo);
		} catch (Exception e) {
			log.error("[通知公告]==>当前登录的未读数及最新信息，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	//@ApiOperation(value="用户登录前的维护公告提醒")
	@GetMapping(value = "/getRemindMessage")
	public ApiResponse<String> getRemindMessage(){
		ApiResponse<String> resp = new ApiResponse<>();
		try {
			String unVo = noticeService.getRemindMessage();
			resp.ok().setData(unVo);
		} catch (Exception e) {
			log.error("[通知公告]==>当前登录维护公告提醒，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	@ApiOperation(value="收件人提示信息的公告列表,按类型显示")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "公告类型", required = false, dataType = "Integer"),
			@ApiImplicitParam(name = "readFlag", value = "是否已读，0为未读 1为已读", required = false, dataType = "Integer")
	})
	@GetMapping(value = "/getMessageListPage")
	public ApiResponse<List<NoticeVo>> getMessageListPage(Integer type, Integer readFlag) {
		UserVo loginUser = getLoginUser();
		ApiResponse<List<NoticeVo>> resp = new ApiResponse<List<NoticeVo>>();
		try {
			List<NoticeVo> list = noticeService.getMessageListPage(loginUser,type, readFlag);
			resp.ok(ResponseCode.SUCCESS.getMessage()).setData(list);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("[通知公告]==> 分页查询公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	@ApiOperation(value="查询公告详细信息(发件人)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "公告Id", required = true, dataType = "String"),
	})
    @GetMapping(value = "/queryNoticeInfoByPublish")
    public ApiResponse<NoticeVo> queryNoticeInfoByPublish(String id){
		UserVo loginUser = getLoginUser();
    	ApiResponse<NoticeVo> resp = new ApiResponse<>();
    	if(StrUtil.isBlank(id)){
            return resp.error("公告id不允许为空！");
        }
    	try {
    		NoticeVo noticeVo = noticeService.getNoticeInfoByPublish(loginUser,id);
    		if(noticeVo == null){
    			return resp.error("找不到用户信息！");
    		}
    		resp.ok().setData(noticeVo);	
		} catch (Exception e) {
			log.error("[通知公告]==> 查看公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
    	return resp;
    }

	@ApiOperation(value="查询公告详细信息(收件人)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "公告Id", required = true, dataType = "String"),
	})
	@GetMapping(value = "/queryNoticeInfoByReceive")
	public ApiResponse<NoticeVo> queryNoticeInfoByReceive(String id){
		UserVo loginUser = getLoginUser();
		ApiResponse<NoticeVo> resp = new ApiResponse<>();
		if(StrUtil.isBlank(id)){
			return resp.error("公告id不允许为空！");
		}
		try {
			NoticeVo noticeVo = noticeService.queryNoticeInfoByReceive(loginUser,id);
			if(noticeVo == null){
				return resp.error("找不到用户信息！");
			}
			resp.ok().setData(noticeVo);
		} catch (Exception e) {
			log.error("[通知公告]==> 查看公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	@ApiOperation(value="删除公告（发件人）")
    @DeleteMapping(value = "/delNotice")
    public ApiResponse<Object> delNotice(@RequestParam(value="ids[]") String[] ids){
    	ApiResponse<Object> resp = new ApiResponse<Object>();
    	UserVo loginUser = getLoginUser();
        List<String> idList = Arrays.asList(ids);
        try {
        	noticeService.delNotice(loginUser,idList);
        	resp.ok("删除成功！");
		} catch (Exception e) {
			log.error("[通知公告]==> 删除公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
    	return resp;
    }

	@ApiOperation(value="删除公告（收件人）通过公告类型")
	@DeleteMapping(value = "/delUserNoticeByIds")
	public ApiResponse<Object> delUserNoticeByIds(@RequestParam(value="ids[]") String[] ids,Integer flag){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo loginUser = getLoginUser();
		List<String> idList = Arrays.asList(ids);
		try {
			noticeService.delUserNoticeByIds(loginUser.getId(),flag,idList);
			resp.ok("删除成功！");
		} catch (Exception e) {
			log.error("[通知公告]==> 删除公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	@ApiOperation(value="删除公告（收件箱）")
	@DeleteMapping(value = "/delReceiveNoticeByIds")
	public ApiResponse<Object> delReceiveNoticeByIds(@RequestParam(value="ids[]") String[] ids){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo loginUser = getLoginUser();
		List<String> idList = Arrays.asList(ids);
		try {
			noticeService.delReceiveNoticeByIds(loginUser.getId(),idList);
			resp.ok("删除成功！");
		} catch (Exception e) {
			log.error("[通知公告]==> 删除公告信息失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}
		return resp;
	}

	@ApiOperation(value = "下载附件")
	@ApiImplicitParam(name = "filePath", value = "数据库保存的路径", required = true)
	@GetMapping(value="/downloadFile")
	public ApiResponse<Object> downloadFile(@RequestParam(value="filePath",required=true) String filePath,@RequestParam(value="fileName",required=true) String fileName,HttpServletResponse response){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		/*String suffixName = fileName.substring(fileName.lastIndexOf(".")+1);
		if(suffixName == "xlsx" ||suffixName == "xls"){
			response.setHeader("Content-disposition", "attachment; filename="+fileName);
			response.setContentType("application/msexcel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
		} else if(suffixName == "docx"){
			response.setHeader("Content-disposition", "attachment; filename="+fileName);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setCharacterEncoding("utf-8");
		} else if(suffixName == "pdf"){
			response.setHeader("Content-disposition", "attachment; filename="+fileName);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setCharacterEncoding("utf-8");
		}*/

		// 是以流的形式下载文件，这样可以实现任意格式的文件下载。
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;charset=utf-8;filename=" + fileName);
		response.setCharacterEncoding("utf-8");
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;

		try {
			String relatePath = webAppConfig.getUploadRoot()+"/";
			File file = new File(relatePath + filePath); //新建一个文件
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));

			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
			//os.close();
			resp.ok();
		}catch(Exception e) {
			log.error("[通知公告]==> 附件下载失败，异常：", e);
			resp.error(ResponseCode.FAILURE.getMessage());
		}finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resp;
	}
}
