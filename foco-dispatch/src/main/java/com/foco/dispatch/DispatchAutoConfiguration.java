package com.foco.dispatch;

import com.foco.context.util.BootStrapPrinter;
import com.foco.dispatch.properties.DispatchProperties;
import com.foco.dispatch.core.context.ClassMethodHandlerMapping;
import com.foco.dispatch.core.context.ClassMethodInterceptorMapping;
import com.foco.dispatch.core.handler.PretaDispatch;
import com.foco.dispatch.interceptor.InterceptorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 11:54
 **/
@Slf4j
@EnableConfigurationProperties(DispatchProperties.class)
public class DispatchAutoConfiguration implements ApplicationContextAware {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-dispatch",this.getClass());
    }
    @Bean
    public ClassMethodHandlerMapping classMethodHandlerMapping(){
        return new ClassMethodHandlerMapping();
    }
    @Bean
    public ClassMethodInterceptorMapping classMethodInterceptorMapping(){
        return new ClassMethodInterceptorMapping();
    }
    @Bean
    public InterceptorHandler interceptorHandler(){
        return new InterceptorHandler();
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        DispatchProperties dispatchProperties = applicationContext.getBean(DispatchProperties.class);
        ClassMethodHandlerMapping classMethodHandlerMapping = applicationContext.getBean(ClassMethodHandlerMapping.class);
        InterceptorHandler interceptorHandler=applicationContext.getBean(InterceptorHandler.class);
        PretaDispatch pretaDispatch = new PretaDispatch(classMethodHandlerMapping,interceptorHandler);
        RequestMappingInfo requestMappingInfo = RequestMappingInfo
                .paths(dispatchProperties.getUrl())
                .methods(RequestMethod.GET,RequestMethod.POST)
                .build();
        try {
            requestMappingHandlerMapping.registerMapping(requestMappingInfo, pretaDispatch, pretaDispatch.getClass().getMethod("doService",String.class));
        } catch (NoSuchMethodException e) {
            log.error("注册dispatch异常",e);
        }
    }
}
