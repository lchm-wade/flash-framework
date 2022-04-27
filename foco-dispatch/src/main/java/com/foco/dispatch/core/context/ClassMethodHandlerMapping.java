package com.foco.dispatch.core.context;

import com.foco.context.annotation.PretaClass;
import com.foco.context.annotation.PretaMethod;
import com.foco.model.exception.SystemException;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 10:07
 **/
public class ClassMethodHandlerMapping implements ApplicationContextAware {
    private Map<String, ClassMethod> handlerMapping=new ConcurrentHashMap<>();
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(PretaClass.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object bean = entry.getValue();
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            PretaClass pretaClass = targetClass.getAnnotation(PretaClass.class);
            String infoType = pretaClass.value();
            ReflectionUtils.doWithMethods(targetClass,(method)->{
                if(method.isAnnotationPresent(PretaMethod.class)){

                    PretaMethod pretaMethod = method.getAnnotation(PretaMethod.class);
                    String eventType = pretaMethod.value();
                    String key = buildKey(infoType, eventType);
                    if(handlerMapping.containsKey(key)){
                        SystemException.throwException(String.format("%s:%s重复存在",infoType,eventType));
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?> parameterType = null;
                    if(parameterTypes.length>0){
                        parameterType=parameterTypes[0];
                    }
                    ClassMethod classMethod=new ClassMethod(bean,targetClass,method.getName(),parameterType);
                    handlerMapping.put(key,classMethod);
                }
            });
        }
    }
    public ClassMethod route(String infoType, String eventType){
        return handlerMapping.get(buildKey(infoType,eventType));
    }

    public Map<String, ClassMethod> getHandlerMapping() {
        return handlerMapping;
    }
    public String buildKey(String infoType,String eventType){
        return infoType+":"+eventType;
    }
}
