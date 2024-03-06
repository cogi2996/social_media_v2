package com.example.social_media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SocialMediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApplication.class, args);
	}

//	@PersistenceContext
//	private EntityManager entityManager;
//
//	@Bean
//	@Transactional
//	public CommandLineRunner commandLineRunner() {
//		return args -> {
//			try {
//				System.out.println("ket noi thanh cong");
//				// Do your database operations here
//			} catch (Exception e) {
//				System.err.println("Ket noi that bai" + e.getMessage());
//			}
//		};
//	}
//	@Bean
//	CommandLineRunner commandLineRunner(
//			AuthenticationService service
//	) {
//		return args -> {
//			var admin = new RegisterRequest().builder()
//					.firstName("Admin")
//					.lastName("Admin")
//					.email("admin@gmail.com")
//					.password("password")
//					.role(Role.ADMIN)
//					.build();
//			var userAdmin1 = new User().builder()
//					.firstName("Tuan")
//					.lastName("Dang")
//					.createDate(new Date(System.currentTimeMillis())).build();
//
//			var manager = new RegisterRequest().builder()
//					.firstName("Admin")
//					.lastName("Admin")
//					.email("manager@gmail.com")
//					.password("password")
//					.role(Role.MANAGER)
//					.build();
//			var userManager1 = new User().builder()
//					.firstName("Thao")
//					.lastName("Chung")
//					.createDate(new Date(System.currentTimeMillis())).build();;
//			System.out.println("Admin token : "+ service.register(admin,userAdmin1).getAccessToken());
//			System.out.println("Manager token : "+ service.register(manager,userManager1).getAccessToken());
//
//		};
//	}
}
