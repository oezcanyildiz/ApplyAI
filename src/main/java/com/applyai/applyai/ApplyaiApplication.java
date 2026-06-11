package com.applyai.applyai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ApplyaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplyaiApplication.class, args);
	}

}
