package com.cloudside.zuul;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.PropertySource;

import com.cloudside.jwt.util.Audience;

@ServletComponentScan
@MapperScan(basePackages = "com.cloudside.mybatis.dao")
@PropertySource("classpath:db.properties")
@EnableConfigurationProperties(Audience.class)
@EnableZuulProxy
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}

}