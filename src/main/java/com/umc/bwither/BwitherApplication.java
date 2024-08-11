package com.umc.bwither;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BwitherApplication {

	public static void main(String[] args) {
		SpringApplication.run(BwitherApplication.class, args);
	}

}
