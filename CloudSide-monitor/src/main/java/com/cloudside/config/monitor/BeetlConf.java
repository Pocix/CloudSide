package com.cloudside.config.monitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.beetl.core.Format;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.beetl.ext.spring.UtilsFunctionPackage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cloudside.beetlPlugin.LocalDateFormat;
import com.cloudside.beetlPlugin.LocalDateTimeFormat;

@ComponentScan(basePackages = { "com.cloudside.config.monitor.web" })
@Configuration
public class BeetlConf {
	@Value("${beetl.templatesPath}")
	private String templatesPath;

	@Bean(initMethod = "init", name = "beetlConfig")
	public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
		BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
		try {
			ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader(this.getClass().getClassLoader(), templatesPath);
			beetlGroupUtilConfiguration.setResourceLoader(resourceLoader);
			beetlGroupUtilConfiguration.setTypeFormats(genTypeFormats());
			beetlGroupUtilConfiguration.setFunctionPackages(genFunctionPackages());
			return beetlGroupUtilConfiguration;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean(name = "beetlViewResolver")
	public BeetlSpringViewResolver getBeetlSpringViewResolver(
			@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
		beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
		beetlSpringViewResolver.setOrder(0);
		beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
		return beetlSpringViewResolver;
	}

	private Map<Class<?>, Format> genTypeFormats() {
		Map<Class<?>, Format> typeFormats = new HashMap<>();
		typeFormats.put(LocalDateTime.class, new LocalDateTimeFormat());
		typeFormats.put(LocalDate.class, new LocalDateFormat());
		return typeFormats;
	}

	public Map<String, Object> genFunctionPackages() {
		Map<String, Object> functionPackages = new HashMap<>();
		functionPackages.put("sputil", new UtilsFunctionPackage());
		return functionPackages;
	}
}