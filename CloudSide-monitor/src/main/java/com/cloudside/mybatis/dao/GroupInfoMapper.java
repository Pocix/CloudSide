package com.cloudside.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.cloudside.mybatis.model.GroupInfo;
import com.github.pagehelper.Page;

public interface GroupInfoMapper{
	
	/**
	 * get groups according to name. 
	 * @param name
	 * @return
	 */
	List<GroupInfo> getByName(String name);
	
	/**
	 * get groups of type.
	 * @return
	 */
	Page<GroupInfo> getAllGroupWithLv(String lv);
	
	/**
	 * get groups .
	 * @return
	 */
	Page<GroupInfo> getAllGroup(Map<String,Object> data);
	
	/**
	 * get group according to id.
	 * @param name
	 * @return
	 */
	GroupInfo queryById(String id);
	
	/**
	 * add group information.
	 * @param group
	 * @return
	 */
	int saveGroup(Map<String,Object> group);
	
	/**
	 * add group information.
	 * @param group
	 * @return
	 */
	int saveUser2Group(List<Map<String,Object>> group);
	
	/**
	 * update group information according to id.
	 * @param group
	 * @return
	 */
	int updateById(Map<String,Object> group);
	
	/**
	 * update user information according to id.
	 * @param id
	 * @return
	 */
	int deleteById(String id);
	
	/**
	 * Delete User2Group relation according to id.
	 * @param id
	 * @return
	 */
	int deleteUser2GroupById(String id);
	
	/**
	 * get group according to id.
	 * @param name
	 * @return
	 */
	List<GroupInfo> queryByIdWithOwner(String id);
}
