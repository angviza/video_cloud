package com.hdvon.nmp.service.impl;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.FileRelate;
import com.hdvon.nmp.entity.Notice;
import com.hdvon.nmp.entity.UserNotice;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.INoticeService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>通知公告表Service<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class NoticeServiceImpl implements INoticeService {

	@Autowired
	private NoticeMapper noticeMapper;
	
	@Autowired
	private UserNoticeMapper userNoticeMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private FileRelateMapper fileRelateMapper;

	@Autowired
	private AttachFileMapper attachFileMapper;

	@Autowired
	private NoticeTypeMapper noticeTypeMapper;

	@Override
	public PageInfo<NoticeVo> getNoticePage(PageParam pp, String username, Integer type, String theme,String loginName) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> param = new HashMap<>();
		param.put("createUser",loginName);
        param.put("username",username);
        param.put("type",type);
        param.put("theme",theme);
		List<NoticeVo> list = noticeMapper.selectPublishMessageByParam(param);
		if(list.size() >0){
			for(NoticeVo noticeVo :list){
				String noticeId = noticeVo.getId();
				List<UserVo> userVos = userMapper.queryUserByNoticeId(noticeId);
				noticeVo.setUserVos(userVos);
//				List<AttachFileVo> attachFileVos = attachFileMapper.queryAttachFileByNoticeId(noticeId);
//				noticeVo.setAttachFileVos(attachFileVos);
			}
		}
    	return new PageInfo<>(list);
	}

	@Override
	public PageInfo<NoticeVo> getReceiveMessagePage(PageParam pp, String username, Integer type, String theme,String userId) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		Map<String,Object> param = new HashMap<>();
		param.put("userId",userId);
		param.put("username",username);
		param.put("type",type);
		param.put("theme",theme);
		List<NoticeVo> list = noticeMapper.selectReceiveMessageByParam(param);
		if(list != null && list.size() >0) {
			for (NoticeVo noticeVo : list) {
				String noticeId = noticeVo.getId();
				List<AttachFileVo> attachFileVos = attachFileMapper.queryAttachFileByNoticeId(noticeId);
				noticeVo.setAttachFileVos(attachFileVos);
			}
		}
		return new PageInfo<>(list);
	}

	@Override
	public void saveNotice(UserVo loginUser,NoticeVo noticeVo) {
		
		if(StrUtil.isBlank(noticeVo.getId())){
			Notice notice = Convert.convert(Notice.class, noticeVo);
		   // 保存公告
			notice.setId(IdGenerator.nextId());
			Date date = new Date();
			notice.setCreateTime(date);
			notice.setCreateUser(loginUser.getAccount());
			notice.setUpdateTime(date);
			notice.setUpdateUser(loginUser.getAccount());
			noticeMapper.insertSelective(notice);
			
			// 保存用户公告映射表
			UserNotice uNotice = new UserNotice();
			String[] userIds = noticeVo.getUserIds().split(",");
			//去重操作
			Set<String> set = new HashSet();
			for (int i = 0; i < userIds.length; i++) {
				if(loginUser.getId().equals(userIds[i])){
					throw new ServiceException("不能对自己发送消息");
				}
				set.add(userIds[i]);
			}
			String[] list =set.toArray(new String[set.size()]);

			for(int i = 0; i < list.length;i++) {
				uNotice.setId(IdGenerator.nextId());
				uNotice.setUserId(list[i]);
				uNotice.setNoticeId(notice.getId());
				uNotice.setFlag(0);
				userNoticeMapper.insertSelective(uNotice);
			}
			
			// 保存附件路径
			String fileIdArr = noticeVo.getFileIds();
			if(fileIdArr != null){
				String[] fileIds = fileIdArr.split(",");
				for(String id : fileIds) {
					FileRelate fileRelate = new FileRelate();
					fileRelate.setId(IdGenerator.nextId());
					fileRelate.setFileId(id);
					fileRelate.setRelateId(notice.getId());
					fileRelateMapper.insertSelective(fileRelate);
				}
			}
		}
	}

	@Override
	public void delNotice(UserVo loginUserVo, List<String> ids) {
		// 删除公告表
        Example ue = new Example(Notice.class);
        ue.createCriteria().andIn("id", ids);
        noticeMapper.deleteByExample(ue);
        //删除附近表
//        Example af = new Example(AttachFile.class);
//        af.createCriteria().andIn("relateId", ids);
//        attachFileMapper.deleteByExample(af);
//        //删除用户公告表
//        Example un = new Example(UserNotice.class);
//        un.createCriteria().andIn("noticeId", ids);
//        userNoticeMapper.deleteByExample(un);
	}

	/**
	 * 编辑/查看公告信息(发件人)
	 * @param userVo
	 * @param id
	 * @return
	 */
	@Override
	public NoticeVo getNoticeInfoByPublish(UserVo userVo, String id) {
	    if(id == null){
	        id = "";
        }
		List<NoticeVo> list = noticeMapper.queryNoticeByParam(userVo.getName(),id);
		return getInfoBylist(list,id,userVo);
	}

	/**
	 * 编辑/查看公告信息(收件人)
	 * @param userVo
	 * @param id
	 * @return
	 */
	@Override
	public NoticeVo queryNoticeInfoByReceive(UserVo userVo, String id) {
		if(id == null){
			id = "";
		}
		List<NoticeVo> list = noticeMapper.queryNoticeByReceive(userVo.getId(),id);
		return getInfoBylist(list,id,userVo);
	}
	private NoticeVo getInfoBylist(List<NoticeVo> list,String id,UserVo userVo){
		if(list != null) {
			NoticeVo noticeVo = list.get(0);
			List<UserVo> userVos =userMapper.queryUserByNoticeId(id);
			noticeVo.setUserVos(userVos);
			List<AttachFileVo> attachFileVos = attachFileMapper.queryAttachFileByNoticeId(id);
			noticeVo.setAttachFileVos(attachFileVos);
			noticeVo.setCreateUser(userVo.getName());
			noticeVo.setDepartmentName(userVo.getDepartmentName());
			return noticeVo;
		}
		return null;
	}
	@Override
	public List<NoticeTypeVo> queryMessageByNoticeType(String userId,Integer flag) {
		// 通知公告列表
		List<NoticeTypeVo> list = noticeTypeMapper.queryUnReadNoticeType(userId,flag);
		return list;
	}

	@Override
	public NoticeUnReadVo queryNoticeUnRead(String userId, Integer flag) {
		NoticeUnReadVo noticeUnReadVo = new NoticeUnReadVo();
		Integer total = noticeMapper.queryUnReadTotal(userId,flag);
		List<NoticeVo> list = noticeMapper.queryNoticeUnRead(userId,flag);
		noticeUnReadVo.setCount(total);
		noticeUnReadVo.setNoticeVos(list);

		return noticeUnReadVo;
	}

	@Override
	public List<NoticeVo> getMessageListPage(UserVo userVo,Integer type, Integer readFlag) {
		/*PageHelper.startPage(pp.getPageNo(), pp.getPageSize());*/
		List<NoticeVo> list = noticeMapper.queryNoticeByFlag(userVo.getId(),type,readFlag);
		for(NoticeVo noticeVo:list){
			noticeVo.setCreateUser(userVo.getName());
			noticeVo.setDepartmentName(userVo.getDepartmentName());
			List<AttachFileVo> attachFileVos = attachFileMapper.queryAttachFileByNoticeId(noticeVo.getId());
			noticeVo.setAttachFileVos(attachFileVos);
		}
		return list;
	}

	@Override
	public void delUserNoticeByIds(String userId,Integer flag, List<String> typeIds) {
		List<UserNoticeVo> userNoticeVos = userNoticeMapper.queryUserNoticeByTypeId(userId ,flag,typeIds);
		if(userNoticeVos != null){
			for(UserNoticeVo userNoticeVo : userNoticeVos){
				userNoticeMapper.deleteByPrimaryKey(userNoticeVo);
			}
		}
	}

	@Override
	public void delReceiveNoticeByIds(String userId, List<String> Ids) {
		List<UserNoticeVo> userNoticeVos = userNoticeMapper.queryUserNotice(userId ,Ids);
		if(userNoticeVos != null){
			for(UserNoticeVo userNoticeVo : userNoticeVos){
				userNoticeMapper.deleteByPrimaryKey(userNoticeVo);
			}
		}
	}

	@Override
	public void updateNoticeFlag(String userId, String ids) {
		String[] idArr = ids.split(",");
		for(String id : idArr){
			UserNoticeVo userNoticeVo = userNoticeMapper.queryUserNoticeByParams(userId,id);
			userNoticeVo.setFlag(1);
			UserNotice userNotice = Convert.convert(UserNotice.class, userNoticeVo);
			userNoticeMapper.updateByPrimaryKey(userNotice);
		}
	}

	@Override
	public String getRemindMessage() {
		return noticeMapper.queryRemindMessage();
	}
}
