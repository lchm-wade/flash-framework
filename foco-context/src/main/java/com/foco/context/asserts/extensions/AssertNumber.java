package com.foco.context.asserts.extensions;


import com.foco.model.api.ApiErrorCode;
import com.foco.model.exception.ApiException;

import java.math.BigDecimal;
import java.util.Objects;


public class AssertNumber extends AbstractIAssert<AssertNumber, Number> {

    public AssertNumber(Number param) {
        super(param);
        super.child = this;
    }

    public AssertNumber isEq(Number p, String msg) {
        return isEq(p, new ApiException(msg));
    }

    public AssertNumber isEq(Number p,ApiErrorCode error, Object... params){
        return isEq(p,new ApiException(error,params));
    }
    public AssertNumber isEq(Number p,String msg,ApiErrorCode error,Object... params){
        return isEq(p,new ApiException(msg,error,params));
    }
    public AssertNumber isEq(Number p,String code, String msg,Object... params){
        return isEq(p,new ApiException(code,msg,params));
    }

    public AssertNumber isEq(Number p, ApiException ex) {
        return check(() -> compare(p, ex) == 0, ex);
    }


    public AssertNumber isNe(Number p, String msg) {
        return isNe(p, new ApiException(msg));
    }

    public AssertNumber isNe(Number p,ApiErrorCode error, Object... params){
        return isNe(p,new ApiException(error,params));
    }
    public AssertNumber isNe(Number p,String msg,ApiErrorCode error,Object... params){
        return isNe(p,new ApiException(msg,error,params));
    }
    public AssertNumber isNe(Number p,String code, String msg,Object... params){
        return isNe(p,new ApiException(code,msg,params));
    }


    public AssertNumber isNe(Number p, ApiException ex) {
        return check(() -> compare(p, ex) != 0, ex);
    }

    public AssertNumber isGt(Number p, String msg) {
        return isGt(p, new ApiException(msg));
    }

    public AssertNumber isGt(Number p,ApiErrorCode error, Object... params){
        return isGt(p,new ApiException(error,params));
    }
    public AssertNumber isGt(Number p,String msg,ApiErrorCode error,Object... params){
        return isGt(p,new ApiException(msg,error,params));
    }
    public AssertNumber isGt(Number p,String code, String msg,Object... params){
        return isGt(p,new ApiException(code,msg,params));
    }

    public AssertNumber isGt(Number p, ApiException ex) {
        return check(() -> compare(p, ex) > 0, ex);
    }

    public AssertNumber isGe(Number p, String msg) {
        return isGe(p, new ApiException(msg));
    }


    public AssertNumber isGe(Number p,ApiErrorCode error, Object... params){
        return isGe(p,new ApiException(error,params));
    }
    public AssertNumber isGe(Number p,String msg,ApiErrorCode error,Object... params){
        return isGe(p,new ApiException(msg,error,params));
    }
    public AssertNumber isGe(Number p,String code, String msg,Object... params){
        return isGe(p,new ApiException(code,msg,params));
    }
    public AssertNumber isGe(Number p, ApiException ex) {
        return check(() -> compare(p, ex) >= 0, ex);
    }

    public AssertNumber isLt(Number p, String msg) {
        return isLt(p, new ApiException(msg));
    }

    public AssertNumber isLt(Number p,ApiErrorCode error, Object... params){
        return isLt(p,new ApiException(error,params));
    }
    public AssertNumber isLt(Number p,String msg,ApiErrorCode error,Object... params){
        return isLt(p,new ApiException(msg,error,params));
    }
    public AssertNumber isLt(Number p,String code, String msg,Object... params){
        return isLt(p,new ApiException(code,msg,params));
    }

    public AssertNumber isLt(Number p, ApiException ex) {
        return check(() -> compare(p, ex) < 0, ex);
    }

    public AssertNumber isLe(Number p, String msg) {
        return isLe(p, new ApiException(msg));
    }

    public AssertNumber isLe(Number p,ApiErrorCode error, Object... params){
        return isLe(p,new ApiException(error,params));
    }
    public AssertNumber isLe(Number p,String msg,ApiErrorCode error,Object... params){
        return isLe(p,new ApiException(msg,error,params));
    }
    public AssertNumber isLe(Number p,String code, String msg,Object... params){
        return isLe(p,new ApiException(code,msg,params));
    }

    public AssertNumber isLe(Number p, ApiException ex) {
        return check(() -> compare(p, ex) <= 0, ex);
    }

    private int compare(Number p, ApiException ex) {
        this.isNotNull(ex);
        check(() -> Objects.nonNull(p), ex);
        return new BigDecimal(this.param.toString()).compareTo(new BigDecimal(p.toString()));
    }
}
