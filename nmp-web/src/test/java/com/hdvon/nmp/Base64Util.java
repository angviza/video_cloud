package com.hdvon.nmp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.util.ImageUtils;
import org.springframework.util.Base64Utils;

import com.hdvon.nmp.util.ImageException;
import com.hdvon.nmp.util.ImageUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util{
    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
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
        	File file=new File(path);    
        	if(!file.exists()){    
        		file.createNewFile();    
        	}    
            String fileName = UUID.randomUUID().toString() + ".jpg";
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
	/**
     * 保存文件，直接以base64Data形式
     * @param base64Data
     * @param path 文件保存绝对路径
     * @throws IOException
     */
    public static String saveImg(String base64Data,String filePath) throws IOException {
   	 	String dataPrix = "";
   	 	String data = "";	  
        if(base64Data == null || "".equals(base64Data)){
        	throw new ImageException("上传失败，上传图片数据为空!");
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
        byte[] bs = Base64Utils.decodeFromString(data);
        try{

            FileUtils.writeByteArrayToFile(new File(filePath, fileName), bs);  
            
            
            return fileName;
        }catch(Exception ee){
       	 	throw new ImageException("用户上传头像异常！");
        }	 
    }
    public static void main(String[] args) {
    	String strImg = getImageStr("D:/4a36acaf2edda3cc.jpg");
        System.out.println(strImg);

        generateImage(strImg, "E://upload//portrait");

	}
}
