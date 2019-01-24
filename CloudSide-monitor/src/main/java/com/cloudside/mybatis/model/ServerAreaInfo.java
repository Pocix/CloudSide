package com.cloudside.mybatis.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServerAreaInfo implements Serializable{

	public ServerAreaInfo(){
		super();
	}
	
	private static final long serialVersionUID = 1L;
	private String serverAreaName;// 服务域名
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

	public String getServerAreaName() {
		return serverAreaName;
	}



	public void setServerAreaName(String serverAreaName) {
		this.serverAreaName = serverAreaName;
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
	
}
