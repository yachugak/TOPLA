package com.yachugak.topla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ToplaSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToplaSpringApplication.class, args);
	}

}
