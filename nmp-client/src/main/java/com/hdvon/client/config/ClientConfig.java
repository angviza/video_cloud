package com.hdvon.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 读取client配置信息
 * @author wanshaojian
 *
 */
@Configuration
@Getter
@Setter
public class ClientConfig {
	
    /** 
     * elk集群地址 
     */  
    @Value("${elasticsearch.ip}")  
    private String esHostName;  
    /** 
     * 端口 
     */  
    @Value("${elasticsearch.port}")  
    private Integer esPort;  
    /** 
     * 集群名称 
     */  
    @Value("${elasticsearch.cluster.name}")  
    private String esClusterName;  
  
    /** 
     * 连接池 
     */  
    @Value("${elasticsearch.pool}")  
    private Integer esPoolSize;  
  
    
    /** 
     * 是否开启重新创建索引 
     */  
    @Value("${elasticsearch.regenerateIndexEnabled}")  
    private Boolean esRegenerateIndexFlag; 
    
    
    /** 
     * 是否启用索引 数据同步 
     */  
    @Value("${elasticsearch.syncDataEnabled}")  
    private Boolean esSyncDataEnabled; 
    
    /**
     * rabbitmq地址
     *//*
    @Value("${spring.rabbitmq.host}")  
    private String mqHost;
    
    *//**
     * rabbitmq地址
     *//*
    @Value("${spring.rabbitmq.port}")  
    private Integer mqPort;
    
    *//**
     * rabbitmq账号
     *//*
    @Value("${spring.rabbitmq.username}")  
    private String mqUsername;
    
    *//**
     * rabbitmq密码
     *//*
    @Value("${spring.rabbitmq.password}")  
    private String mqPassword;
    
    *//**
     * rabbitmq是否要进行消息回调，则这里必须要设置为true
     *//*
    @Value("${spring.rabbitmq.publisherconfirms}")  
    private Boolean mqPublisherconfirms;*/
    
}
