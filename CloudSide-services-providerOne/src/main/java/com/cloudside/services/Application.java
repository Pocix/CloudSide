package com.cloudside.services;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;


@MapperScan(basePackages = "com.cloudside.mybatis.dao")
@PropertySource("classpath:db.properties")
@PropertySource("classpath:rabbitmq.properties")
@EnableDiscoveryClient
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}
	
	@Bean  
    public RestTemplate restTemplate() {  
        return new RestTemplate();  
    }

}
