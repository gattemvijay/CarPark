package com.example.carpark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Enable scheduled tasks
public class CarparkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarparkApplication.class, args);
	}

}
