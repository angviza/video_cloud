package com.hdvon.face.container;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>spring容器前加载dll文件<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018/8/16<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
@Component
public class CustomApplicationContextInitializer implements ApplicationRunner {


	private static String OPENCV_DLL= "opencv_java320.dll";
	private static String JNICPU_DLL= "FaceSDKJniCPU.dll";
	private static String JNIGPU_DLL= "FaceSDKJniGPU.dll";
	
	
	
    @Value("${dll.path}")
    public String dllPath;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(ApplicationArguments arguments) throws Exception {
		log.info("=========================== 服务启动执行,执行加载数据等操作  =======================");
		

    	//预先加载dll依赖文件
		System.load(dllPath + File.separator +"cublas64_80.dll");
		System.load(dllPath + File.separator +"cudart64_80.dll");
		System.load(dllPath + File.separator +"cudnn64_5.dll");
		System.load(dllPath + File.separator +"curand64_80.dll");
		
		
		System.load(dllPath + File.separator +"msvcp140.dll");
		System.load(dllPath + File.separator +"vcruntime140.dll");
		System.load(dllPath + File.separator +"gflags.dll");
		System.load(dllPath + File.separator +"glog.dll");
		
	
		System.load(dllPath + File.separator +"libprotobuf.dll");
		
    	System.load(dllPath + File.separator +"libgcc_s_seh-1.dll");
    	
    	System.load(dllPath + File.separator +"libquadmath-0.dll");
    	
    	System.load(dllPath + File.separator +"libgfortran-3.dll");
    	
    	System.load(dllPath + File.separator +"libopenblas.dll");
    	

    	
		System.load(dllPath + File.separator + "opencv_world310.dll");
		System.load(dllPath + File.separator + OPENCV_DLL);
		System.load(dllPath + File.separator + JNICPU_DLL);
		System.load(dllPath + File.separator + JNIGPU_DLL);
		
		
		log.info("加载DLL/SO类库完成");
	}

	
}
