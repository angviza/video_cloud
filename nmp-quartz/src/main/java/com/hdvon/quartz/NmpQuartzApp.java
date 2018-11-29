package com.hdvon.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.hdvon.quartz.mapper")
@ComponentScan(basePackages = {"com.hdvon.quartz"})
public class NmpQuartzApp {
	public static void main(String[] args) {
		SpringApplication.run(NmpQuartzApp.class, args);
	}
}
