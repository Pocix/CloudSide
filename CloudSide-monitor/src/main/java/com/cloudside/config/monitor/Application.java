package com.cloudside.config.monitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@MapperScan(basePackages = "com.cloudside.mybatis.dao")
@PropertySource("classpath:db.properties")
@EnableEurekaClient
@SpringBootApplication
@ServletComponentScan(basePackages = {"com.cloudside.ds.config"}) //扫描ds模块配置的servlet
public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}

}
