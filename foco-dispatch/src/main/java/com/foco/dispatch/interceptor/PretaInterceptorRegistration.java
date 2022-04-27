package com.foco.dispatch.interceptor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 拦截器注册处
 * @Author lucoo
 * @Date 2021/6/11 16:08
 **/
public class PretaInterceptorRegistration {
    private PretaHandlerInterceptor pretaHandlerInterceptor;
    private final List<String> includeClassMethod = new ArrayList<>();
    private final List<String> excludeClassMethod = new ArrayList<>();
    private int order = 0;
    public List<String> getIncludeClassMethod() {
        return includeClassMethod;
    }

    public List<String> getExcludeClassMethod() {
        return excludeClassMethod;
    }

    public PretaHandlerInterceptor getInterceptor() {
        return pretaHandlerInterceptor;
    }

    public PretaInterceptorRegistration(PretaHandlerInterceptor pretaHandlerInterceptor) {
        this.pretaHandlerInterceptor = pretaHandlerInterceptor;
    }
    public PretaInterceptorRegistration addClasses(String... patterns) {
        return addClasses(Arrays.asList(patterns));
    }
    private PretaInterceptorRegistration addClasses(List<String> patterns) {
        this.includeClassMethod.addAll(patterns);
        return this;
    }
    public PretaInterceptorRegistration excludeClassMethods(String... patterns) {
        return excludeClassMethods(Arrays.asList(patterns));
    }
    private PretaInterceptorRegistration excludeClassMethods(List<String> patterns) {
        this.excludeClassMethod.addAll(patterns);
        return this;
    }
    public PretaInterceptorRegistration order(int order){
        this.order = order;
        return this;
    }

    public int getOrder() {
        return order;
    }
}
