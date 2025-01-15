package com.lumenore2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Project2Application {

	public static void main(String[] args) {

		SpringApplication.run(Project2Application.class, args);
	}

}
