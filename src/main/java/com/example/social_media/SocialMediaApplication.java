package com.example.social_media;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;


@SpringBootApplication
public class SocialMediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApplication.class, args);
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return args -> {
			try {
				System.out.println("ket noi thanh cong");
				// Do your database operations here
			} catch (Exception e) {
				System.err.println("Ket noi that bai" + e.getMessage());
			}
		};
	}
}
