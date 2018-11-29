package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="ResourcerolePermissionVo")
public class ResourcerolePermissionVo implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1525381310247936971L;
	
	@ApiModelProperty(value = "可管理部门用户树")
	List<DepartmentUserTreeVo> departmentUserTreeVos;
	
	@ApiModelProperty(value = "资源角色关联的用户")
    List<UserVo> userVos;
}
