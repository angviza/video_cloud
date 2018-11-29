package com.hdvon.client.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 发送的消息实体
 * @author wanshaojian
 *  4中消息字段具体描述
 *  通用字段   id 自动生成唯一标识
 *        msgId 消息唯一id
 *        content 内容可以为空 
 *  1:资源授权 
 *  	 relationId 角色id  
 *  	 updateIds 跟新用户id集合【多个已‘,’分割】
 *  	 type 1
 *       deleteIds 为空
 *  2:权限预案  
 *  	 relationId 预案id
 *       updateIds 跟新用户id集合【多个已‘,’分割】
 *       type 2
 *       deleteIds 为空
 *  3:自定义分组  
 *  	 relationId 分组id
 *       updateIds 跟新设备deviceId集合【多个已‘,’分割】
 *       type 3
 *       deleteIds 为空
 *  4：用户分配角色
 *  	 relationId 用户id
 *       updateIds 跟新角色id集合【多个已‘,’分割】
 *       type 4
 *       deleteIds 删除的角色集合
 */
@Data
public class CameraMappingMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//操作用户id
	private String content;
	/**
	 * 更新集合 
	 */
	private String updateIds;
	/**
	 * 删除集合
	 */
	private String deleteIds;
	
	
	private Integer type;//1:资源授权  2:权限预案  3:自定义分组  4:用户关联角色

	private String relationId;
	
	private String msgId; //消息id
	
	private List<String> deviceId;
}
