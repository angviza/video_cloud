package com.hdvon.nmp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
public class WebAppConfig {

  //上传根目录
  @Value("${upload.upload_root}")
   private String uploadRoot;

   //文件后缀
   @Value("${upload.SUFFIXLIST}")
   private String suffixList;

   
   //头像识别的最低相似度
   @Value("${face.feature_similarity}")
   private Float faceSimilarity;
   
   //服务器的启动端口
   @Value("${server.port}")
   private Integer port;
   
   @Value("${isDEV}")
   private String isDev;
   
   @Value("${default.deviceCode}")
   private String deviceCode;
   
}
