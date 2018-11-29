package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hdvon.nmp.entity.UserSysmenu;
import com.hdvon.nmp.mapper.UserSysmenuMapper;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.BeanUtils;
import tk.mybatis.mapper.entity.Example;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.config.redis.BaseRedisDao;
import com.hdvon.nmp.entity.Sysmenu;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.SysmenuMapper;
import com.hdvon.nmp.mapper.UserMapper;
import com.hdvon.nmp.service.ISysmenuService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>系统功能表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class SysmenuServiceImpl implements ISysmenuService {

    @Autowired
    private SysmenuMapper sysmenuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSysmenuMapper userSysmenuMapper;
    @Autowired
    private BaseRedisDao redisDao;

    @Override
    public List<SysmenuVo> getSysMenus(String search) {
        /*  Example sme = new Example(Sysmenu.class);*/
        List<SysmenuVo> sysmenus = null;
        Map<String, Object> param = new HashMap<String, Object>();
        if (StrUtil.isNotEmpty(search)) {
            //sme.createCriteria().andLike("name", "%"+search+"%");
            param.put("name", search);
        } else {
        	/*sme.createCriteria();
        	sysmenus = sysmenuMapper.selectByExample(sme);
        	List<SysmenuVo> sysmenuVos = BeanHelper.convertList(SysmenuVo.class, sysmenus);*/
            List<SysmenuVo> sysmenuVos = sysmenuMapper.selectByParam(param);
            return sysmenuVos;
        }
        sysmenus = sysmenuMapper.selectByParam(param);

        String idArr = "";
        for (int i = 0; i < sysmenus.size(); i++) {
            SysmenuVo sysmenu = sysmenus.get(i);
            if (sysmenu.getId() != null) {
                idArr += (sysmenu.getId() + ",");
            }
        }
        idArr = idArr.substring(0, idArr.length() - 1);

        List<SysmenuVo> treemenus = sysmenuMapper.selectMenuTreeByIds(idArr);
        return treemenus;
    }

    @Override
    public SysmenuVo getSysMenuDetail(String id) {
        Sysmenu sysmenu = sysmenuMapper.selectByPrimaryKey(id);
        SysmenuVo sysmenuVo = Convert.convert(SysmenuVo.class, sysmenu);
        return sysmenuVo;
    }

    @Override
    public List<SysmenuVo> getMenuVoByRoleId(Map<String, Object> param) {
        return sysmenuMapper.selectMenuByRoleId(param);
    }

    @Override
    public List<SysmenuVo> getMenuVoByUserId(UserVo userVo) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userVo.getId());
        param.put("isAdmin", userVo.isAdmin());
        List<SysmenuVo> userMenu = sysmenuMapper.selectMenuByUserId(param);
        return userMenu;
    }

    @Override
    public List<SysmenuVo> getSysmenusList() {
        return sysmenuMapper.selectSysmenusList();
    }

    @Override
    public void saveSysmenu(UserVo userVo, SysmenuVo sysmenuVo) {
        Sysmenu sysmenu = Convert.convert(Sysmenu.class, sysmenuVo);
        if (StrUtil.isNotBlank(sysmenuVo.getId())) {//修改
            if (StrUtil.isNotBlank(sysmenuVo.getUrl())) {
                Example eUrl = new Example(Sysmenu.class);
                Example.Criteria criteria = eUrl.createCriteria();
                criteria.andEqualTo("url", sysmenuVo.getUrl());
                criteria.andEqualTo("name", sysmenuVo.getName());
                criteria.andNotEqualTo("id", sysmenuVo.getId());
                int countUrl = sysmenuMapper.selectCountByExample(eUrl);
                if (countUrl > 0) {
                    throw new ServiceException("系统菜单请求地址和名称已存在！");
                }
            }
            if (StrUtil.isNotBlank(sysmenuVo.getCode())) {
                Example eCode = new Example(Sysmenu.class);
                eCode.createCriteria().andEqualTo("code", sysmenuVo.getCode()).andNotEqualTo("id", sysmenuVo.getId());
                int countCode = sysmenuMapper.selectCountByExample(eCode);
                if (countCode > 0) {
                    throw new ServiceException("系统菜单编码已存在！");
                }
            }
            sysmenuMapper.updateByPrimaryKeySelective(sysmenu);
        }
    }


    @Override
    public List<SysmenuVo> getCustomerMenu(UserVo userVo) {
        //查询用户自定义菜单权限
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userVo.getId());
        param.put("isAdmin", userVo.isAdmin());
        List<SysmenuVo> voLst = sysmenuMapper.selectCustomerMenuByUserId(param);
        return voLst;
