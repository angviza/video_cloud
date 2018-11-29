package com.hdvon.nmp.generate.util;

public class PropertyConfig {
		/**
	     * 缺省配置文件名
	     */
	 	//public static final String CONFIG_CAMERA_EXPORT = "/excel/camera/expCameraAttr.properties";
	    
	    public static final String CONFIG_CAMERA_ATTR = "/excel/camera/cameraAttr.properties";
	    
	    public static final String CONFIG_CAMERA = "/excel/camera/camera.properties";
	    
	    public static final String CONFIG_CAMERA_DIC = "/excel/camera/cameraDic.properties";

	    public static final String CONFIG_ORG_IMPORT = "/excel/org/impOrganization.properties";
	    
	    public static final String CONFIG_ORG_VIR_IMPORT = "/excel/org/impVirtualOrganization.properties";
	    
	    public static final String CONFIG_DEPT_IMPORT = "/excel/org/impDepartment.properties";
	    
	    public static final String CONFIG_ORG_DIC = "/excel/org/orgDic.properties";
	    
	    public static final String CONFIG_ORG = "/excel/org/org.properties";
	    
	    /**
	     * properties文件类型
	     */
	    //public static final String PROPERTY_CAMERA_EXPORT = "exportCamera";
	    
	    public static final String PROPERTY_CAMERA_ATTR = "cameraAttr";
	    
	    public static final String PROPERTY_CAMERA = "camera";
	    
	    public static final String PROPERTY_CAMERA_DIC = "cameraDic";

	    public static final String PROPERTY_ORG_IMPORT = "impOrganization";
	    
	    public static final String PROPERTY_ORG_VIR_IMPORT = "impVirtualOrganization";
	    
	    public static final String PROPERTY_DEPT_IMPORT = "impDepartment";
	    
	    public static final String PROPERTY_ORG_DIC = "orgDic";
	    
	    public static final String PROPERTY_ORG = "org";

	    /**
	     * 正则
	     */
	    public static final String REGX_GBBM = "\\d{20}";//国标编码
	    public static final String REGX_INT = "^\\d+$";//整数
	    public static final String REGX_IP = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";//ip地址
	    public static final String REGX_DOUBLE = "^[-//+]?//d+(//.//d*)?|//.//d+$";//double类型
	    public static final String REGX_VIRTUAL_CODE = "\\d{20}";//虚拟组织编码
	    public static final String REGX_JD = "^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,6}|1[0-7]?\\d{1}\\.\\d{1,6}|180\\.0{1,6})$";//经度
	    public static final String REGX_WD = "^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,6}|90\\.0{1,6})$";//纬度
	    public static final String REGX_PHONE = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";
	    public static final String REGX_MOBILE="^((\\(\\d{3}\\))|(\\d{3}\\-))?13[0-9]\\d{8}|15[89]\\d{8}";
}
