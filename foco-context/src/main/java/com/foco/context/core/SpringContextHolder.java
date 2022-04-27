package com.foco.context.core;

import com.foco.model.constant.MainClassConstant;
import com.foco.model.constant.SpringApplicationListenerOrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.*;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * spring容器
 * @Author lucoo
 * @Date 2021/6/23 18:16
 */
@Slf4j
public class SpringContextHolder  implements GenericApplicationListener {

    private static ApplicationContext applicationContext;
    private static Environment environment;
    private AtomicBoolean init = new AtomicBoolean(false);
    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }
    /**
     * 获取environment
     */
    public static Environment getEnvironment() {
        checkApplicationContext();
        return environment;
    }
    /**
     * 获取spring bean
     */
    public static <T> T getBean(String beanName) {
        checkApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 获取spring bean
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(beanName, clazz);
    }

    /**
     * 获取spring bean
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        if (beans.isEmpty()) {
            return null;
        }
        return beans.values().iterator().next();
    }
    public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        checkApplicationContext();
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        if (beans.isEmpty()) {
            return new ArrayList<>();
        }
        return beans.values();
    }
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext为空，请检查配置");
        }
    }
    /**
     * 获取当前环境
     * 新版用法采用
     * @see EnvHelper
     * @return
     */
    @Deprecated
    public static Env getEnv() {
        String property = EnvHelper.getEnv();
        return Env.valueOf(property);
    }
    /**
     * 判断是否生产环境
     *新版用法采用
     *@see EnvHelper
     * @return
     */
    @Deprecated
    public static Boolean isProd() {
        return Env.isProd(getEnv());
    }
    public static String getProperty(String key){
        checkApplicationContext();
        return environment.getProperty(key);
    }
    public static <T> T getProperty(String key,Class<T> targetType){
        checkApplicationContext();
        return environment.getProperty(key,targetType);
    }
    public static String getProperty(String key,String defaultValue){
        checkApplicationContext();
        return environment.getProperty(key,defaultValue);
    }
    public void onApplicationEvent(ApplicationEvent event){
        if (init.get()) {
            return;
        }
        if(event instanceof ApplicationPreparedEvent){
            ConfigurableApplicationContext context = ((ApplicationPreparedEvent) event).getApplicationContext();
            if(ClassUtils.isPresent(MainClassConstant.SPRING_CLOUD,this.getClass().getClassLoader())){
                //微服务项目
                if (context.getParent()!=null){
                    applicationContext = context;
                    environment= context.getEnvironment();
                }
            }else {
                //单体项目
                applicationContext = context;
                environment= context.getEnvironment();
            }
        }
        init.set(true);
    }
    private static final Class<?>[] EVENT_TYPES = {
            ApplicationPreparedEvent.class};

    private static final Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return SpringApplicationListenerOrderConstants.SPRING_CONTEXT_ORDER;
    }
}
