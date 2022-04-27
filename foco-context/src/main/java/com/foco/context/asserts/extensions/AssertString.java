package com.foco.context.asserts.extensions;

import com.foco.model.api.ApiErrorCode;
import com.foco.model.exception.ApiException;
import com.foco.context.util.StringUtils;

public class AssertString extends AbstractIAssert<AssertString, String> {

    public AssertString(String param) {
        super(param);
        super.child = this;
    }

    public AssertString isEmpty(String msg) {
        return isEmpty(new ApiException(msg));
    }

    public AssertString isEmpty(ApiErrorCode error, Object... params){
        return isEmpty(new ApiException(error,params));
    }
    public AssertString isEmpty(String msg,ApiErrorCode error,Object... params){
        return isEmpty(new ApiException(msg,error,params));
    }
    public AssertString isEmpty(String code, String msg,Object... params){
        return isEmpty(new ApiException(code,msg,params));
    }
    public AssertString isEmpty(ApiException ex) {
        return checkField(StringUtils::isEmpty, ex);
    }

    public AssertString isNotEmpty(String msg) {
        return isNotEmpty(new ApiException(msg));
    }
    public AssertString isNotEmpty(ApiErrorCode error, Object... params){
        return isNotEmpty(new ApiException(error,params));
    }
    public AssertString isNotEmpty(String msg,ApiErrorCode error,Object... params){
        return isNotEmpty(new ApiException(msg,error,params));
    }
    public AssertString isNotEmpty(String code, String msg,Object... params){
        return isNotEmpty(new ApiException(code,msg,params));
    }

    public AssertString isNotEmpty(ApiException ex) {
        return checkField(StringUtils::isNotEmpty, ex);
    }

    public AssertString isBlank(String msg) {
        return isBlank(new ApiException(msg));
    }
    public AssertString isBlank(ApiErrorCode error, Object... params){
        return isBlank(new ApiException(error,params));
    }
    public AssertString isBlank(String msg,ApiErrorCode error,Object... params){
        return isBlank(new ApiException(msg,error,params));
    }
    public AssertString isBlank(String code, String msg,Object... params){
        return isBlank(new ApiException(code,msg,params));
    }

    public AssertString isBlank(ApiException ex) {
        return checkField(StringUtils::isBlank, ex);
    }

    public AssertString isNotBlank(String msg) {
        return isNotBlank(new ApiException(msg));
    }

    public AssertString isNotBlank(ApiErrorCode error, Object... params){
        return isNotBlank(new ApiException(error,params));
    }

    public AssertString isNotBlank(String msg,ApiErrorCode error,Object... params){
        return isNotBlank(new ApiException(msg,error,params));
    }

    public AssertString isNotBlank(String code, String msg,Object... params){
        return isNotBlank(new ApiException(code,msg,params));
    }

    public AssertString isNotBlank(ApiException ex) {
        return checkField(StringUtils::isNotBlank, ex);
    }

}
