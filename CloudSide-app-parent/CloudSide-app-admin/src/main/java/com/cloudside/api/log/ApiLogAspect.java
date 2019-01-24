package com.cloudside.api.log;

import com.cloudside.common.dao.LogDao;
import com.cloudside.common.domain.LogDO;
import com.cloudside.common.utils.*;
import com.cloudside.system.domain.UserDO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class ApiLogAspect {
	@Autowired
	LogDao logMapper;

	@Pointcut("@annotation(com.cloudside.api.log.ApiLog)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 保存日志
		saveLog(point, time, result);
		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time,final Object result) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		LogDO sysLogDO = new LogDO();
		ApiLog syslog = method.getAnnotation(ApiLog.class);
		if (syslog != null) {
			// 注解上的描述
			sysLogDO.setOperation(syslog.value());
		}
		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLogDO.setMethod(className + "." + methodName + "()");
		// 请求的参数
		Object[] args = joinPoint.getArgs();
		try {
			sysLogDO.setOperation(sysLogDO.getOperation()+"[Mapping:"+args[2]+"] Result:[HashCode_"+result.hashCode()+"]/Success");
			String params = JSONUtils.beanToJson(args[3]);
			sysLogDO.setParams(params);
		} catch (Exception e) {

		}
		// 获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		// 设置IP地址
		sysLogDO.setIp(IPUtils.getIpAddr(request));
		// 用户名
		UserDO currUser = ShiroUtils.getUser();
		if (null == currUser) {
			if (null != sysLogDO.getParams()) {
				try {
					Map<String, Object> map = JSONUtils.jsonToMap(sysLogDO.getParams());
					map = JSONUtils.jsonToMap(map.get("param").toString());
					Object userIdObj = map.get("sessionKeyId");
					String sessionKeyId = null;
					if (null != userIdObj) {
						sessionKeyId = (String) userIdObj;
					}else{
						sysLogDO.setUserId(-1L);
						sysLogDO.setUsername("Token信息错误");
					}
				}catch (Exception e){
					sysLogDO.setUserId(-1L);
					sysLogDO.setUsername("Token信息错误");
				}
			} else {
				sysLogDO.setUserId(-1L);
				sysLogDO.setUsername("获取用户信息为空");
			}
		} else {
			sysLogDO.setUserId(ShiroUtils.getUserId());
			sysLogDO.setUsername(ShiroUtils.getUser().getUsername());
		}
		sysLogDO.setTime((int) time);
		// 系统当前时间
		Date date = new Date();
		sysLogDO.setGmtCreate(date);
		// 保存系统日志
		logMapper.save(sysLogDO);
	}
}
