package com.cloudside.services.test;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cloudside.mybatis.model.UserInfo;
import com.cloudside.services.dao.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "测试接口", description = "基于SpringCloud的Restful测试接口" )  
@RestController
public class TestController {

	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;
    
    private String url = "http://localhost:2232";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired 
    private AmqpTemplate rabbitTemplate;
    
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value="两个值求和", notes="根据参数a、b求两值的和",produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name = "a", value = "参数A", required = true, dataType = "Integer"),
		@ApiImplicitParam(paramType="query",name = "b", value = "参数B", required = true, dataType = "Integer") })
    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    @ResponseBody
    public Integer add(@RequestParam Integer a, @RequestParam Integer b) {
        ServiceInstance instance = client.getLocalServiceInstance();
        Integer r = a + b;
        String tmpLog = "/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r;
        this.rabbitTemplate.convertAndSend("hello",tmpLog);
        logger.info(tmpLog);
        r = this.restTemplate.getForObject(url+"/add?a="+a+"&b="+b, Integer.class);
        return r;
    }
    
    @ApiOperation(value="获取名称", notes="根据参数,查询名称",produces = "application/json")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String",paramType="query")
	})
    @ResponseBody
    @RequestMapping(value = "/getName" ,method = RequestMethod.GET)
    public String getName(@RequestParam String name) {
        return "get Name :"+name;
    }
    
    @ApiOperation(value="获取用户", notes="根据名称,模糊查询相关用户",produces = "application/json")
	@ApiImplicitParam(paramType="path",name = "name", value = "名称", required = false, dataType = "String")
    @ResponseBody
    @RequestMapping(value = "/getAllUser/{name}" ,method = RequestMethod.GET)
    public List<UserInfo> getAllUser(@PathVariable String name) {
        return userInfoService.getAllUser();
    }
}
