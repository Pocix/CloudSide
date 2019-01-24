package com.cloudside.common.controller;

import org.springframework.stereotype.Controller;

import com.cloudside.common.utils.ShiroUtils;
import com.cloudside.system.domain.UserDO;
import com.cloudside.system.domain.UserToken;

@Controller
public class BaseController {
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}