//        Example userMenuExample = new Example(UserSysmenu.class);
//        userMenuExample.createCriteria().andEqualTo("userId", userVo.getId());
//        List<UserSysmenu> us = userSysmenuMapper.selectByExample(userMenuExample);
//        Map<String,UserSysmenu> menuMap = new HashMap<>();
//        for (int i = 0 ; i < us.size() ; i++){
//            menuMap.put()
//        }
//
//
//
//
//        if ((voLst == null) || voLst.isEmpty()) {
//            return null;
//        }
//        // 信息整合。
//        SysmenuVo vo;
//        Integer YES = 1;
//        Integer NO = 0;
//        for (int size = voLst.size(), index = size - 1; index >= 0; --index) {
//            vo = voLst.get(index);
//
//            Example exm = new Example(UserSysmenu.class);
//            exm.createCriteria()
//                    .andEqualTo("userId", userId)
//                    .andEqualTo("sysmenuId", vo.getId());
//            UserSysmenu us = this.userSysmenuMapper.selectOneByExample(exm);
//            if (us != null) {
//                if (YES.equals(us.getHide())) {
//                    voLst.remove(index);
//                } else {
//                    vo.setPid(us.getSysmenuPid());
//                    vo.setName(us.getSysmenuName());
//                    if (us.getOrderby() != null) {
//                        vo.setOrderby(us.getOrderby());
//                    } // if
//                }
//            } // if
//        } // for
//        Collections.sort( voLst, new Comparator<SysmenuVo>() {
//            @Override
//            public int compare(SysmenuVo o1, SysmenuVo o2) {
//                if (StrUtil.isEmpty(o1.getPid())) {
//                    return -1;
//                } else if (StrUtil.isEmpty(o2.getPid())) {
//                    return 1;
//                } else if (o1.getPid().equals(o2.getPid())) {
//                    if (o1.getOrderby() == null) {
//                        return -1;
//                    } else if (o2.getOrderby() == null) {
//                        return 1;
//                    } else {
//                        return o1.getOrderby().compareTo(o2.getOrderby());
//                    }
//                } else {
//                    return o1.getPid().compareTo(o2.getPid());
//                }
//            }
//        });
//        return voLst;
    }

    @Override
    public void drag(String userId, String pid, List<OptMenuVo> list) {
        Integer YES = 1;
        Integer NO = 0;
        Map<String,OptMenuVo> menuMap = new HashMap<>();
        for (OptMenuVo item : list) {
            menuMap.put(item.getId(),item);
        }

        for (OptMenuVo item : list) {
            Example exam = new Example(UserSysmenu.class);
            exam.createCriteria().andEqualTo("userId", userId).andEqualTo("sysmenuId", item.getId());
            UserSysmenu old = userSysmenuMapper.selectOneByExample(exam);
            // 若未存在。
            if (old == null) {
                UserSysmenu usvo = new UserSysmenu();
                usvo.setHide(NO);
                usvo.setId(IdGenerator.nextId());
                usvo.setSysmenuId(item.getId());
                usvo.setSysmenuName(item.getName());
                usvo.setSysmenuPid(pid);
                usvo.setUserId(userId);
                usvo.setOrderby(item.getOrderOpt());
                usvo.setId(IdGenerator.nextId());
                userSysmenuMapper.insert(usvo);
            }
            // 若已存在。
            else {
                if (YES.equals(old.getHide()) || (!old.getSysmenuName().equals(item.getName())) || (!old.getSysmenuPid().equals(pid))) {
                    old.setHide(NO);
                    old.setSysmenuName(item.getName());
                    old.setSysmenuPid(pid);
                    old.setOrderby(item.getOrderOpt());

                    if(old.getUserId() != null && old.getSysmenuId() != null){
                        Example exm = new Example(UserSysmenu.class);
                        exm.createCriteria()
                                .andEqualTo("userId", old.getUserId())
                                .andEqualTo("sysmenuId", old.getSysmenuId());
                        UserSysmenu menu = this.userSysmenuMapper.selectOneByExample(exm);
                        if (menu != null) {
                            BeanUtils.copyProperties(old, menu);
                            this.userSysmenuMapper.updateByExample(menu, exm);
                        }
                    }

                }
            }
        }

    }

    /**
     * 查询系统功能菜单
     */
	@Override
	public List<SysmenuVo> getMenuFunctionByParam(Map<String, Object> param) {
      List<SysmenuVo> menuList=null;
  	  if(!redisDao.exists(WebConstant.SYSMENU_METHOD)) {
  		  param.put("type", "2");//接口及按钮权限类型
          menuList = sysmenuMapper.selectByParam(param);
          redisDao.addList(WebConstant.SYSMENU_METHOD, menuList);
          redisDao.setExpireTime(WebConstant.SYSMENU_METHOD, WebConstant.KEY_SYSMENU_METHOD);
  	  }else {
  		menuList = (List<SysmenuVo>) redisDao.getList(WebConstant.SYSMENU_METHOD);
  	  }
      return menuList;
	}

}
