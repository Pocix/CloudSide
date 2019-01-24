package com.cloudside.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.cloudside.mybatis.model.UserInfo;

public interface UserInfoMapper{
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	@Select("SELECT * from cs_user where login_name = #{loginName}")  
	public UserInfo getByLoginName(String loginName);

}
