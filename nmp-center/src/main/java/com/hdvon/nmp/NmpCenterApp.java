package com.hdvon.nmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.hdvon.nmp.mapper")
public class NmpCenterApp {

	public static void main(String[] args) {
		SpringApplication.run(NmpCenterApp.class, args);
	}
}