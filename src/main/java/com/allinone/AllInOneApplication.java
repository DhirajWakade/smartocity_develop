package com.allinone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableWebMvc
public class AllInOneApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(AllInOneApplication.class, args);
		System.out.println("*****Application Started Successfully******");
	}
	
}
