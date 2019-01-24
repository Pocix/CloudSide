/**
 * @author Poci.
 * @date 2018/1/1
 */
package com.cloudside.api.common;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApiStore {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private HashMap<String, ApiRunnable> apiMap = new HashMap<String, ApiRunnable>();

	@PostConstruct
	public boolean initApiStore(){
		if(applicationContext != null){
			if(apiMap.size() > 0){
				return true;
			}
			this.loadApiFromSpringBeans();
			return true;
		}
		return false;
	}
	
	public void loadApiFromSpringBeans(){
		String[] names = applicationContext.getBeanDefinitionNames();
		Class<?> type;
		for(String name : names){
			type = applicationContext.getType(name);
			for(Method m : type.getDeclaredMethods()){
				ApiMapping apiMapping = m.getAnnotation(ApiMapping.class);
				if(apiMapping != null) {
					addApiItem(apiMapping, name, m);
				}
			}
		}
	}

	private void addApiItem(ApiMapping apiMapping, String beanName, Method method) {
		// TODO Auto-generated method stub
		ApiRunnable apiRun = new ApiRunnable();
		apiRun.apiName = apiMapping.value();
		apiRun.apiMapping = apiMapping;
		apiRun.targetMethod = method;
		apiRun.beanName = beanName;
		apiRun.applicationContext = applicationContext;
		apiMap.put(apiMapping.value(), apiRun);
		
	}
	
	private ApiRunnable findApiRunnable(String apiName, String version){
		return apiMap.get(apiName+"_"+version);
	}

	public HashMap<String, ApiRunnable> getApiMap() {
		return apiMap;
	}

	public void setApiMap(HashMap<String, ApiRunnable> apiMap) {
		this.apiMap = apiMap;
	}
	
}