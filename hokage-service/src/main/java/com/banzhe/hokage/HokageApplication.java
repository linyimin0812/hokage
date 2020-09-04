package com.banzhe.hokage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HokageApplication {

	public static void main(String[] args) {
		SpringApplication.run(HokageApplication.class, args);
	}
}
