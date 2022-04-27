package com.foco.model.exception;


import com.foco.model.api.ApiErrorCode;
import com.foco.model.constant.FocoErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * 业务处理异常
 * 具体的业务异常码各业务定义,实现ApiErrorCode接口的一个枚举
 * @Author lucoo
 * @Date 2021/6/23 16:26
 */
@Getter
@Setter
public class ApiException extends RuntimeException {
    /***错误码*/
    private String code;
    private Object[] params;
    @Deprecated
    public ApiException params(Object... params){
        this.params=params;
        return this;
    }
    public static void throwException(String code,String msg,Object... params){
        throw new ApiException(code,msg,params);
    }
    public static void throwException(ApiErrorCode error,Object... params){
        throw new ApiException(error,params);
    }
    public static void throwException(String msg){
        throw new ApiException(msg);
    }
    public static void throwException(String msg,ApiErrorCode error,Object... params){
        throw new ApiException(msg,error,params);
    }

    public ApiException(ApiErrorCode error,Object... params) {
        this(error.getCode(),error.getMsg(),params);
    }
    public ApiException(String msg) {
        this(FocoErrorCode.SYSTEM_ERROR.getCode(),msg);
    }
    public ApiException(String msg,ApiErrorCode error,Object... params) {
        this(error.getCode(),msg,params);
    }
    public ApiException(String code, String msg,Object... params) {
        super(msg);
        this.code = code;
        this.params=params;
    }
    public String toString() {
        return "ApiException(code=" + this.getCode() + ", params=" + Arrays.deepToString(this.getParams()) + ",message=" +this.getMessage()+")" ;
    }
}
