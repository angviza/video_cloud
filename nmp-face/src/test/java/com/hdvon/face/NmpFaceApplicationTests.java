package com.hdvon.face;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSON;
import com.hdvon.FaceLogin.HDFaceInfo;
import com.hdvon.FaceLogin.HDFaceSDK_JNI_CPU;
import com.hdvon.face.exception.FaceException;
import com.hdvon.face.service.FaceService;
import com.hdvon.face.utils.Base64Util;
import com.hdvon.face.vo.HDFaceInfoVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NmpFaceApplicationTests {
	@Autowired
	FaceService faceService;
	

	@Test
	public void getFaceFeatureTest() {
		try{
			String imgPath = "E:\\nmp_java\\nmp\\nmp-face\\src\\test\\java\\1.jpg";
			String base64Data = Base64Util.getImageStr(imgPath);
			HDFaceInfoVo info = faceService.getFaceFeatureBase64(base64Data, FaceConstant.cpuType);
			
			System.out.println("face Feature");
			System.out.println(JSON.toJSONString(info));
		}catch(FaceException e){
			System.out.println(e.getMessage());
		}

	}
//	@Test
//	public void getFaceRectTest() {
//		String imagePath = "E:\\nmp_java\\nmp\\nmp-face\\src\\test\\java\\1.jpg";
//		String base64Data = Base64Util.getImageStr(imagePath);
//		int[] info = faceService.getFaceRectBase64(base64Data, FaceConstant.cpuType);
//		
//		System.out.println("face rect");
//		System.out.println(JSON.toJSONString(info));
//	}
	
	@Test
	public void getSTest() {
		String imagePath1 = "E:\\nmp_java\\nmp\\nmp-face\\src\\test\\java\\1.jpg";
		String base64Data1 = Base64Util.getImageStr(imagePath1);
		HDFaceInfoVo feat1 = faceService.getFaceFeatureBase64(base64Data1, FaceConstant.cpuType);
		
		
		String imagePath2 = "E:\\nmp_java\\nmp\\nmp-face\\src\\test\\java\\2.jpg";
		String base64Data2 = Base64Util.getImageStr(imagePath2);
		HDFaceInfoVo feat2 = faceService.getFaceFeatureBase64(base64Data2, FaceConstant.cpuType);
	
		
		double similarity = faceService.getSimilarity(feat1.feature, feat2.feature);
		System.out.println("face Similarity:"+similarity);
	}
	

}
