package com.hdvon.nmp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.util.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageUtil {

    
	/**
     * 保存文件，直接以base64Data形式
     * @param base64Data
     * @param filePath 文件保存绝对路径
     * @param fileName 文件名称
     * @throws IOException
     */
    public static void saveImg(String base64Data,String filePath,String fileName) throws IOException {
        if(base64Data == null || "".equals(base64Data)){
        	throw new ImageException("上传失败，上传图片数据为空!");
        }
 
          try {
        	  generateImage(base64Data, filePath, fileName);
          }catch(Exception ee){
     	 		throw new ImageException("用户上传头像异常！");
          }	 
    }
    
    private static void createFile(String path) throws IOException {
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdir();
        }
    }
    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path,String fileName) {
        if(imgStr == null){
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try{
            //解密
            byte[] b = decoder.decodeBuffer(imgStr);
            //处理数据
            for (int i = 0;i<b.length;++i){
                if(b[i]<0){
                    b[i]+=256;
                }
            }
        	createFile(path);    

        	String fullPath = path + File.separator + fileName;
            
            OutputStream out = new FileOutputStream(fullPath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     * @return
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
        	
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
