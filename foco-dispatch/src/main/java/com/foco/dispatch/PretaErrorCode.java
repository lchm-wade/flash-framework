package com.foco.dispatch;

import com.foco.model.api.ApiErrorCode;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 10:58
 **/
public enum PretaErrorCode implements ApiErrorCode {
    URL_ERROR("60000", "not found handler"),
    INTERCEPTOR_ERROR("60001", "interceptor error"),
    ;
    private String code;
    private String msg;


    PretaErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Override
    public String getProjectCode() {
        return "SYS";
    }
    @Override
    public String getModularCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return toString(getCode(), getMsg());
    }
}
