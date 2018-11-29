package com.hdvon.face.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.hdvon.face.exception.FaceException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util{
	/**
	 * 将base64位转换成字节流
	 * @param base64Data
	 * @return
	 */
	public static byte[] trainByteByBase64(String base64Data) throws FaceException{
		BASE64Decoder d = new BASE64Decoder();
        byte[] bytes = null;
		try {
			bytes = d.decodeBuffer(base64Data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new FaceException("face base64Date transform btye type exception ");
		}
		return bytes;
	}
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

    public static void main(String[] args) {
    	String strImg = getImageStr("D:/4a36acaf2edda3cc.jpg");
        System.out.println(strImg);

        generateImage(strImg, "E://upload//portrait");

	}

}
