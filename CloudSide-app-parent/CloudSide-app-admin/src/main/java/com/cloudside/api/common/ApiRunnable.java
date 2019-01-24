/**
 * @author Poci.
 * @date 2018/1/1
 */
package com.cloudside.api.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;

public class ApiRunnable {

	String apiName;
	
	Method targetMethod;
	
	String beanName;
	
	Object target;
	
	ApiMapping apiMapping;
	
	ApplicationContext applicationContext;
	
	public Object run(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(target == null){
			target = applicationContext.getBean(beanName);
		}
		return targetMethod.invoke(target, args);
	}

	public Method getTargetMethod(){
		return targetMethod;
	}
	
}
