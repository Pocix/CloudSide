package com.cloudside.redis.common;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource("classpath:rabbitmq.properties")
public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

}
