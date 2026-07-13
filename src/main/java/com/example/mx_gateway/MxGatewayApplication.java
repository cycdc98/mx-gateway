package com.example.mx_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MxGatewayApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MxGatewayApplication.class, args);
	}

}
