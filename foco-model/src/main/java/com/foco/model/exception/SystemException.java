package com.foco.model.exception;


import com.foco.model.api.ApiErrorCode;
import com.foco.model.constant.FocoErrorCode;
import lombok.Data;

import java.util.Arrays;

/**
 * 业务处理异常
 * 具体的业务异常码各业务定义,实现BaseErrorEnum接口的一个枚举
 * @Author lucoo
 * @Date 2021/6/23 16:26
 */
@Data
public class SystemException extends RuntimeException {
    /***错误码*/
    private String code;
    public static SystemException throwException(ApiErrorCode error){
        throw new SystemException(error);
    }
    public SystemException(ApiErrorCode error) {
        super(error.getMsg());
        this.code = error.getCode();
    }

    public static void throwException(ApiErrorCode error, Throwable throwable){
        throw new SystemException(error,throwable);
    }

    public SystemException(ApiErrorCode error, Throwable throwable) {
        super(error.getMsg(),throwable);
        this.code = error.getCode();
    }

    public static void throwException(String errMsg){
        throw new SystemException(errMsg);
    }
    public SystemException(String errMsg) {
        super(errMsg);
        this.code = FocoErrorCode.SYSTEM_ERROR.getCode();
    }

    public static void throwException(String errMsg, Throwable throwable){
        throw new SystemException(errMsg,throwable);
    }
    public SystemException(String errMsg, Throwable throwable) {
        super(errMsg,throwable);
        this.code = FocoErrorCode.SYSTEM_ERROR.getCode();
    }

    public static void throwException(String errCode, String errorMsg){
        throw new SystemException(errCode,errorMsg);
    }
    public SystemException(String code, String errorMsg) {
        super(errorMsg);
        this.code = code;
    }

    public static void throwException(String errorCode, String errorMsg, Throwable throwable){
        throw new SystemException(errorCode,errorMsg,throwable);
    }
    public SystemException(String code, String errorMsg, Throwable throwable) {
        super(errorMsg,throwable);
        this.code = code;
    }
    public String toString() {
        return "ApiException(code=" + this.getCode() +",message=" +this.getMessage()+")" ;
    }
}
