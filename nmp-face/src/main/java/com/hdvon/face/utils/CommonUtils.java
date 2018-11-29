package com.hdvon.face.utils;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.util.ResourceUtils;

public class CommonUtils {
	/**
	 * 获取项目根目录
	 * @return
	 */
	public static String getAbsolutePath() {
		File path = null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!path.exists()) {
			path = new File("");
		}
		String rootPath = null;
		String absolutePath = path.getAbsolutePath();
		int lastIndex = absolutePath.lastIndexOf("\\");
		String dic = absolutePath.substring(lastIndex+1, absolutePath.length());
		if(!"classes".equals(dic)) {
			rootPath = absolutePath.substring(0,lastIndex)+"/classes";
		}else {
			rootPath = absolutePath;
		}
		return rootPath.replace("\\", "/");
	}
	
	public static void main(String[] args) {
		CommonUtils.getAbsolutePath();
	}
}
