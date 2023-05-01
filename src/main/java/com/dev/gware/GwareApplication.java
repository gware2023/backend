package com.dev.gware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(GwareApplication.class, args);
	}
}
