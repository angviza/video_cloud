package com.hdvon.face.service;

import java.io.IOException;
import java.io.InputStream;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.FaceLogin.HDFaceInfo;
import com.hdvon.FaceLogin.HDFaceSDK_JNI_CPU;
import com.hdvon.face.exception.FaceException;
import com.hdvon.face.utils.Base64Util;
import com.hdvon.face.vo.HDFaceInfoVo;

@Service(interfaceClass=com.hdvon.face.service.FaceService.class)
public class FaceServiceImpl implements FaceService {
	static Logger logger = LoggerFactory.getLogger(FaceServiceImpl.class);
	
	
    /**
     * 获取人脸位置
     * @param base64Data  图片base64
     * @param type  方式 1：cpu  2：Gpu
     */
	@Override
	public int[] getFaceRectPath(String base64Data, int type) throws FaceException {

		// TODO Auto-generated method stub
        byte[] bytes = Base64Util.trainByteByBase64(base64Data);
		Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_IMWRITE_JPEG_OPTIMIZE);
		return getFaceRect(mat, type);
	}
    /**
     * 获取人脸位置
     * @param imgPath  图片全路径
     * @param type  方式 1：cpu  2：Gpu
     */
	@Override
	public int[] getFaceRectBase64(String imgPath, int type) throws FaceException {
		// TODO Auto-generated method stub
		Mat mat = Imgcodecs.imread(imgPath);
        
		return getFaceRect(mat, type);
	}
	
	
    /**
     * 获取人脸位置
     * @param base64Data  图片base64
     * @param type  方式 1：cpu  2：Gpu
     */
	private int[] getFaceRect(Mat mat, int type) throws FaceException {
		// TODO Auto-generated method stub
		/**
		 * 初始化人脸算法
		 */
		HDFaceSDK_JNI_CPU jni = new HDFaceSDK_JNI_CPU();
		long lHandle = initSdk(jni);

		
		byte[] bBGRData = new byte[mat.cols() * mat.rows() * mat.channels()];
		mat.get(0, 0, bBGRData);
        int pos1[]=jni.GetMaxFaceRect(lHandle,bBGRData,mat.cols(),mat.rows());
        if(logger.isDebugEnabled()) {
        	logger.info("GetMaxFaceRect: face pos1: x = %d  y = %d  width = %d height = %d\n", pos1[0],pos1[1],pos1[2],pos1[3]);
        }
        /**
         * 关闭mat
         */
        mat.release();
		/**
		 * 反初始化算法
		 */
		jni.UninitSDK(lHandle);
		return pos1;
	}
    /**
     * 获取图片特征值
     * @param base64Data  图片base64
     * @param type  方式 1：cpu  2：Gpu
     */
	@Override
	public HDFaceInfoVo getFaceFeatureBase64(String base64Data, int type) throws FaceException {
		// TODO Auto-generated method stub
        byte[] bytes = Base64Util.trainByteByBase64(base64Data);
		Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_IMWRITE_JPEG_OPTIMIZE);
		return getFaceFeature(mat, type);
	}

	
    /**
     * 获取图片特征值
     * @param imgPath 图片全路径
     * @param type  方式 1：cpu  2：Gpu
     */
	@Override
	public HDFaceInfoVo getFaceFeaturePath(String imgPath, int type) throws FaceException {
		// TODO Auto-generated method stub
		Mat mat = Imgcodecs.imread(imgPath);
		return getFaceFeature(mat, type);
	}
	private HDFaceInfoVo getFaceFeature(Mat mat, int type) throws FaceException {
		// TODO Auto-generated method stub
		/**
		 * 初始化人脸算法
		 */
		HDFaceSDK_JNI_CPU jni = new HDFaceSDK_JNI_CPU();
		long lHandle = initSdk(jni);
		
		byte[] bBGRData = new byte[mat.cols() * mat.rows() * mat.channels()];
		mat.get(0, 0, bBGRData);
		/**
		 * 功能：获取人脸特征
		 * 参数bBGRData：图像RGB数据
		 * 参数imgPath： 图像宽度
		 * 参数imgPath： 图像高度
		 * 返回：人脸特征数组（512维度）
		 */
		HDFaceInfo face = jni.GetMaxFaceInfo(lHandle, bBGRData, mat.cols(), mat.rows());
		logger.info("GetMaxFaceFeature: feature1 length = %d \n", face.feature.length);
		
        /**
         * 关闭mat
         */
		mat.release();
		/**
		 * 反初始化算法
		 */
		jni.UninitSDK(lHandle);
		

		return convestObject(face);
	}
	@Override
	public float getSimilarity(float [] fea1,float [] fea2) throws FaceException {
		// TODO Auto-generated method stub
		HDFaceSDK_JNI_CPU jni = new HDFaceSDK_JNI_CPU();
		float similarity = jni.getSimilarity(fea1,fea2);
		logger.info("the two faces similarity :  %.4f \n", similarity);
		return similarity;
	}
	/**
	 * 初始化人脸算法
	 * 参数lHandle：算法初始化返回的句柄
	 */
	private long initSdk(HDFaceSDK_JNI_CPU jni) {

		String configFilePath = "";
		try {
			ClassPathResource resource = new ClassPathResource("model/hdface_config_model.json");
			configFilePath = resource.getFile().getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long lHandle = jni.InitSDK(configFilePath);
		if (lHandle == 0) {
			logger.error("face Initialization algorithm fail,face lhandle is 0");
			throw new FaceException("face Initial FaceSDK_CPU fail,face lhandle is 0");
		}
        logger.info("Initial FaceSDK_CPU SUCCEED!\n");
        return lHandle;
	}

	private HDFaceInfoVo convestObject(HDFaceInfo face) {
		HDFaceInfoVo faceVo = new HDFaceInfoVo();
		faceVo.setFeature(face.feature);
		faceVo.setHeight(face.height);
		faceVo.setWidth(face.width);
		faceVo.setLeft_x(face.left_x);
		faceVo.setLeft_y(face.left_y);
		
		return faceVo;
	}


}
