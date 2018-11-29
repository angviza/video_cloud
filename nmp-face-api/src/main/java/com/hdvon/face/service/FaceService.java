package com.hdvon.face.service;

import com.hdvon.face.exception.FaceException;
import com.hdvon.face.vo.HDFaceInfoVo;
/**
 * 人脸识别服务接口
 * @author wanshaojian
 *
 */
public interface FaceService {

    /**
     * 获取人脸位置
     * @param base64Data  图片base64
     * @param type  方式 1：cpu  2：Gpu
     */
    int[] getFaceRectBase64(String base64Data,int type) throws FaceException;
    
    /**
     * 获取人脸位置
     * @param imgPath 图片全路径
     * @param type  方式 1：cpu  2：Gpu
     */
    int[] getFaceRectPath(String imgPath,int type) throws FaceException;
    
    /**
     * 获取图片特征值
     * @param base64Data  图片base64
     * @param type  方式 1：cpu  2：Gpu
     */
    HDFaceInfoVo getFaceFeatureBase64(String base64Data,int type) throws FaceException;

    

    /**
     * 获取图片特征值
     * @param imgPath 图片全路径
     * @param type  方式 1：cpu  2：Gpu
     */
    HDFaceInfoVo getFaceFeaturePath(String imgPath,int type) throws FaceException;

    /**
     * 图片相似度
     * @param fea1 图片特征1
     * @param fea2 图片特征2
     * @param type 方式 1：cpu  2：Gpu
     * @return
     */
    float getSimilarity(float [] fea1,float [] fea2) throws FaceException;

 
}
