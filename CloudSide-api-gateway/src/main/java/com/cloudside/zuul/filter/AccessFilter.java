package com.cloudside.zuul.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudside.jwt.util.Audience;
import com.cloudside.jwt.util.JwtHelper;
import com.cloudside.mybatis.dao.GatewayApiMapper;
import com.cloudside.mybatis.dao.UserInfoMapper;
import com.cloudside.mybatis.model.ZuulRouteVO;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * zuul 权限过滤
 * 
 * @author Administrator
 *
 */
@Component
public class AccessFilter extends ZuulFilter {

	@Autowired
	GatewayApiMapper gatewayApiMapper;
	
	@Autowired  
    private Audience audienceEntity;

	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

	@Override
	public String filterType() {
		// 前置过滤器
		return "pre";
	}

	@Override
	public int filterOrder() {
		// 优先级，数字越大，优先级越低
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		// 是否执行该过滤器，true代表需要过滤
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		log.info("host {} url {} heads {}", request.getRemoteHost(), request.getRequestURL().toString(),
				request.getParameterMap());
		
		List<ZuulRouteVO> tmp = gatewayApiMapper.getListByEnabled();
		/*TODO 两种方案
		 * 1根据user的权限进行路径判断,可以写进redis里面
		 * 2在获取token的时候,把权限路由写进redis,则此地可以直接从redis里面获取
		 * 问题redis的更新问题-分配权限同时，更新redis
		 * */
		// 获取传来的参数accessToken
		Object accessToken = request.getParameter("accessToken");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String auth = httpRequest.getHeader("Authorization");
		if ((auth != null) && (auth.length() > 7)) {
			String HeadStr = auth.substring(0, 6).toLowerCase();
			if (HeadStr.compareTo("bearer") == 0) {
				auth = auth.substring(7, auth.length());
				if (JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret()) != null) {
					log.info("access !!");
					return null;
				}else{
					log.warn("access token is empty");
					// 过滤该请求，不往下级服务去转发请求，到此结束
					ctx.setSendZuulResponse(false);
					ctx.setResponseStatusCode(401);
					ctx.setResponseBody("{\"result\":\"accessToken is invaild!\"}");
					return null;
				}
			} else {
				log.warn("access token is empty");
				// 过滤该请求，不往下级服务去转发请求，到此结束
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody("{\"result\":\"request Authorization type not oauth2!\"}");
			}
		} else if(request.getRequestURL().toString().endsWith("api-docs")){
			log.info("access swagger-ui !!");
			return null;
		} else{
			log.warn("access token is empty");
			// 过滤该请求，不往下级服务去转发请求，到此结束
//			ctx.setSendZuulResponse(false);
//			ctx.setResponseStatusCode(401);
//			ctx.setResponseBody("{\"result\":\"request Header no Authorization parameter!\"}");
		}
		return null;
	}

}
