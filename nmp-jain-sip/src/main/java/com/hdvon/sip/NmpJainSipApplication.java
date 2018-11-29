package com.hdvon.sip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NmpJainSipApplication {

	public static void main(String[] args) {
		SpringApplication.run(NmpJainSipApplication.class, args);
	}
}
