package com.cloudside.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.cloudside.mybatis.model.ZuulRouteVO;
import com.github.pagehelper.Page;

public interface GatewayApiMapper{
	
	/**
	 * 查询可用路由
	 * @return
	 */
	public List<ZuulRouteVO> getListByEnabled();
	
	/**
	 * 查询所有路由
	 * @return
	 */
//	@Select({"<script> ",
//			 "select * from cs_gateway_api_define a where 1=1 ",
//			 "<when test='u.path!=null'> ",
//			 "and locate(#{u.path},a.path) > 0 ",
//			 "</when>",
//			 "<when test='u.url!=null'> ",
//			 "and locate(#{u.url},a.url) > 0",
//			 "</when>",
//			 "</script> "
//	})
	public Page<ZuulRouteVO> getAllZuulList(Map<String, Object> param);

	/**
	 * 查询所有路由
	 * @return
	 */
	public List<ZuulRouteVO> queryByIdWithOwner(String id);
	
	/**
	 * add Group2Route information.
	 * @param group
	 * @return
	 */
	int saveGroup2Route(List<Map<String,Object>> route);

	/**
	 * Delete Group2Route relation according to id.
	 * @param id
	 * @return
	 */
	int deleteGroup2RouteById(String id);

	public ZuulRouteVO getZuulRouteById(String id);

	public int saveGateway(Map<String, Object> gateway);
	
	int updateById(Map<String,Object> gateway);
	
	int deleteById(String id);
}
