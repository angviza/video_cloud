package com.hdvon.nmp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.hdvon.nmp"})
public class NmpWebApp {

	public static void main(String[] args) {
		SpringApplication.run(NmpWebApp.class, args);
		log.debug("running web-app");
	} 

}
