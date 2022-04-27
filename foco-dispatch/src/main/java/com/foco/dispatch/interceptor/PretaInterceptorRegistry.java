package com.foco.dispatch.interceptor;

import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 拦截器注册器,将注册到PretaInterceptorRegistration里
 * @Author lucoo
 * @Date 2021/6/11 16:03
 **/
public class PretaInterceptorRegistry {
    private PretaInterceptorRegistry(){}
    private static PretaInterceptorRegistry registry;
    private final List<PretaInterceptorRegistration> registrations = new ArrayList<>();
    public PretaInterceptorRegistration addInterceptor(PretaHandlerInterceptor handlerInterceptor){
        PretaInterceptorRegistration registration=new PretaInterceptorRegistration(handlerInterceptor);
        this.registrations.add(registration);
        return registration;
    }
    public List<PretaInterceptorRegistration> getRegistrations() {
        return this.registrations.stream()
                .sorted(INTERCEPTOR_ORDER_COMPARATOR).collect(Collectors.toList());
    }
    private static final Comparator<Object> INTERCEPTOR_ORDER_COMPARATOR =
            OrderComparator.INSTANCE.withSourceProvider(object -> {
                if (object instanceof PretaInterceptorRegistration) {
                    return (Ordered) ((PretaInterceptorRegistration) object)::getOrder;
                }
                return null;
            });
    public static PretaInterceptorRegistry instance(){
        if(registry==null){
            synchronized (PretaInterceptorRegistry.class){
                if(registry==null){
                    registry=new PretaInterceptorRegistry();
                }
            }
        }
        return registry;
    }
}
