package com.cloudside.sleuth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

import zipkin.server.EnableZipkinServer;

@PropertySource("classpath:db.properties")
@EnableZipkinServer  //@EnableZipkinStreamServer// //使用Stream方式启动ZipkinServer
@EnableEurekaClient
@SpringBootApplication
@ServletComponentScan(basePackages = {"com.cloudside.ds.config"}) //扫描ds模块配置的servlet
public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}

}
