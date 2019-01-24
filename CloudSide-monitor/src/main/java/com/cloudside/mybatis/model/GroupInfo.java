package com.cloudside.mybatis.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GroupInfo implements Serializable{

	public GroupInfo(){
		super();
	}
	
	private static final long serialVersionUID = 1L;
	private String name;// 名称
	private String enname;// 英文名称
	private String groupLv;		// 分组等级
	private Date createDate;	// 创建日期
	private Date updateDate;	// 更新日期
	private String remarks;	// 备注
	private String delFlag;	//是否删除
	private String id;
	

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getGroupLv() {
		return groupLv;
	}

	public void setGroupLv(String groupLv) {
		this.groupLv = groupLv;
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
