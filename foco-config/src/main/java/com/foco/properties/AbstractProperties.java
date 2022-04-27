package com.foco.properties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/06 18:07
 */
public class AbstractProperties implements ApplicationContextAware {
    public static Map<String,AbstractProperties> configClassMap=new ConcurrentHashMap();
    private static ApplicationContext context;
    protected static  <T extends AbstractProperties> T getConfig(Class<T> clazz) {
        AbstractProperties config=configClassMap.get(clazz.getSimpleName());
            if (config == null) {
                synchronized (clazz) {
                    if (config == null) {
                        config = context.getBean(clazz);
                        configClassMap.put(clazz.getSimpleName(),config);
                    }
                }
            }
        return (T) config;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;

    }
}
