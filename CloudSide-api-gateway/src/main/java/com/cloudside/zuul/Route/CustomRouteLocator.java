package com.cloudside.zuul.Route;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

import com.cloudside.mybatis.dao.GatewayApiMapper;
import com.cloudside.mybatis.model.ZuulRouteVO;

/**
 * Created by xujingfeng on 2017/4/1.
 */
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

	public final static Logger logger = LoggerFactory.getLogger(CustomRouteLocator.class);

	private GatewayApiMapper gatewayApiMapper;

	private ZuulProperties properties;
	
	private List<ZuulRouteVO> enableRoutes;

	public void setGatewayApiMapper(GatewayApiMapper gatewayApiMapper) {
		this.gatewayApiMapper = gatewayApiMapper;
	}

	public CustomRouteLocator(String servletPath, ZuulProperties properties) {
		super(servletPath, properties);
		this.properties = properties;
		logger.info("servletPath:{}", servletPath);
	}

	// 父类已经提供了这个方法，这里写出来只是为了说明这一个方法很重要！！！
	// @Override
	// protected void doRefresh() {
	// super.doRefresh();
	// }

	@Override
	public void refresh() {
		doRefresh();
	}

	@Override
	protected Map<String, ZuulRoute> locateRoutes() {
		LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
		// 从application.properties中加载路由信息
		routesMap.putAll(super.locateRoutes());
		// 从db中加载路由信息
		routesMap.putAll(locateRoutesFromDB());
		// 优化一下配置
		LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
		for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
			String path = entry.getKey();
			// Prepend with slash if not already present.
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (!StringUtils.isBlank(this.properties.getPrefix())) {
				path = this.properties.getPrefix() + path;
				if (!path.startsWith("/")) {
					path = "/" + path;
				}
			}
			values.put(path, entry.getValue());
		}
		return values;
	}

	private Map<String, ZuulRoute> locateRoutesFromDB() {
		Map<String, ZuulRoute> routes = new LinkedHashMap<>();
		List<ZuulRouteVO> results = gatewayApiMapper.getListByEnabled();
		this.setEnableRoutes(results);
		for (ZuulRouteVO result : results) {
			if (StringUtils.isEmpty(result.getPath())
					|| StringUtils.isEmpty(result.getUrl())) {
				continue;
			}
			ZuulRoute zuulRoute = new ZuulRoute();
			try {
				org.springframework.beans.BeanUtils.copyProperties(result, zuulRoute);
			} catch (Exception e) {
				logger.error("=============load zuul route info from db with error==============", e);
			}
			routes.put(zuulRoute.getPath(), zuulRoute);
		}
		return routes;
	}

	public List<ZuulRouteVO> getEnableRoutes() {
		return enableRoutes;
	}

	public void setEnableRoutes(List<ZuulRouteVO> enableRoutes) {
		this.enableRoutes = enableRoutes;
	}

}
