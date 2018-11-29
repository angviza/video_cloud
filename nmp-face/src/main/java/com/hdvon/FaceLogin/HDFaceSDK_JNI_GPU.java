package com.hdvon.FaceLogin;

public class HDFaceSDK_JNI_GPU
{

    public native  long InitSDK(String configFilePath, int nGPUID);
    public native  void UninitSDK(long lHandel);

    public native  int [] GetMaxFaceRect(long lHandel, String sImgPath);
    public native  int [] GetMaxFaceRect(long lHandel, byte[] bBGRData, int nWidth, int nHeight);

    public native  float [] GetMaxFaceFeature(long lHandel, String sImgPath);
    public native  float [] GetMaxFaceFeature(long lHandel, byte[] bBGRData, int nWidth, int nHeight);

    public native  HDFaceInfo GetMaxFaceInfo(long lHandel, String sImgPath);
    public native  HDFaceInfo GetMaxFaceInfo(long lHandel, byte[] bBGRData, int nWidth, int nHeight);

    public  float getSimilarity(float [] fea1, float [] fea2)
    {
        assert(fea1 != null && fea2 != null && fea1.length == fea2.length);
        float innerDot = 0.0f;
        for(int i=0; i !=fea1.length;++i)
        {
            innerDot += fea1[i] * fea2[i];
        }
        float score = innerDot * 100.0f;
        if(score < 0.0f) score = 0.0f;
        return score > 100.0f ? 100.0f:score;
    }
}
