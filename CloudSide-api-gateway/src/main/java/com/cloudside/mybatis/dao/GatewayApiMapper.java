package com.cloudside.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.cloudside.mybatis.model.ZuulRouteVO;

public interface GatewayApiMapper{
	
	/**
	 * 查询可用路由
	 * @return
	 */
	@Select("select * from cs_gateway_api_define where enabled = true ")  
	public List<ZuulRouteVO> getListByEnabled();

}
