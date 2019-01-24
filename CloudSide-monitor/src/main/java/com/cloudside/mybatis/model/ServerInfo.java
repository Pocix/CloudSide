package com.cloudside.mybatis.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServerInfo implements Serializable{

	public ServerInfo(){
		super();
	}
	
	private static final long serialVersionUID = 1L;
	private String hostIp;// 服务域名
	private int hostPort;// 
	private String hostName;// 
	private String serverAreaId;// 
	private Date createDate;	// 创建日期
	private Date updateDate;	// 更新日期
	private String remarks;	// 备注
	private String delFlag;	//是否删除
	private String id;
	

	public String getId() {
		return id;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		if (updateDate == null){
			return createDate;
		}
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public int getHostPort() {
		return hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getServerAreaId() {
		return serverAreaId;
	}

	public void setServerAreaId(String serverAreaId) {
		this.serverAreaId = serverAreaId;
	}
	
}
