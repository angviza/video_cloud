package com.hdvon.sip.video.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import com.hdvon.nmp.exception.SipServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesUtils {
		/**
	     * 缺省配置文件名
	     */
	    public static final String DEFAULT_CONFIG = "/jni.properties";

	    /**
	     * 读出的属性
	     */
	    private static Properties config;

	    /**
	     * 私有方法，单对象模式；
	     */
	    private PropertiesUtils() {
	    }
	    /**
	     * 初始化项目属性；
	     */
	    static {
	        init();
	    }

	    /**
	     * 当属性未初始化时，读取属性文件进行初始化 ，使用system.out输出调试信息；
	     */
	    @SuppressWarnings("rawtypes")
	    private static void init() {
	        try {
	            config = new Properties();
				Class cfgClass = Class.forName("com.hdvon.sip.video.utils.PropertiesUtils");
	            if (cfgClass == null) {
	                log.info("com.hdvon.sip.video.utils.PropertiesUtils: cfgClass is null");
	            }
	            log.info("com.hdvon.sip.video.utils.PropertiesUtils: cfgClass loaded ");
	            InputStream is = cfgClass.getResourceAsStream(DEFAULT_CONFIG);
	            if (is == null) {
	                throw new RuntimeException("Cannot found config file, please check it out and try again.");
	            } else {
	                log.info("com.hdvon.sip.video.utils.PropertiesUtils:  Getted input stream of configuration file");
	                try {
	                    config.load(is);
	                    log.info("com.hdvon.sip.video.utils.PropertiesUtils:  successfully loaded config.properties file");
	                } catch (IOException e) {
	                    log.info("com.hdvon.sip.video.utils.PropertiesUtils:  Cannot configure system param, please check out the gzdec-config.properties file");
	                    e.printStackTrace();
	                }
	            }
	        } catch (ClassNotFoundException e) {
	            log.info("com.hdvon.sip.video.utils.PropertiesUtils:Cannot load class com.hdvon.sip.video.utils.PropertiesUtils");
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 取得所有配置，
	     *
	     * @return 返回所有配置
	     */
	    public static Properties getProperties() {
	        if (config == null) {
	            log.info("Can not find jni properties ");
	            throw new SipServiceException("没有找到属性值 ");
	        } else {
	            return config;
	        }
	    }

	    /**
	     * 返回一个属性值
	     *
	     * @param propertyName 属性名
	     * @return 返回指定属性名的值
	     */
	    public static String getProperty(String propertyName){
	        if (config == null) {
	            log.info("Can not find jni properties ");
	            throw new SipServiceException("没有找到属性值 ");
	        } else {
	            return config.getProperty(propertyName);
	        }
	    }

	    /**
	     * 返回指定的名称的属性值，
	     *
	     * @param propertyName
	     * @param defaultValue
	     * @return String类型的属性值；
	     */
	    public static String getProperty(String propertyName, String defaultValue) {
	        if (config == null) {
	            log.info("Can not find jni properties ");
	            throw new SipServiceException("没有找到属性值 ");
	        } else {
	            return config.getProperty(propertyName, defaultValue);
	        }
	    }

	    /**
	     * 返回所有的属性名
	     *
	     * @return 返回指定属性名
	     */
	    @SuppressWarnings("rawtypes")
		public Enumeration getKeys() {
	        if (config == null) {
	            log.info("Can not find jni properties ");
	            throw new SipServiceException("没有找到属性值 ");
	        } else {
	            return config.keys();
	        }
	    }

	    /**
	     * 返回一个布尔型的值
	     *
	     * @param propertyName
	     * @return 返回属性名的bool值
	     */
	    public static boolean getBooleanProperty(String propertyName) {
	        if (config == null) {
	            log.info("Can not find jni properties ");
	            throw new SipServiceException("没有找到属性值 ");
	        } else {
	            String value = config.getProperty(propertyName).toLowerCase();
	            return Boolean.parseBoolean(value);
	        }
	    }

	    /**
	     * 返回一个int型的值
	     *
	     * @param propertyName
	     * @return 返回属性名的数字值；
	     */
	    public static int getIntegerProperty(String propertyName) {
	        if (config == null) {
	            log.info("Can not find jni properties ");
	            throw new SipServiceException("没有找到属性值 ");
	        } else {
	            String value = config.getProperty(propertyName);
	            return Integer.parseInt(value);
	        }
	    }

	    public static void setProperty(String name, String value){
	    	config.setProperty(name, value);
	    }

 }

