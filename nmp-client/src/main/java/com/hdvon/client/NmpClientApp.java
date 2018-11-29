package com.hdvon.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;
/**
 * 启动类
 * @author wanshaojian
 *
 */
@SpringBootApplication
@MapperScan("com.hdvon.client.mapper")
public class NmpClientApp {

	
	public static void main(String[] args) {
		SpringApplication.run(NmpClientApp.class, args);
		
	}
	

}
