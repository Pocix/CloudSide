package com.cloudside.feigh.consumer.intf;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudside.feigh.consumer.hystrix.ConsumerClientHystrix;

@FeignClient(value ="Test2-service", fallback = ConsumerClientHystrix.class)
public interface ConsumerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/add")
    Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);

}