package com.hdvon.FaceLogin;

import java.io.File;

public class HDFaceSDK_JNI_CPU
{
    public native  long InitSDK(String configFilePath);
    public native  void UninitSDK(long lHandel);

    public native  int [] GetMaxFaceRect(long lHandel, String sImgPath);
    public native  int [] GetMaxFaceRect(long lHandel, byte[] bBGRData, int nWidth, int nHeight);

    public native  float [] GetMaxFaceFeature(long lHandel, String sImgPath);
    public native  float [] GetMaxFaceFeature(long lHandel, byte[] bBGRData, int nWidth, int nHeight);

    public native  HDFaceInfo GetMaxFaceInfo(long lHandel, String sImgPath);
    public native  HDFaceInfo GetMaxFaceInfo(long lHandel, byte[] bBGRData, int nWidth, int nHeight);

    public  float getSimilarity(float [] feature1, float [] feature2)
    {
        assert(feature1 != null && feature2 != null && feature1.length == feature2.length);
        float innerDot = 0.0f;
        for(int i=0; i != feature1.length;++i)
        {
            innerDot += feature1[i] * feature2[i];
        }
        float score = innerDot * 100.0f;
        if(score < 0.0f) score = 0.0f;
        return score > 100.0f ? 100.0f:score;
    }

}
