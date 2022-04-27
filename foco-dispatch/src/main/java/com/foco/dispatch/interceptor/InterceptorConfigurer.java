package com.foco.dispatch.interceptor;
/**
 * @Description 客户端实现该接口配置拦截器
 * @Author lucoo
 * @Date 2021/6/12 9:42
 **/
public interface InterceptorConfigurer {
    void addInterceptors(PretaInterceptorRegistry registry);
}
