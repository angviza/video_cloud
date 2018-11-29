package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.NoticeTypeVo;
import com.hdvon.nmp.vo.NoticeUnReadVo;
import com.hdvon.nmp.vo.NoticeVo;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>通知公告表 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface INoticeService{

    /**
     * 分页查询公告信息(发件箱)
     * @param pp
     * @param username
     * @param type
     * @param theme
     * @return
     */
    PageInfo<NoticeVo> getNoticePage(PageParam pp, String username , Integer type, String theme, String loginName);

    /**
     * 分页查询公告信息(发件箱)
     * @param pp
     * @param username
     * @param type
     * @param theme
     * @return
     */
    PageInfo<NoticeVo> getReceiveMessagePage(PageParam pp, String username , Integer type, String theme, String userId);

    /**
     * 公告列表
     * @param type
     * @param readFlag
     * @return
     */
    List<NoticeVo> getMessageListPage(UserVo loginUser, Integer type, Integer readFlag);

    /**
     * 保存公告信息
     * @param userVo
     * @param noticeVo
     * @return
     */
    void saveNotice(UserVo userVo,NoticeVo noticeVo);

    /**
     * 更新公告信息状态
     * @param userId
     * @param id
     * @return
     */
    void updateNoticeFlag(String userId,String ids);

    /**
     * 删除公告信息（发布人）
     * @param userVo
     * @param ids
     * @return
     */
    void delNotice(UserVo userVo , List<String> ids);

    /**
     * 删除公告信息
     * @param userId
     * @param flag
     * @param typeIds
     * @return
     */
    void delUserNoticeByIds(String userId , Integer flag,List<String> typeIds);

    /**
     * 收件箱删除
     * @param userId
     * @param Ids
     * @return
     */
    void delReceiveNoticeByIds(String userId , List<String> Ids);

    /**
     * 编辑/查看公告信息(发件人)
     * @param userVo
     * @param id
     * @return
     */
    NoticeVo getNoticeInfoByPublish(UserVo userVo, String id);

    /**
     * 编辑/查看公告信息(收件人)
     * @param userVo
     * @param id
     * @return
     */
    NoticeVo queryNoticeInfoByReceive(UserVo userVo, String id);

    /**
     * 公告信息提示
     * @param userId
     * @return
     */
    List<NoticeTypeVo> queryMessageByNoticeType(String userId,Integer flag);

    /**
     * 统计当前用户未读信息
     * @param userId
     * @return
     */
    NoticeUnReadVo queryNoticeUnRead (String userId, Integer flag);

    /**
     * 用户登录前的维护公告提醒
     * @return
     */
    String getRemindMessage ();
}
