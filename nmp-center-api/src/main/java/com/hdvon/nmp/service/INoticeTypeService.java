package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.NoticeTypeVo;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>公告类型表 服务类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface INoticeTypeService{
    /**
     * 公告类型数据
     * @return
     */
    List<NoticeTypeVo> selectNoticeType();
}
