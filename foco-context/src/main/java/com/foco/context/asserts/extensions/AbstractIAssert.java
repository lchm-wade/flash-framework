package com.foco.context.asserts.extensions;


import com.foco.model.api.ApiErrorCode;
import com.foco.model.exception.ApiException;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public abstract class AbstractIAssert<C, P> {

    protected C child;
    protected P param;

    public AbstractIAssert(P param) {
        this.param = param;
    }

    public C isNull(String msg) {
        return isNull(new ApiException(msg));
    }

    public C isNull(ApiErrorCode error, Object... params){
        return isNull(new ApiException(error,params));
    }
    public C isNull(String msg,ApiErrorCode error,Object... params){
        return isNull(new ApiException(msg,error,params));
    }
    public C isNull(String code, String msg,Object... params){
        return isNull(new ApiException(code,msg,params));
    }

    public C isNull(ApiException ex) {
        return checkField(Objects::isNull, ex);
    }

    public C isNotNull(String msg) {
        return isNotNull(new ApiException(msg));
    }
    public C isNotNull(ApiErrorCode error, Object... params){
        return isNotNull(new ApiException(error,params));
    }
    public C isNotNull(String msg,ApiErrorCode error,Object... params){
        return isNotNull(new ApiException(msg,error,params));
    }
    public C isNotNull(String code, String msg,Object... params){
        return isNotNull(new ApiException(code,msg,params));
    }

    public C isNotNull(ApiException ex) {
        return checkField(Objects::nonNull, ex);
    }

    public C isEqualTo(P p, String msg) {
        return isEqualTo(p, new ApiException(msg));
    }
    public C isEqualTo(P p,ApiErrorCode error, Object... params){
        return isEqualTo(p,new ApiException(error,params));
    }
    public C isEqualTo(P p,String msg,ApiErrorCode error,Object... params){
        return isEqualTo(p,new ApiException(msg,error,params));
    }
    public C isEqualTo(P p,String code, String msg,Object... params){
        return isEqualTo(p,new ApiException(code,msg,params));
    }

    public C isEqualTo(P p, ApiException ex) {
        return check(() -> Objects.equals(param, p), ex);
    }

    public C isNotEqualTo(P p, String msg) {
        return isNotEqualTo(p, new ApiException(msg));
    }
    public C isNotEqualTo(P p,ApiErrorCode error, Object... params){
        return isNotEqualTo(p,new ApiException(error,params));
    }
    public C isNotEqualTo(P p,String msg,ApiErrorCode error,Object... params){
        return isNotEqualTo(p,new ApiException(msg,error,params));
    }
    public C isNotEqualTo(P p,String code, String msg,Object... params){
        return isNotEqualTo(p,new ApiException(code,msg,params));
    }

    public C isNotEqualTo(P p, ApiException ex) {
        return check(() -> !Objects.equals(param, p), ex);
    }

    /**
     * 直接校验this.param
     * @param func
     * @param ex
     * @return 当前对象
     */
    protected C checkField(Predicate<P> func, ApiException ex){
        return check(func.test(param), ex);
    }

    /**
     * this.param和其它参数比较时，语义更清晰一些
     * checkField(Predicate<P> func, ApiException ex)也能实现该接口的功能
     * @param func
     * @param ex
     * @return 当前对象
     */
    protected C check(BooleanSupplier func, ApiException ex) {
        return check(func.getAsBoolean(), ex);
    }

    private C check(boolean boo, ApiException ex){
        if(boo){
            return child;
        }
        if (ex == null) {
            throw new ApiException("断言失败，异常提示信息为空");
        }
        throw ex;
    }
}
