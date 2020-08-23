package com.banzhe.hokage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class HokageApplication {

	public static void main(String[] args) {
		SpringApplication.run(HokageApplication.class, args);
	}
}
