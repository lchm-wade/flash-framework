package com.foco.dispatch.core.context;


import cn.hutool.core.collection.CollectionUtil;
import com.foco.dispatch.interceptor.InterceptorConfigurer;
import com.foco.dispatch.interceptor.PretaHandlerInterceptor;
import com.foco.dispatch.interceptor.PretaInterceptorRegistration;
import com.foco.dispatch.interceptor.PretaInterceptorRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 10:07
 **/
public class ClassMethodInterceptorMapping implements ApplicationContextAware {
    @Autowired
    private ClassMethodHandlerMapping classMethodHandlerMapping;
    @Autowired(required = false)
    private InterceptorConfigurer configurer;
    private Map<String, List<PretaHandlerInterceptor>> interceptorMapping=new ConcurrentHashMap<>();
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ClassMethod> handlerMapping = classMethodHandlerMapping.getHandlerMapping();
        PretaInterceptorRegistry registry = PretaInterceptorRegistry.instance();
        if(configurer!=null){
            configurer.addInterceptors(registry);
        }
        List<PretaInterceptorRegistration> registrations = PretaInterceptorRegistry.instance().getRegistrations();
        if(CollectionUtil.isNotEmpty(registrations)){
            for(Map.Entry<String, ClassMethod> entry:handlerMapping.entrySet()){
                String key = entry.getKey();
                for(PretaInterceptorRegistration registration:registrations){
                    List<String> includeClassMethods = registration.getIncludeClassMethod();
                    List<String> excludeClassMethods = registration.getExcludeClassMethod();
                    for(String includeClassMethod:includeClassMethods){
                        if(candidate(includeClassMethod,excludeClassMethods,key)){
                            putKey(key,registration);
                            break;
                        }
                    }
                }
            }
        }
    }
    private void putKey(String key, PretaInterceptorRegistration registration){
        if(interceptorMapping.get(key)!=null){
            List<PretaHandlerInterceptor> pretaHandlerInterceptors = interceptorMapping.get(key);
            pretaHandlerInterceptors.add(registration.getInterceptor());
        }else {
            List<PretaHandlerInterceptor> pretaHandlerInterceptors=new ArrayList<>();
            pretaHandlerInterceptors.add(registration.getInterceptor());
            interceptorMapping.put(key,pretaHandlerInterceptors);
        }
    }
    public List<PretaHandlerInterceptor> getInterceptors(String key) {
        return interceptorMapping.get(key);
    }
    private boolean candidate(String includeClassMethod,List<String> excludeClassMethods,String key){
        return (includeClassMethod.equals("**")&&!excludeClassMethods.contains(key))
                ||(key.split(":")[0].equals(includeClassMethod.split(":")[0])
                &&!excludeClassMethods.contains(key));
    }
}
