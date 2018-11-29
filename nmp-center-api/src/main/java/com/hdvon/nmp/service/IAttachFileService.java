package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.AttachFileVo;
import com.hdvon.nmp.vo.NoticeVo;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>上传文件表 服务类<br>
 * <b>作者：</b>xuguocai<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IAttachFileService{
    /**
     * 保存附近信息
     * @param userVo
     * @param attachFileVo
     * @return
     */
    AttachFileVo saveAttachFile(UserVo userVo, AttachFileVo attachFileVo);
}
