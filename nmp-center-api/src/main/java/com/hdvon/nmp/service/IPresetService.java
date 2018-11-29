package com.hdvon.nmp.service;

import com.hdvon.nmp.common.CruiseBean;
import com.hdvon.nmp.common.PresentBean;
import com.hdvon.nmp.common.PresentListBean;

/**
 * <br>
 * <b>功能：</b>预置位和巡航预案接口<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-11-13<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPresetService{


    /**
     * 新增预置位
     * @param vo
     * @return
     */
    public void savePreset(PresentBean vo);

    /**
     * 保存地址
     * @param vo
     */
    public void deletePreset(PresentBean vo);
    
    /**
     * 批量更新预置位信息
     * @param vo
     */
    public void batchUpdatePresent(PresentListBean vo);
    

    /**
     * 新增预案与预置位关联关系
     * @param vo
     */
    public void savePlanPresent(CruiseBean vo);

    /**
     * 跟新预案与预置位关联关系
     * @param vo
     */
    public void updatePlanPresent(CruiseBean vo);
    
    /**
     * 删除预案与预置位关联关系
     * @param vo
     */
    public void deletePlanPresent(CruiseBean vo);
    

}
