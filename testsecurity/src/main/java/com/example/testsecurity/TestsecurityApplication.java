package com.example.testsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class TestsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestsecurityApplication.class, args);
	}

}
