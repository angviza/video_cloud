package com.hdvon.client.es;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hdvon.client.config.ClientConfig;
/**
 * es配置启动类
 * @author wanshaojian
 *
 */
@Configuration 
public class ElasticsearchConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfig.class);  
    
    @Autowired
    ClientConfig clientConfig;
    
    @Bean  
    public TransportClient init() {  
        LOGGER.info("初始化开始。。。。。");  
        TransportClient transportClient = null;  
  
        try {  
            /**
             *  配置信息 
             *  client.transport.sniff   增加嗅探机制，找到ES集群 
             *  thread_pool.search.size  增加线程池个数，暂时设为5  
             */
            Settings esSetting = Settings.builder()  
                    .put("client.transport.sniff", true) 
                    .put("thread_pool.search.size", clientConfig.getEsPoolSize())
                    .build();  
            //配置信息Settings自定义
            transportClient = new PreBuiltTransportClient(esSetting);  
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(clientConfig.getEsHostName()), clientConfig.getEsPort());  
            transportClient.addTransportAddresses(transportAddress);  
  
  
        } catch (Exception e) {  
            LOGGER.error("elasticsearch TransportClient create error!!!", e);  
        }  
  
        return transportClient;  
    }  
    
    
}
