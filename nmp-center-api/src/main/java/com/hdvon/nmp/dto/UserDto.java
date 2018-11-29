package com.hdvon.nmp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {

    private String sysroleId;

    private String resourceRoleId;

    private String departmentId;

    private String account;
    
    private String name;

    private List<String> userIds;
}
