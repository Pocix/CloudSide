package com.cloudside.config.monitor.web;


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

import com.cloudside.common.util.ResultMsg;
import com.cloudside.common.util.ResultStatusCode;
import com.cloudside.config.monitor.service.MonitorService;
import com.cloudside.mybatis.model.GroupInfo;
import com.cloudside.mybatis.model.TenantInfo;
import com.cloudside.mybatis.model.ZuulRouteVO;
import com.github.pagehelper.Page;

@RestController
public class MonitorController {
	
	@Autowired
	private MonitorService monitorSvc;

	ResultMsg result;
	
	private static Logger log = LoggerFactory.getLogger(MonitorController.class);

	/**
	 * 首页
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/index")
	public ModelAndView homePage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
		mv.addObject("author", "Poci");
		mv.setViewName("/index.html");
		return mv;
	}
    
    /**
     * 系统信息
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/sysInfo")
	public ModelAndView sysInfo(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
    	mv.setViewName("/views/system/index.html");
		return mv;
	}
    
    /**
     * 主页面
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/main")
	public ModelAndView mainPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
    	List<Map> servers = monitorSvc.getAllServicesInstalce();
    	Page<ZuulRouteVO> gateways = monitorSvc.getAllZuulList(null);
    	List<ZuulRouteVO> gatewaysEnable = monitorSvc.getZuulListByEnabled();
    	mv.addObject("SvcCount",servers.size());
    	mv.addObject("SvcList",servers);
    	mv.addObject("GatewayCount",gateways.getTotal());
    	mv.addObject("GatewayList",gateways);
    	mv.addObject("GatewayEnableCount",gatewaysEnable.size());
    	mv.addObject("GatewayEnableList",gatewaysEnable);
		mv.setViewName("/main.html");
		return mv;
	}
    
    /**
     * 网关集合
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/gateway")
	public ModelAndView gatewayPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response, @RequestParam Map<String,Object> data) {
    	List<Map> servers = monitorSvc.getAllServicesInstalce();
    	for(Map map:servers){
    		if("API-GATEWAY".equals(map.get("serviceId").toString().toUpperCase())){
    			mv.addObject("gatewayServer",map);
    		}
    	}
		Page<ZuulRouteVO> gateways = monitorSvc.getAllZuulList(data);
		mv.addObject("GatewayList",gateways);
		mv.addObject("findBy",data);
		mv.setViewName("/views/gateway/gateway.html");
		return mv;
	}
    
    /**
     * 网关表单
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/gateway/form")
	public ModelAndView gatewayAddPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
    	mv.setViewName("/views/gateway/addGateway.html");
		mv.addObject("gateway",null);
		if(request.getParameter("id") == null){
			return mv;
		}
		String gatewayId = request.getParameter("id").toString();
		ZuulRouteVO gateway = monitorSvc.getZuulRouteById(gatewayId);
		mv.addObject("gateway",gateway);
		return mv;
	}
    
    /**
     * 新增网关
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "/gateway/add")
	public String gatewayAddAndUpdPage(HttpServletRequest request,@RequestParam Map<String,Object> data) {
    	String gatewayId = data.get("id") != null ?data.get("id").toString():"";
    	int resultCode;
    	if("".equals(gatewayId)){
    		resultCode = monitorSvc.saveGateway(data);
    	}else{
    		resultCode = monitorSvc.updateGateway(data);
    	}
    	if(resultCode==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result.toString();
	}
    
    /**
     * 网关路由数据删除
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "/gateway/del")
	public ResultMsg gatewayDelete(HttpServletRequest request,@RequestParam Map<String,Object> data) {
    	String gatewayId = data.get("id") != null ?data.get("id").toString():"";
    	if(monitorSvc.deleteGatewayById(gatewayId)==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result;
	}
    
    /**
     * 用户列表
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/user/list")
	public ModelAndView userPage(ModelAndView mv,@RequestParam Map<String,Object> data,HttpServletRequest request,HttpServletResponse response) {
    	Page<TenantInfo> users = monitorSvc.getAllUser(data);
    	mv.addObject("users",users);
    	mv.addObject("findBy",data);
		mv.setViewName("/views/system/user/user.html");
		return mv;
	}
    
    /**
     * 用户编辑
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/user/form")
	public ModelAndView userAddPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
		mv.setViewName("/views/system/user/add.html");
		mv.addObject("user",null);
		if(request.getParameter("id") == null){
			return mv;
		}
		String userId = request.getParameter("id").toString();
		TenantInfo user = monitorSvc.getUserById(userId);
		mv.addObject("user",user);
		return mv;
	}
    
    /**
     * 新增用户
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "/user/add")
	public String userAddAndUpdPage(HttpServletRequest request,@RequestParam Map<String,Object> data) {
    	String userId = data.get("id") != null ?data.get("id").toString():"";
    	int resultCode;
    	if("".equals(userId)){
    		resultCode = monitorSvc.saveUser(data);
    	}else{
    		resultCode = monitorSvc.updateUser(data);
    	}
    	if(resultCode==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result.toString();
	}
    
    /**
     * 用户数据删除
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "/user/del")
	public ResultMsg userDelete(HttpServletRequest request,@RequestParam Map<String,Object> data) {
    	String userId = data.get("id") != null ?data.get("id").toString():"";
    	if(monitorSvc.deleteById(userId)==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result;
	}
    
    /**
     * 获取分组列表
     * @param mv
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/user2group")
	public ModelAndView user2groupPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response,@RequestParam("id") String id) {
    	mv.addObject("userid", id);
    	mv.addObject("allGroup", monitorSvc.getAllGroup(null));
    	mv.addObject("myGroup", monitorSvc.getGroupByIdWithOwner(id));
		mv.setViewName("/views/system/user/user2group.html");
		return mv;
	}
    
    /**
     * 用户与分组关系
     * @param request
     * @param response
     * @param data
     * @return
     */
    @RequestMapping(value = "/user2group/upd")
	public ResultMsg rolePage(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String,Object> data) {
    	String userId = data.get("id") != null ?data.get("id").toString():"";
    	String groupIds = data.get("groups") != null ?data.get("groups").toString():"";
    	if(monitorSvc.updateGroupByIdWithOwner(userId,groupIds)>=0){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result;
	}
    
    /**
     * 分组管理
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/group/list")
	public ModelAndView groupPage(ModelAndView mv,@RequestParam Map<String,Object> data,HttpServletRequest request,HttpServletResponse response) {
    	List<GroupInfo> groups = monitorSvc.getAllGroup(data);
    	mv.addObject("groups",groups);
    	mv.addObject("findBy",data);
		mv.setViewName("/views/system/role/group.html");
		return mv;
	}
    
    /**
     * 分组编辑
     * @param mv
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/group/form")
	public ModelAndView groupAddPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
    	mv.setViewName("/views/system/role/addGroup.html");
		mv.addObject("group",null);
		if(request.getParameter("id") == null){
			return mv;
		}
		String groupId = request.getParameter("id").toString();
		GroupInfo group = monitorSvc.getGroupById(groupId);
		mv.addObject("group",group);
		return mv;
	}
    
    /**
     * 新增分组
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "/group/add")
	public String groupAddAndUpdPage(HttpServletRequest request,@RequestParam Map<String,Object> data) {
    	String groupId = data.get("id") != null ?data.get("id").toString():"";
    	int resultCode;
    	if("".equals(groupId)){
    		resultCode = monitorSvc.saveGroup(data);
    	}else{
    		resultCode = monitorSvc.updateGroup(data);
    	}
    	if(resultCode==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result.toString();
	}
    
    /**
     * 删除分组
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "/group/del")
	public ResultMsg groupDelete(HttpServletRequest request,@RequestParam Map<String,Object> data) {
    	String groupId = data.get("id") != null ?data.get("id").toString():"";
    	if(monitorSvc.deleteGroupById(groupId)==1){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result;
	}
    
    /**
     * 获取网关列表
     * @param mv
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping(value = "/group/group2route")
	public ModelAndView group2RoutePage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response,@RequestParam("id") String id) {
    	mv.addObject("groupid", id);
    	mv.addObject("allRoute", monitorSvc.getAllZuulList(null));
    	mv.addObject("myRoute", monitorSvc.getRouteByIdWithOwner(id));
		mv.setViewName("/views/system/role/group2route.html");
		return mv;
	}
    
    /**
     * 分组与网关关系
     * @param request
     * @param response
     * @param data
     * @return
     */
    @RequestMapping(value = "/group2route/upd")
	public ResultMsg group2routePage(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String,Object> data) {
    	String userId = data.get("id") != null ?data.get("id").toString():"";
    	String routeIds = data.get("routes") != null ?data.get("routes").toString():"";
    	if(monitorSvc.updateRouteByIdWithOwner(userId,routeIds)>=0){
    		result = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
    	}else{
    		result = new ResultMsg(ResultStatusCode.DATABASE_OPERATE_ERR.getErrcode(), ResultStatusCode.DATABASE_OPERATE_ERR.getErrmsg(), null);
    	}
		return result;
	}
}
