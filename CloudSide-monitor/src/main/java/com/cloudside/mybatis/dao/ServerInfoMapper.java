package com.cloudside.mybatis.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cloudside.mybatis.model.ServerAreaInfo;
import com.cloudside.mybatis.model.ServerInfo;
import com.github.pagehelper.Page;

public interface ServerInfoMapper{
	
	/**
	 * get server_area .
	 * @return
	 */
	Page<ServerAreaInfo> queryAllServerArea();
	
	/**
	 * add server area name information.
	 * @param serverArea
	 * @return
	 */
	int saveServerArea(Map<String,Object> serverArea);
	
	/**
	 * Delete server area information according to id.
	 * @param id
	 * @return
	 */
	int queryServerAreaById(String id);
	
	/**
	 * Delete server area information according to id.
	 * @param id
	 * @return
	 */
	int deleteServerAreaById(String id);

	/**
	 * Delete host register information according to server_area_id.
	 * @param id
	 * @return
	 */
	int deleteRegServerById(String id);
	
	/**
	 * get servers.
	 * @return
	 */
	Page<ServerInfo> queryAllServers();
	
	/**
	 * add server information.
	 * @param server
	 * @return
	 */
	int saveServer(Map<String,Object> server);
	
	/**
	 * update cs_host_register information according to user.
	 * @param cs_host_register
	 * @return
	 */
	int updateServerById(Map<String,Object> user);
}
