package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.OptMenuVo;
import com.hdvon.nmp.vo.SysmenuVo;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>系统功能表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ISysmenuService{

    /**
     * 查询系统菜单列表
     * @param search
     * @return
     */
    List<SysmenuVo> getSysMenus(String search);
    /**
     * 查看菜单详情
     * @param id 菜单id
     * @return
     */
    SysmenuVo getSysMenuDetail(String id);

    /**
     * 根据角色id查询菜单列表
     * @param param
     * @return
     */
    List<SysmenuVo> getMenuVoByRoleId(Map<String ,Object> param);

    /**
     * 根据用户id查询菜单列表
     * @param userVo
     * @return
     */
    List<SysmenuVo> getMenuVoByUserId(UserVo userVo);


    List<SysmenuVo> getSysmenusList();
    
    /**
     * 保存或者修改系统菜单
     * @param userVo
     * @param sysmenuVo
     */
    void saveSysmenu(UserVo userVo, SysmenuVo sysmenuVo);


    /**
     * 查询自定义菜单列表。
     * @param userVo
     * @return
     */
    List<SysmenuVo> getCustomerMenu(UserVo userVo);

    /**
     * 拖动菜单
     * @param userId
     * @param pid
     * @param list
     */
    void drag(String userId, String pid, List<OptMenuVo> list);
    
    /**
     * 功能菜单
     * @param param
     * @return
     */
	List<SysmenuVo> getMenuFunctionByParam(Map<String, Object> param);
}
