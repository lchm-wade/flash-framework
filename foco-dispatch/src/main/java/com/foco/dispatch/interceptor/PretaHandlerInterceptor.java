package com.foco.dispatch.interceptor;


import com.foco.dispatch.core.context.ClassMethod;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 15:49
 **/
public interface PretaHandlerInterceptor {
    default boolean preHandle(ClassMethod classMethod) {
        return true;
    }
    default void afterCompletion(ClassMethod classMethod, Object result) {

    }
}
