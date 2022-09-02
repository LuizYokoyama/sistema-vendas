package com.ls.sistemavendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class QuermesseApplication {

	public static void main(String[] args) {
/*
		PasswordEncoder encoder = new BCryptPasswordEncoder(4);
		System.out.println(encoder.encode("password"));
*/
		SpringApplication.run(QuermesseApplication.class, args);
	}

}
