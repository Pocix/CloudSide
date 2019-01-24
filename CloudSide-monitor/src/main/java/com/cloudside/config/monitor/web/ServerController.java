package com.cloudside.config.monitor.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cloudside.common.util.ResultMsg;
import com.cloudside.common.util.ResultStatusCode;
import com.cloudside.config.monitor.service.ServerService;
import com.cloudside.mybatis.model.ServerInfo;
import com.github.pagehelper.Page;

/**
 * 服务
 * @author Administrator
 *
 */
@RestController
public class ServerController {
	
	@Autowired
	private ServerService serverSvc;

	ResultMsg result;
	
	private static Logger log = LoggerFactory.getLogger(ServerController.class);

	/**
     * 服务页面
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/services")
	public ModelAndView svcPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
    	List<Map> servers = serverSvc.queryAllServicesInstalce();
    	mv.addObject("SvcCount",servers.size());
    	mv.addObject("SvcList",servers);
		mv.setViewName("/views/svc/services.html");
		return mv;
	}
    
    /**
	 * 服务域首页
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/svc/areaIdx")
	public ModelAndView svcAreaIdx(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
		mv.addObject("author", "Poci");
		mv.setViewName("/views/svc/svc_area.html");
		return mv;
	}
    
    /**
	 * 服务域注册
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/svc/area/save")
	public String svcAreaSave(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String,Object> data) {
    	int resultCode = serverSvc.saveServerArea(data);
    	if(resultCode==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result.toString();
	}
    
    /**
	 * 服务域删除
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/svc/area/del")
	public String delAreaSave(HttpServletRequest request,HttpServletResponse response,@RequestParam String id) {
    	int resultCode = serverSvc.delServerArea(id);
    	if(resultCode==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result.toString();
	}
    
    /**
	 * 查询服务主机
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/svc/host/query")
	public Map<String, Object> querySvcHost(HttpServletRequest request,HttpServletResponse response) {
    	Page<ServerInfo> page = serverSvc.queryAllServers();
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("code", 0);  
    	result.put("msg", "");  
    	result.put("count", page.getTotal());  
    	result.put("data", page.getResult()); 
		return result;
	}
    
    /**
	 * 服务主机注册
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/svc/host/save")
	public String svcHostSave(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String,Object> data) {
    	int resultCode = serverSvc.saveServer(data);
    	if(resultCode==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result.toString();
	}
}
