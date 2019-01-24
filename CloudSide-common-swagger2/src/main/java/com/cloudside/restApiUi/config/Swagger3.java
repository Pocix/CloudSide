/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.cloudside.restApiUi.config;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class Swagger3 {

    @Bean
	public Docket createRestApi() {
		Predicate<RequestHandler> predicate = new Predicate<RequestHandler>() {
			@Override
			public boolean apply(RequestHandler input) {
				Class<?> declaringClass = input.declaringClass();
				if (declaringClass == BasicErrorController.class)// 排除
					return false;
				if (declaringClass.isAnnotationPresent(RestController.class)) // 被注解的类
					return true;
				if (input.isAnnotatedWith(ResponseBody.class)) // 被注解的方法
					return true;
				return false;
			}
		};
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(false)
				.select()
				.paths(PathSelectors.any())
				.apis(predicate).build();
	}
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .description("基于Spring Cloud架构由Spring Boot提供Restful的微服务,并且生成相应的APIs")
                .contact("Poci.")
                .version("1.0")
                .build();
    }

}