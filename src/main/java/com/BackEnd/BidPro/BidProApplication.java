package com.BackEnd.BidPro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BidProApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidProApplication.class, args);
	}

}
