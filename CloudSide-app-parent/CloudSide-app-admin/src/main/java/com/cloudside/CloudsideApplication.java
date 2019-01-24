package com.cloudside;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.cloudside.*.dao")
@SpringBootApplication
@EnableCaching
public class CloudsideApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudsideApplication.class, args);
    }
}
