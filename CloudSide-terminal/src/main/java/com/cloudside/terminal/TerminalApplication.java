package com.cloudside.terminal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@EnableDiscoveryClient
@SpringBootApplication
@PropertySource("classpath:db.properties")
public class TerminalApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(TerminalApplication.class, args);
	}

}
