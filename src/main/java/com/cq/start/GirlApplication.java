package com.cq.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@Component
@SpringBootApplication
public class GirlApplication {

	public static void main(String[] args) {
		SpringApplication.run(GirlApplication.class, args);
	}
}
