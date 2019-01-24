/**
 * @author Administrator
 *
 */
package com.cloudside.api.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cloudside.api.common.ApiMapping;
import com.cloudside.api.domain.Person;

/**
 * 测试服务类
 * @author Poci.
 *
 */
@Service
public class TestSer{
	
	@ApiMapping(value = "com.tz.api.testSer.getName",checkLogin = false)
	public String getName(String id){
		return "Sam";
	}
	
	@ApiMapping(value = "com.tz.api.testSer.getMap",checkLogin = false)
	public String getMap(String id,int sss,HttpServletRequest request){
		return id+":"+sss+":Request:"+request.getSession().toString();
	}
	
	@ApiMapping(value = "com.tz.api.testSer.getPerson",checkLogin = false)
	public String getPerson(String id,Person person){
		return id+":"+person.toString();
	}
}