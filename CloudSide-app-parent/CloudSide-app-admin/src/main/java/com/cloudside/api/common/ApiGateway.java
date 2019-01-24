/**
 * @author Poci.
 * @date 2018/1/1
 */
package com.cloudside.api.common;

import com.alibaba.fastjson.JSON;
import com.cloudside.common.utils.JSONUtils;
import com.cloudside.api.log.ApiLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@RestController
public class ApiGateway {
	private static Logger logger = LoggerFactory.getLogger(ApiGateway.class);

	@Autowired
	ApiStore apiStore;
	
	final ParameterNameDiscoverer parameterUtil;
	
	public ApiGateway(){
		parameterUtil = new LocalVariableTableParameterNameDiscoverer();
	}
	
	/**
	 * JSON服务接口
	 * @param request
	 * @param response
	 * @param path
	 * @param params
	 * @return
	 * @throws ApiException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@ApiLog("Api:Service Invoke")
	@RequestMapping(value="api/{path:.+}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Object apiGateway(HttpServletRequest request, HttpServletResponse response,@PathVariable String path,@RequestBody Object params) throws ApiException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ApiRunnable apiRun = apiStore.getApiMap().get(path);
		if(apiRun == null){
			logger.error("接口: {} 不存在",path);
			throw new ApiException(ErrorCode.METHOD_NOTFOUND);
		}else{
			if(apiRun.apiMapping.checkLogin()){
				//权限验证
				Map<String, Object> map = null;
				map = JSONUtils.jsonToMap(JSONUtils.beanToJson(params));
				map = JSONUtils.jsonToMap(map.get("param").toString());
				Object sessionKeyObj=map.get("sessionKeyId");
				String token="";
				if(null!=sessionKeyObj){
					token =sessionKeyObj .toString();
				}else{
					throw new ApiException(ErrorCode.SESSIONKEYID_NOTFOUND);
				}
				//校验
				long userId= 1;//baseService.getUserId(token);
				if (userId==0) {
					logger.error("令牌:[ {} ]过期", token);
					throw new ApiException(ErrorCode.REQUEST_EXPIRE);
				}else{
					//判断敏感字符
					return apiRun.run(buildParams(request,response,apiRun,JSONUtils.beanToJson(params)));
				}
			}else{
				return apiRun.run(buildParams(request,response,apiRun,JSONUtils.beanToJson(params)));
			}
			
		}
	}
	
	/**
	 * 文件上传接口
	 * @param file
	 * @param request
	 * @param response
	 * @param path
	 * @param params
	 * @return
	 * @throws ApiException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@ApiLog("Api:File Upload")
	@RequestMapping(value="api/upload/{path:.+}")
	public Object apiGatewayUpload(HttpServletRequest request, HttpServletResponse response,@PathVariable String path) throws ApiException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
		List<MultipartFile> files = multipartRequest.getFiles("files");
		String params="{\"param\":\"\"}";
		ApiRunnable apiRun = apiStore.getApiMap().get(path);
		if(apiRun == null){
			logger.error("接口: {} 不存在",path);
			throw new ApiException(ErrorCode.METHOD_NOTFOUND);
		}else{
			if(false/*apiRun.apiMapping.checkLogin()*/){
				//权限验证
				String token = JSONUtils.jsonToMap(params).get("token").toString();
				//校验
				logger.error("令牌:[ {} ]过期",token);
				throw new ApiException(ErrorCode.REQUEST_EXPIRE);
			}else{
				return apiRun.run(buildParams(files,request,response,apiRun,params));
			}
			
		}
	}
	
	/**
	 * JSON服务解析
	 * @param request
	 * @param response
	 * @param run
	 * @param params
	 * @return
	 * @throws ApiException
	 */
	private Object[] buildParams(HttpServletRequest request, HttpServletResponse response, ApiRunnable run,String params) throws ApiException{
		Map<String, Object> map = null;
		try{
			map = JSONUtils.jsonToMap(params);
			map = JSONUtils.jsonToMap(map.get("param").toString());
		}catch(IllegalArgumentException e){
			logger.error("参数:[ {} ] 无效",params);
			throw new ApiException(ErrorCode.PARAM_INVALID);
		}
		if(map == null){
			map = new HashMap<>();
		}
		
		Method method = run.getTargetMethod();
		
		List<String> paramNames = Arrays.asList(parameterUtil.getParameterNames(method));
		
		Class<?>[] paramTyles = method.getParameterTypes();
		
		for(Map.Entry<String, Object> m : map.entrySet()){
			if(!paramNames.contains(m.getKey())){
				logger.error("调用失败: 接口不存在 {} 参数",m.getKey());
				throw new ApiException(ErrorCode.PARAM_NOEXIST);
			}
		}
		
		Object[] args = new Object[paramTyles.length];
		for(int i = 0; i < paramTyles.length; i++){
			if(map.containsKey(paramNames.get(i))){
				args[i] = convertJsonToBean(map.get(paramNames.get(i)),paramTyles[i]);
			}else if("request".equals(paramNames.get(i))){
				args[i] = request;
			}else if("response".equals(paramNames.get(i))){
				args[i] = response;
			}
		}
		return args;
	}

	/**
	 * 针对文件上传的接口参数解析
	 * @param file
	 * @param request
	 * @param response
	 * @param run
	 * @param params
	 * @return
	 * @throws ApiException
	 */
	private Object[] buildParams(List<MultipartFile> files, HttpServletRequest request, HttpServletResponse response, ApiRunnable run,String params) throws ApiException{
		Map<String, Object> map = null;
		try{
			map = JSONUtils.jsonToMap(params);
			map = JSONUtils.jsonToMap(map.get("param").toString());
		}catch(IllegalArgumentException e){
			logger.error("参数:[ {} ] 无效",params);
			throw new ApiException(ErrorCode.PARAM_INVALID);
		}
		if(map == null){
			map = new HashMap<>();
		}
		
		Method method = run.getTargetMethod();
		
		List<String> paramNames = Arrays.asList(parameterUtil.getParameterNames(method));
		
		Class<?>[] paramTyles = method.getParameterTypes();
		
		for(Map.Entry<String, Object> m : map.entrySet()){
			if(!paramNames.contains(m.getKey())){
				logger.error("调用失败: 接口不存在 {} 参数",m.getKey());
				throw new ApiException(ErrorCode.PARAM_NOEXIST);
			}
		}
		
		Object[] args = new Object[paramTyles.length];
		for(int i = 0; i < paramTyles.length; i++){
			if(map.containsKey(paramNames.get(i))){
				args[i] = convertJsonToBean(map.get(paramNames.get(i)),paramTyles[i]);
			}else if("request".equals(paramNames.get(i))){
				args[i] = request;
			}else if("response".equals(paramNames.get(i))){
				args[i] = response;
			}else if("files".equals(paramNames.get(i))){
				args[i] = files;
			}
		}
		return args;
	}
	
	private Object convertJsonToBean(Object object, Class<?> clazz) {
		if(clazz.getName().contains("java.lang")){
			return object;
		}else if("string,int,float,boolean,long,double,byte,char".contains(clazz.getName().toLowerCase())){
			return object;
		}
		return JSON.parseObject(JSONUtils.beanToJson(object), clazz);
	}
}
