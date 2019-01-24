package com.cloudside.services.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    public Integer add(@RequestParam Integer a, @RequestParam Integer b) {
        ServiceInstance instance = client.getLocalServiceInstance();
        Integer r = a + b;
        logger.info("/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r);
        return r;
    }
    
    @RequestMapping(value = "/getAllSI" ,method = RequestMethod.GET)
    public List<Map> getAllServicesInstalce() {
    	List<ServiceInstance> list = client.getInstances("STORES");
    	List<Map> listServices = null;
    	if(list != null && list.size() > 0){
    		listServices = new ArrayList();
    		for(ServiceInstance instance : list){
    			Map _tmp = new HashMap();
    			_tmp.put("host", instance.getHost());
    			_tmp.put("port", instance.getPort());
    			_tmp.put("serviceId", instance.getServiceId());
    			_tmp.put("secure", instance.isSecure());
    			_tmp.put("uri", instance.getUri().getPath());
    			listServices.add(_tmp);
    		}
    		return listServices;
    	}
        return null;
    }
}
