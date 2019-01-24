/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.cloudside.restApiUi.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class SwaggerConfiguration extends WebMvcConfigurerAdapter implements EnvironmentAware {

    protected RelaxedPropertyResolver propertyResolver;

    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, (String)null);
    }
}