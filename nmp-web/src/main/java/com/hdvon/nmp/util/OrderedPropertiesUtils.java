package com.hdvon.nmp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.generate.util.PropertyConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderedPropertiesUtils {
    /**
     * 读出的属性
     */
    private static OrderedProperties config;

    /**
     * 私有方法，单对象模式；
     */
    private OrderedPropertiesUtils() {
    }
    /**
     * 初始化项目属性；
     */
    static {
        //init();
    }

    /**
     * 当属性未初始化时，读取属性文件进行初始化 ，使用system.out输出调试信息；
     * @param type importCamera:读取导入摄像机的配置；exportCamera：读取导出摄像机的配置；camera：读取摄像机相关功能的公共配置。
     */
    @SuppressWarnings("rawtypes")
    private static void init(String type) {
    	InputStream is = null;
        try {
            config = new OrderedProperties();
			Class cfgClass = Class.forName("com.hdvon.nmp.util.OrderedPropertiesUtils");
            if (cfgClass == null) {
                log.info("com.hdvon.nmp.util.OrderedPropertiesUtils: cfgClass is null");
            }
            log.info("com.hdvon.nmp.util.OrderedPropertiesUtils: cfgClass loaded ");
            
            is = getPropertyFileConfig(cfgClass, type);
            
            if (is == null) {
                throw new RuntimeException("Cannot found config file, please check it out and try again.");
            } else {
                log.info("com.hdvon.nmp.util.OrderedPropertiesUtils:  Getted input stream of configuration file");
                try {
                    config.load(new InputStreamReader(is,"utf-8"));
                    log.info("com.hdvon.nmp.util.OrderedPropertiesUtils:  successfully loaded config.properties file");
                } catch (IOException e) {
                    log.info("com.hdvon.nmp.util.OrderedPropertiesUtils:  Cannot configure system param, please check out the gzdec-config.properties file");
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            log.info("com.hdvon.nmp.util.OrderedPropertiesUtils:Cannot load class com.hdvon.nmp.util.PropertiesUtils");
            e.printStackTrace();
        }
    }

    /**
     * @param cfgClass
     * @param type
     * @return
     */
    private static InputStream getPropertyFileConfig(Class cfgClass, String type) {
    	InputStream is = null;
    	if(PropertyConfig.PROPERTY_CAMERA_ATTR.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_CAMERA_ATTR);
        }else if(PropertyConfig.PROPERTY_CAMERA.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_CAMERA);
        }else if(PropertyConfig.PROPERTY_CAMERA_DIC.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_CAMERA_DIC);
        }else if(PropertyConfig.PROPERTY_ORG_IMPORT.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_ORG_IMPORT);
        }else if(PropertyConfig.PROPERTY_DEPT_IMPORT.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_DEPT_IMPORT);
        }else if(PropertyConfig.PROPERTY_ORG_VIR_IMPORT.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_ORG_VIR_IMPORT);
        }else if(PropertyConfig.PROPERTY_ORG_DIC.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_ORG_DIC);
        }else if(PropertyConfig.PROPERTY_ORG.equals(type)) {
        	is = cfgClass.getResourceAsStream(PropertyConfig.CONFIG_ORG);
        }
    	return is;
    }
    /**
     * 取得所有配置，
     *
     * @return 返回所有配置
     */
    public static OrderedProperties getProperties(String type) {
    	init(type);
        if (config == null) {
            log.info("Can not find properties ");
            throw new ServiceException("没有找到属性文件 ");
        } else {
            return config;
        }
    }
}
