package com.hdvon.client.config.code;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@PropertySource("classpath:/code/code.properties")
@Getter
@Setter
public class CodeConfig {
	/**
	 * 部门根节点编码
	 */
	@Value("${department.rootCode}")  
	private String rootCode; 
	
	@Value("${department.splitCodeSuffix}")  
	private String splitCodeSuffix;  
}
