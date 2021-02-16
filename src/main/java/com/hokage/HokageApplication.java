package com.hokage;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author linyimin
 * @date 2020/4/21 1:11 am
 * @email linyimin520812@gmail.com
 * @description
 */

@OpenAPIDefinition(
	info = @Info(
		title = "Hokage API docs",
		version = "1.0",
		description = "Hokage is a server management system based-on spring boot, jsch and react"
	),
	externalDocs = @ExternalDocumentation(
		description = "project address",
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
