package com.cloudside.zuul.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudside.common.util.MD5Util;
import com.cloudside.common.util.ResultMsg;
import com.cloudside.common.util.ResultStatusCode;
import com.cloudside.jwt.util.AccessToken;
import com.cloudside.jwt.util.Audience;
import com.cloudside.jwt.util.JwtHelper;
import com.cloudside.jwt.util.LoginPara;
import com.cloudside.mybatis.dao.UserInfoMapper;
import com.cloudside.mybatis.model.UserInfo;
import com.cloudside.zuul.event.RefreshRouteService;

/**
 * Created by xujingfeng on 2017/4/1.
 */
@RestController
public class GatewayController {

	private static Logger log = LoggerFactory.getLogger(GatewayController.class);
	
	@Autowired
	RefreshRouteService refreshRouteService;
	
	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Autowired  
    private Audience audienceEntity;

	@RequestMapping("/refreshRoute")
	public ResultMsg refreshRoute() {
		refreshRouteService.refreshRoute();
		resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(),   
                ResultStatusCode.OK.getErrmsg(), "refreshRoute");
		return resultMsg;
	}

	@Autowired
	ZuulHandlerMapping zuulHandlerMapping;

	private ResultMsg resultMsg;

	@RequestMapping("/watchNowRoute")
	public String watchNowRoute() {
		// 可以用debug模式看里面具体是什么
		Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
		return "watchNowRoute";
	}

	@RequestMapping("oauth/token")
	public ResultMsg getAccessToken(@RequestBody LoginPara loginPara) {
		try {
			// 验证码校验在后面章节添加
			// 验证用户名密码
			UserInfo user = userInfoMapper.getByLoginName(loginPara.getUserName());
			if (user == null) {
				resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),  
                        ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
				return resultMsg;
			} else {
				String md5Password = MD5Util.getMD5(loginPara.getPassword() + user.getSalt());

				if (md5Password.compareTo(user.getPassword()) != 0) {
					resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),
							ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
					return resultMsg;
				}
			}
 
			// 拼装accessToken
			String accessToken = JwtHelper.createJWT(loginPara.getUserName(), String.valueOf(user.getId()),
					user.getUserType(), user.getClientId(), user.getIpaddress(),
					audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());

			// 返回accessToken
			AccessToken accessTokenEntity = new AccessToken();
			accessTokenEntity.setAccess_token(accessToken);
			accessTokenEntity.setExpires_in(audienceEntity.getExpiresSecond());
			accessTokenEntity.setToken_type("bearer");
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(),   
                    ResultStatusCode.OK.getErrmsg(), accessTokenEntity);
			return resultMsg;

		} catch (Exception ex) {
			log.error("get token error:{0}",ex.getMessage());
		}
		return null;
	}

}
