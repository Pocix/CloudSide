package com.cloudside.netty.common;

import java.io.Serializable;

public class Msg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;// 消息类型
	private String type;// 消息类型
	private String content;// 消息内容

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
