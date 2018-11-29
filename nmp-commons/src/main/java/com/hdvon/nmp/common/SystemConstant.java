package com.hdvon.nmp.common;
/**
 * 常量池
 * @author wanshaojian
 *
 */
public class SystemConstant {
	/**
	 * redis 保存前端地址树资源key
	 */
	public static final String redis_client_department_key = "department_client";
	
	/**
	 * redis 保存前端地址树资源key
	 */
	public static final String redis_client_address_cameraPermission_key = "cameraPermission_address_client";
	
	/**
	 * redis 保存地址树资源key
	 */
	public static final String redis_address_cameraPermission_key = "cameraPermission_address";
	
	/**
	 * redis 保存行政区域树资源key	
	 */
	public static final String redis_organization_cameraPermission_key = "cameraPermission_organization";
	
	/**
	 * redis 保存项目分组树资源key	
	 */
	public static final String redis_project_cameraPermission_key = "cameraPermission_project";
	
	/**
	 * redis 保存自定义分组树资源key	
	 */
	public static final String redis_group_cameraPermission_key = "cameraPermission_group";
	
	/**
	 * redis 保存当前当前角色的拥有的摄像机资源key	
	 */
	public static final String redis_role_cameraPermission_key = "cameraPermission_role";
	
	/**
	 * redis 保存地址树key
	 */
	public static final String redis_addressTree_key = "addressTree";
	
	/**
	 * redis 保存行政区域树key	
	 */
	public static final String redis_organizationTree_key = "organizationTree";
	
	/**
	 * redis 保存自定义分组树key	
	 */
	public static final String redis_cameraGroupTree_key = "cameraGroupTree";
	
	/**
	 * redis 保存地址树资源key
	 */
	public static final String redis_hots_camera_key = "camera_hots";
	
	/**
	 * 超管账号
	 */
	public static final String super_Account ="admin";
	/**
	 * 超管账号标志
	 */
	public static final int super_account_flag = 1;
	
	public static final String redis_collect_camera_key = "camera_collect";

	
	/**
	 * ElasticSearch 中摄像机索引名称
	 */
	public static final String es_cameraPermission_index="hdvon_camera_permission";
	public static final String es_cameraPermission_mapping="cameraPermission";
	public static final String cameraPermission_mapping_file ="hdvon_camera_permission.json";
	
	public static final String es_cameragroup_index="hdvon_camera_group";
	public static final String es_cameragroup_mapping="cameraGroup";
	public static final String cameragroup_mapping_file ="hdvon_camera_group.json";
	
	public static final String es_user_cameragroup_index="hdvon_user_camera_group";
	public static final String es_user_cameragroup_mapping="userCameraGroup";
	public static final String user_cameragroup_mapping_file ="hdvon_user_camera_group.json";
	
	public static final String es_camera_index="hdvon_camera";
	public static final String es_camera_mapping="camera";	
	public static final String camera_mapping_file ="hdvon_camera.json";
	/**
	 * 摄像机消息类型   
	 */
	public static final int CameraMsg_type_add = 1 ;
	/**
	 * 摄像机消息类型  
	 */
	public static final int CameraMsg_type_update = 2 ;
	/**
	 * 摄像机消息类型   
	 */
	public static final int CameraMsg_type_del = 3 ;
	
	/**
	 * rabbitMq队列名称 
	 */
	public static final String QUEUE_ES_USER_CAMERA = "QUEUE_ES_USER_CAMERA";
	public static final String QUEUE_ES_CAMERA = "QUEUE_ES_CAMERA";
	public static final String QUEUE_ES_PLAN_CAMERA = "QUEUE_ES_PLAN_CAMERA";
	public static final String QUEUE_ES_CAMERA_GROUP = "QUEUE_ES_CAMERA_GROUP";
	
	
	public static final int success_stauts = 200;
	
	
	public static final String CONTRACT_FANOUT = "CONTRACT_FANOUT";
	public static final String CONTRACT_TOPIC = "CONTRACT_TOPIC";
	public static final String CONTRACT_DIRECT = "CONTRACT_DIRECT";
	
	/**
	 * CONTRACT_TOPIC		kafaka消息生产者的发送类别为CONTRACT_TOPIC1
	 * CAMERA_KEY			指定kafaka消息的 key为CAMERA
	 */
	public static final String CONTRACT_TOPIC1 = "CONTRACT_TOPIC1";
	public static final String CAMERA_KEY = "CAMERA";
	

	public static final String CONTRACT_TOPIC2 = "CONTRACT_TOPIC2";
	public static final String CONTRACT_TOPIC3 = "CONTRACT_TOPIC3";
	public static final String CONTRACT_TOPIC4 = "CONTRACT_TOPIC4";

	
	public static final String BATCH_CONTRACT_TOPIC = "BATCH_CONTRACT_TOPIC";
	
	public static final String SIPUSER_CONTRACT_TOPIC = "SIPUSER_CONTRACT_TOPIC";
	/**
	 * 球机巡航预案topic名称
	 */
	public static final String CRUISE_TOPIC = "CRUISE_TOPIC";
	/**
	 * 预置位topic名称
	 */
	public static final String PRESET_TOPIC = "PRESET_TOPIC";
	/**
	 * 预置位查询topic名称
	 */
	public static final String PRESET_QUERY_TOPIC = "PRESET_QUERY_TOPIC";
    /**
     * 摄像机权限时效性 永久
     */
    public static final int PERMANENT = 1;
    /**
     * 摄像机权限时效性 临时
     */
    public static final int TEMPORARY = 0;
    /**
     * 摄像机是否永久有效常量
     */
    public static final String IS_PERMANENT = "isPermanent";
	
    
	//黑名单标志
	public static final int BLACK_USER = 1;
	//白名单标志	
	public static final int WHITE_USER = 2;
	//普通标志
	public static final int ORDINARY_USER = 0;
	
	
	//是否摄像机树形分组标志  1:是 0:否
	public static final int TREE_GROUP_FLAG = 1;
	
	//是否摄像机树形分组标志
	public static final int TREE_NOTGROUP_FLAG = 0;
	
    /**
     * 摄像机权限时效性 永久
     */
    public static final int ES_PERMANENT = 1;
    /**
     * 摄像机权限时效性 临时
     */
    public static final int ES_NOT_PERMANENT = 2;
    
   
    //地址节点
    public static final String TREENODE_ADDRESSNODE_KEY = "redis_addressnode_key";
    
    //行政区划节点
    public static final String TREENODE_ORGNODE_KEY = "redis_orgnode_key";
    
    // 项目分组节点
    public static final String TREENODE_PROJECTNODE_KEY = "redis_projectnode_key";
   
    // 自定义分组节点
    public static final String TREENODE_GROUPNODE_KEY = "redis_groupnode_key";
    
    // 树节点放到redis 有效期为30天
    public static final long TREENODE_EXPIRE_SECONDS = 60 * 60 * 24 * 30L;

	
}
