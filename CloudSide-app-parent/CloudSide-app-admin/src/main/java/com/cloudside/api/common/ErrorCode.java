package com.cloudside.api.common;

/**
 * 全局错误码定义
 * @author caosheng
 * @date 2018/01/18
 */
public enum  ErrorCode {
    REQUEST_EXPIRE(1001,"请求SessionKeyID过期"),
    METHOD_NOTFOUND(1002,"调用接口不存在"),
    PARAM_INVALID(1003,"无效参数"),
    PARAM_NOEXIST(1004,"接口不存在指定参数"),
    REQUEST_WXAPI_ERROR(1006,"请求微信API错误"),
    SESSIONKEYID_NOTFOUND(1007,"SessionKeyID参数不存在"),
    RESPONSE_DATA(1008,"返回数据为空"),
    SERVER_ERROR(1009,"服务处理异常"),






    PARTICIPATE_EXCEPTION(2001,"您已经参与过了!"),
    SOLITAIRE_EXCEPTION(2002,"该接龙活动人数已经满了!"),

    ;

    private String msg;
    private int code;

    private ErrorCode(int code,String msg)
    {
        this.code=code;
        this.msg=msg;
    }

    public String getMsg()
    {
        return this.msg;
    }
    public int getCode() {
        return this.code;
    }
}
