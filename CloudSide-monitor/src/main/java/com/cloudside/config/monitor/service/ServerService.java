package com.cloudside.config.monitor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudside.common.util.UuidUtil;
import com.cloudside.mybatis.dao.ServerInfoMapper;
import com.cloudside.mybatis.model.ServerInfo;
import com.github.pagehelper.Page;

@Service
public class ServerService {

    @Autowired
    private DiscoveryClient client;
    
    @Autowired
    private ServerInfoMapper serverInfoMapper;

    public List<Map> queryAllServicesInstalce() {
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
    
    public int saveServerArea(Map<String,Object> server){
    	server.put("id", UuidUtil.getUUID());
    	server.put("delFlag", server.get("delFlag") == null ? 0 : server.get("delFlag"));
    	server.put("createDate", new Date());
    	server.put("updateDate", server.get("createDate"));
    	return serverInfoMapper.saveServerArea(server);
    }
    
    @Transactional
    public int delServerArea(String id){
    	serverInfoMapper.deleteServerAreaById(id);
    	return serverInfoMapper.deleteRegServerById(id);
    }
    
    public Page<ServerInfo> queryAllServers(){
    	return serverInfoMapper.queryAllServers();
    }
    
    public int delServer(String id){
    	return serverInfoMapper.deleteRegServerById(id);
    }
    
    public int saveServer(Map<String,Object> server){
    	server.put("id", UuidUtil.getUUID());
    	server.put("delFlag", server.get("delFlag") == null ? 0 : server.get("delFlag"));
    	server.put("createDate", new Date());
    	server.put("hostPort", Integer.parseInt(server.get("hostPort").toString()));
    	server.put("updateDate", server.get("createDate"));
    	return serverInfoMapper.saveServer(server);
    }
    
}
