package com.pratice.springpratice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pratice"})
@MapperScan(basePackages = {"com.pratice.mapper"})
public class SpringpraticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringpraticeApplication.class, args);
	}

}
