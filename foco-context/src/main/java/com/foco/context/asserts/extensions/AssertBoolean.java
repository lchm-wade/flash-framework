package com.foco.context.asserts.extensions;

import com.foco.model.api.ApiErrorCode;
import com.foco.model.exception.ApiException;

public class AssertBoolean extends AbstractIAssert<AssertBoolean, Boolean> {

    public AssertBoolean(Boolean param) {
        super(param);
        super.child = this;
    }

    public AssertBoolean isTrue(String msg) {
        return isTrue(new ApiException(msg));
    }

    public AssertBoolean isTrue(ApiErrorCode error, Object... params){
        return isTrue(new ApiException(error,params));
    }
    public AssertBoolean isTrue(String msg,ApiErrorCode error,Object... params){
        return isTrue(new ApiException(msg,error,params));
    }
    public AssertBoolean isTrue(String code, String msg,Object... params){
        return isTrue(new ApiException(code,msg,params));
    }

    public AssertBoolean isTrue(ApiException ex) {
        this.isNotNull(ex);
        return checkField(x -> x, ex);
    }


    public AssertBoolean isFalse(String msg) {
        return isFalse(new ApiException(msg));
    }

    public AssertBoolean isFalse(ApiErrorCode error, Object... params){
        return isFalse(new ApiException(error,params));
    }
    public AssertBoolean isFalse(String msg,ApiErrorCode error,Object... params){
        return isFalse(new ApiException(msg,error,params));
    }
    public AssertBoolean isFalse(String code, String msg,Object... params){
        return isFalse(new ApiException(code,msg,params));
    }
    public AssertBoolean isFalse(ApiException ex) {
        this.isNotNull(ex);
        return checkField(x -> !x, ex);
    }

}
