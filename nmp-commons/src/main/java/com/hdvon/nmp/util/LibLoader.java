package com.hdvon.nmp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>本地类库加载的工具类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/5/21<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
public class LibLoader {
	public static void loadLib(String libName) {
		String resourcePath = File.separator + "native" + File.separator + libName;
		
		try {
			
			String folderName = System.getProperty("java.io.tmpdir") + File.separator + "lib" + File.separator;
			File folder = new File(folderName);
			folder.mkdirs();
			
			File libFile = new File(folder, libName);
			
			/**
			 * 注意：如果加载过一次dll并且缓存到c盘用户目录下，之后直接读缓存下来的dll，会出现缓存在c盘目录下的dll有可能跟项目资源路径下的dll不一致的情况
			 */
			/***************************  Update by huanhongliang	2018/07/16  **************************/
			//if (libFile.exists()) {//文件已存在
				
				//加载指定路径下的本地类库
				//System.load(libFile.getAbsolutePath());
			//} else {
			/********************************************************************************************/
			
				//创建类资源的路径实例
				ClassPathResource res = new ClassPathResource(resourcePath);
				
				//InputStream in = LibLoader.class.getResourceAsStream(resourcePath);
				//读取classpath下的文件，如：\resources\native\
				InputStream in = res.getInputStream();
				
				//将读取流复制到指定的临时路径下，一般是C:\Users\Administrator\AppData\Local\Temp\
				FileUtils.copyInputStreamToFile(in, libFile);
				
				//关闭流
				in.close();
				
				//加载指定路径下的本地类库
				System.load(libFile.getAbsolutePath());
			//}
			
			log.info("====================== 加载DLL/SO类库成功  ====================");
		} catch (Throwable e) {
			e.printStackTrace();
			log.error("加载DLL/SO类库异常："+e);
			
			/****************************  Update by huanhongliang	2018/07/16  **************************/
			if (e instanceof FileNotFoundException) {
				throw new RuntimeException("加载dll/so文件失败：<1>另一个程序正在使用此文件，进程无法访问！<2>dll/so文件路径不存在！");
			}
			
			if (e.getMessage().indexOf("libraries") != -1) {
				throw new RuntimeException("加载dll/so文件失败：未找到所依赖的库文件，请检查C++运行环境以及运行库是否正确安装！");
			}
			
			throw new RuntimeException("加载dll/so文件失败：<1>请检查文件操作权限！<2>如果设置了安全管理策略，请检查'java.io.tmpdir'的系统属性设置！");
			/*********************************************************************************************/
		}
		
	}
}
