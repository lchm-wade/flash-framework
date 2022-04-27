package com.foco.context.asserts.extensions;

import com.foco.model.api.ApiErrorCode;
import com.foco.model.exception.ApiException;
import com.foco.context.util.CollectionUtils;

import java.util.Collection;

public class AssertCollection extends AbstractIAssert<AssertCollection, Collection<?>> {

    public AssertCollection(Collection<?> param) {
        super(param);
        super.child = this;
    }

    public AssertCollection isEmpty(String msg) {
        return isEmpty(new ApiException(msg));
    }

    public AssertCollection isEmpty(ApiErrorCode error, Object... params){
        return isEmpty(new ApiException(error,params));
    }
    public AssertCollection isEmpty(String msg,ApiErrorCode error,Object... params){
        return isEmpty(new ApiException(msg,error,params));
    }
    public AssertCollection isEmpty(String code, String msg,Object... params){
        return isEmpty(new ApiException(code,msg,params));
    }

    public AssertCollection isEmpty(ApiException ex) {
        return checkField(CollectionUtils::isEmpty, ex);
    }

    public AssertCollection isNotEmpty(String msg) {
        return isNotEmpty(new ApiException(msg));
    }

    public AssertCollection isNotEmpty(ApiErrorCode error, Object... params){
        return isNotEmpty(new ApiException(error,params));
    }
    public AssertCollection isNotEmpty(String msg,ApiErrorCode error,Object... params){
        return isNotEmpty(new ApiException(msg,error,params));
    }
    public AssertCollection isNotEmpty(String code, String msg,Object... params){
        return isNotEmpty(new ApiException(code,msg,params));
    }

    public AssertCollection isNotEmpty(ApiException ex) {
        return checkField(x -> !CollectionUtils.isEmpty(x), ex);
    }

}
