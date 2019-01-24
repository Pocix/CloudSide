/**
 * @author Administrator
 *
 */
package com.cloudside.services;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudside.restApiUi.config.SwaggerConfiguration;
import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends SwaggerConfiguration{

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
                .title("服务二")
                .description("服务二 的 API 文档")
                .contact("Poci.")
                .version("1.0")
                .build();
    }
}