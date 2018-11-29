package com.hdvon.nmp.service;

import java.util.List;

import com.hdvon.nmp.vo.OrganizationTreeVo;

/**
 * <br>
 * <b>功能：</b>项目与部门关联表 服务类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-6-4<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IProjectDepartmentService{
	/*
	 * 获取行政区域树
	 * @param pid 父节点id
	 * @return
	 * @throws UserException
	 */
	List<OrganizationTreeVo> getProjectTree();
}
