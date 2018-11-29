package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ScreenTemplateVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>分屏管理模板 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IScreenTemplateService{
    /**
     * 保存分屏模板
     * @param userVo
     * @param templateVo
     */
    void saveScreenTemplate(UserVo userVo, ScreenTemplateVo templateVo);
    /**
     * 分页查询分屏模板
     * @param pp
     * @param search
     * @return
     */
    PageInfo<ScreenTemplateVo> getScreenTemplatePages(PageParam pp, Map<String,String> map);
    /**
     * 查询分屏模板列表
     * @param pp
     * @param search
     * @return
     */
    List<ScreenTemplateVo> getScreenTemplateList(Map<String,String> map);
    /**
     * 批量删除分屏模板
     * @param ids
     */
    void delScreenTemplatesByIds(List<String> ids);
    /**
     * 根据id查询单个分屏模板信息
     * @param id
     * @return
     */
    ScreenTemplateVo getScreenTemplateById(String id);
}
