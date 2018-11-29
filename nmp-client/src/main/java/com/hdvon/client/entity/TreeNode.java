package com.hdvon.client.entity;

import java.io.Serializable;
/**
 * 保存树节点的名称关系
 * @author wanshaojian
 *
 */
public class TreeNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前节点id
	 */
	private String nodeId;
	/**
	 * 当前节点名称
	 */
	private String nodeName;
	/**
	 * 保存当前节点的所有上级节点关系，已";"分割
	 */
	private String text;
	
}
