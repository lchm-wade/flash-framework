package com.foco.dispatch.core.context;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 10:08
 **/
@Getter
@Setter
public class ClassMethod {
    private Object bean;
    private Class<?> beanClass;
    private String methodName;

    private Class<?> parameterType;
    public ClassMethod(Object bean, Class<?> beanClass,String methodName,Class parameterType) {
        this.bean = bean;
        this.beanClass=beanClass;
        this.methodName = methodName;
        this.parameterType=parameterType;
    }
}
