/**
 * @author Poci.
 * @date 2018/1/1
 */
package com.cloudside.api.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cloudside.common.utils.R;

@RestControllerAdvice
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String msg;
	
    private int code;

	private boolean status;
    public ApiException(){
    }

	public ApiException(ErrorCode code){
		super(code.getMsg());
		this.msg = code.getMsg();
		this.code=code.getCode();
		this.status=false;//有CODE 默认是返回错误
	}

	
	public ApiException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public ApiException(String msg, int code,boolean status) {
		super(msg);
		this.msg = msg;
		this.code = code;
		this.status=status;
	}


	public ApiException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}
	
	@ExceptionHandler(ApiException.class)
	public R handleApiException(ApiException e) {
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
		r.put("status",e.isStatus());
		return r;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
