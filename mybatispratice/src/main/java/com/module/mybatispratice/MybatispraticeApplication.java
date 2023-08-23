package com.module.mybatispratice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.module.mybatispratice.mapper"})
public class MybatispraticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybatispraticeApplication.class, args);
		/*aa*/
	}

}
