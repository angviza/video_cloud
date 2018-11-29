package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.SysmenuVo;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Sysmenu;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface SysmenuMapper extends Mapper<Sysmenu> , MySqlMapper<Sysmenu>{

    /**
     * 根据角色id查询菜单列表
     * @param roleId
     * @return
     */
    List<SysmenuVo> selectMenuByRoleId(Map<String ,Object> param);

    /**
     * 根据用户id查询菜单列表
     * @param param
     * @return
     */
    List<SysmenuVo> selectMenuByUserId(Map<String, Object> param);

    /**
     * 根据用户id查询自定义菜单列表
     * @param param
     * @return
     */
    List<SysmenuVo> selectCustomerMenuByUserId(Map<String, Object> param);

    /**
     * 根据多个菜单id查询当前节点及所有父节点的树形菜单数据
     * @param idArr
     * @return
     */
    List<SysmenuVo> selectMenuTreeByIds(String idArr);

    List<SysmenuVo> selectSysmenusList();

	List<SysmenuVo> selectByParam(Map<String, Object> param);
}