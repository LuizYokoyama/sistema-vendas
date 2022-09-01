package com.ls.sistemavendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAsync
@EnableCaching
public class QuermesseApplication {

	public static void main(String[] args) {
/*
		PasswordEncoder encoder = new BCryptPasswordEncoder(4);
		System.out.println(encoder.encode("password"));
*/
		SpringApplication.run(QuermesseApplication.class, args);
	}

}
