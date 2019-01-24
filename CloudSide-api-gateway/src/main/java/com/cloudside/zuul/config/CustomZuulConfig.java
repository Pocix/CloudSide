package com.cloudside.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudside.mybatis.dao.GatewayApiMapper;
import com.cloudside.zuul.Route.CustomRouteLocator;

/**
 * Created by xujingfeng on 2017/4/1.
 */
@Configuration
public class CustomZuulConfig {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Autowired
    GatewayApiMapper gatewayApiMapper;

    @Bean
    public CustomRouteLocator custRouteLocator() {
        CustomRouteLocator custRouteLocator = new CustomRouteLocator(this.server.getServletPrefix(), this.zuulProperties);
        custRouteLocator.setGatewayApiMapper(gatewayApiMapper);
        return custRouteLocator;
    }

}
