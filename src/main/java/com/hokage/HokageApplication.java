package com.hokage;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
	info = @Info(
		title = "Hokage接口文档",
		version = "1.0",
		description = "Hokage是一个基于spring boot,jsch,react开发的服务器管理系统"
	),
	externalDocs = @ExternalDocumentation(
		description = "项目地址",
		url = "https://github.com/linyimin-bupt/hokage"
	)
)
@SpringBootApplication
@EnableScheduling
public class HokageApplication {

	public static void main(String[] args) {
		SpringApplication.run(HokageApplication.class, args);
	}
}
