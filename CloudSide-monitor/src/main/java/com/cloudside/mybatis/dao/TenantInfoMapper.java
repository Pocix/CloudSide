package com.cloudside.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.cloudside.mybatis.model.TenantInfo;
import com.github.pagehelper.Page;

public interface TenantInfoMapper{
	
	/**
	 * get users according to login name. 
	 * @param loginName
	 * @return
	 */
	List<TenantInfo> getByLoginName(String loginName);
	
	/**
	 * get users of type 1.
	 * @return
	 */
	Page<TenantInfo> getAllUserWithType(Map<String,Object> param);
	
	/**
	 * add user information.
	 * @param user
	 * @return
	 */
	int saveUser(Map<String,Object> user);

	/**
	 * get user according to id.
	 * @param id
	 * @return
	 */
	TenantInfo queryById(String id);
	
	/**
	 * update user information according to user.
	 * @param user
	 * @return
	 */
	int updateById(Map<String,Object> user);
	
	/**
	 * delete user information according to id.
	 * @param id
	 * @return
	 */
	int deleteById(String id);
}
