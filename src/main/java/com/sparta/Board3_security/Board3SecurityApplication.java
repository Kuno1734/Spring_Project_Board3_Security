package com.sparta.Board3_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Board3SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(Board3SecurityApplication.class, args);
	}

}
