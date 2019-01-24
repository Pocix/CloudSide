package com.cloudside.feigh.consumer.hystrix;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudside.feigh.consumer.intf.ConsumerClient;

@Component
public class ConsumerClientHystrix implements ConsumerClient {

	public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
		// TODO Auto-generated method stub
		return -9999;
	}

}
