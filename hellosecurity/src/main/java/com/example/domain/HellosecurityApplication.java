package com.example.domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.domain.mapper"})
public class HellosecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(HellosecurityApplication.class, args);
	}

}
