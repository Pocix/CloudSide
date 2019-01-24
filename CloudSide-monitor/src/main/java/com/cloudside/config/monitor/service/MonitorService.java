package com.cloudside.config.monitor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudside.common.util.MD5Util;
import com.cloudside.common.util.UuidUtil;
import com.cloudside.mybatis.dao.GatewayApiMapper;
import com.cloudside.mybatis.dao.GroupInfoMapper;
import com.cloudside.mybatis.dao.TenantInfoMapper;
import com.cloudside.mybatis.model.GroupInfo;
import com.cloudside.mybatis.model.TenantInfo;
import com.cloudside.mybatis.model.ZuulRouteVO;
import com.github.pagehelper.Page;

@Service
public class MonitorService {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    GatewayApiMapper gatewayApiMapper;
    
    @Autowired
    TenantInfoMapper userInfoMapper;
    
    @Autowired
    GroupInfoMapper groupInfoMapper;
    
    public List<Map> getAllServicesInstalce() {
    	List<ServiceInstance> listSI = client.getInstances("STORES");
    	List<Map> listServices = null;
    	List<String> list = client.getServices();
        if(list != null && list.size() > 0){
		listServices = new ArrayList();
	        for(String s : list){  
	            List<ServiceInstance> serviceInstances =  client.getInstances(s);  
	            for(ServiceInstance si : serviceInstances){  
	            	Map _tmp = new HashMap();
	    			_tmp.put("host", si.getHost());
	    			_tmp.put("port", si.getPort());
	    			_tmp.put("serviceId", si.getServiceId());
	    			_tmp.put("secure", si.isSecure());
	    			_tmp.put("uri", si.getUri());
	    			_tmp.put("metadata", si.getMetadata());
	    			listServices.add(_tmp);
	            }  
	              
	        }
	        return listServices;
        }
        return null;
    }
    
    public List<ZuulRouteVO> getZuulListByEnabled(){
    	return gatewayApiMapper.getListByEnabled();
    }
    
    public ZuulRouteVO getZuulRouteById(String id){
    	return gatewayApiMapper.getZuulRouteById(id);
    }
    
    public Page<ZuulRouteVO> getAllZuulList(Map<String,Object> data){
    	//PageHelper.startPage(1,2) 分页;
    	return gatewayApiMapper.getAllZuulList(data);
    }
    
    public int saveGateway(Map<String,Object> gateway){
    	gateway.put("id", UuidUtil.getUUID());
    	gateway.put("retryable", gateway.get("retryable") == null ? 0 : gateway.get("retryable"));
    	gateway.put("enabled", gateway.get("enabled") == null ? 0 : gateway.get("enabled"));
    	gateway.put("stripPrefix", gateway.get("stripPrefix") == null ? 0 : gateway.get("stripPrefix"));
    	gateway.put("delFlag", gateway.get("delFlag") == null ? 0 : gateway.get("delFlag"));
    	gateway.put("createDate", new Date());
    	gateway.put("updateDate", gateway.get("createDate"));
    	return gatewayApiMapper.saveGateway(gateway);
    }
    
    public int updateGateway(Map<String,Object> gateway){
    	gateway.put("retryable", gateway.get("retryable") == null ? 0 : gateway.get("retryable"));
    	gateway.put("enabled", gateway.get("enabled") == null ? 0 : gateway.get("enabled"));
    	gateway.put("stripPrefix", gateway.get("stripPrefix") == null ? 0 : gateway.get("stripPrefix"));
    	gateway.put("delFlag", gateway.get("delFlag") == null ? 0 : gateway.get("delFlag"));
    	gateway.put("updateDate", new Date());
    	return gatewayApiMapper.updateById(gateway);
    }
    
    
    public Page<TenantInfo> getAllUser(Map<String,Object> param){
    	return userInfoMapper.getAllUserWithType(param);
    }
    
    public int saveUser(Map<String,Object> user){
    	user.put("id", UuidUtil.getUUID());
    	user.put("clientId", UuidUtil.getUUID());
    	user.put("salt", UuidUtil.getUUID());
    	user.put("password", MD5Util.generate(user.get("password").toString()+user.get("salt").toString()));
    	user.put("delFlag", user.get("delFlag") == null ? 0 : "on".equals(user.get("delFlag")) ? 1 : 0);
    	user.put("createDate", new Date());
    	user.put("userType", 1);
    	user.put("updateDate", user.get("createDate"));
    	return userInfoMapper.saveUser(user);
    }
    
    public TenantInfo getUserById(String id){
    	return userInfoMapper.queryById(id);
    }
    
    public int updateUser(Map<String,Object> user){
    	if(user.get("password").toString().length() != 32){
    		user.put("salt", UuidUtil.getUUID());
    		user.put("password", MD5Util.generate(user.get("password").toString()+user.get("salt")));
    	}
    	user.put("delFlag", user.get("delFlag") == null ? 0 : user.get("delFlag"));
    	user.put("updateDate", new Date());
    	return userInfoMapper.updateById(user);
    }
    
    public int deleteById(String id){
    	return userInfoMapper.deleteById(id);
    }
    
    public List<GroupInfo> getAllGroup(Map<String,Object> data){
    	return groupInfoMapper.getAllGroup(data).getResult();
    }
    
    public List<GroupInfo> getGroupByIdWithOwner(String id){
    	return groupInfoMapper.queryByIdWithOwner(id);
    }
    
    @Transactional
    public int updateGroupByIdWithOwner(String id,String groupIds){
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	groupInfoMapper.deleteUser2GroupById(id);
    	Set<String> dist = new HashSet<String>();
    	for(String tmp : groupIds.split(",")){
    		dist.add(tmp);
    	}
    	for(String tmp : dist){
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("userId", id);
    		map.put("groupId", tmp);
    		list.add(map);
    	}
    	return groupInfoMapper.saveUser2Group(list);
    }
    
    public GroupInfo getGroupById(String id){
    	return groupInfoMapper.queryById(id);
    }
    
    public int saveGroup(Map<String,Object> group){
    	group.put("id", UuidUtil.getUUID());
    	group.put("delFlag", group.get("delFlag") == null ? 0 : "on".equals(group.get("delFlag")) ? 1 : 0);
    	group.put("createDate", new Date());
    	group.put("updateDate", group.get("createDate"));
    	return groupInfoMapper.saveGroup(group);
    }
    
    public int updateGroup(Map<String,Object> group){
    	group.put("delFlag", group.get("delFlag") == null ? 0 : group.get("delFlag"));
    	group.put("updateDate", new Date());
    	return groupInfoMapper.updateById(group);
    }
    
    public int deleteGroupById(String id){
    	return groupInfoMapper.deleteById(id);
    }
    
    public List<ZuulRouteVO> getRouteByIdWithOwner(String id){
    	return gatewayApiMapper.queryByIdWithOwner(id);
    }
    
    @Transactional
    public int updateRouteByIdWithOwner(String id,String routeIds){
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	gatewayApiMapper.deleteGroup2RouteById(id);
    	Set<String> dist = new HashSet<String>();
    	for(String tmp : routeIds.split(",")){
    		dist.add(tmp);
    	}
    	for(String tmp : dist){
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("groupId", id);
    		map.put("gatewayId", tmp);
    		list.add(map);
    	}
    	return gatewayApiMapper.saveGroup2Route(list);
    }
    
    public int deleteGatewayById(String id){
    	return gatewayApiMapper.deleteById(id);
    }
}
