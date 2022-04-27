package com.foco.dispatch.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.foco.dispatch.core.context.ClassMethodInterceptorMapping;
import com.foco.dispatch.core.context.RequestContext;
import com.foco.dispatch.core.handler.PretaRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 17:13
 **/

public class InterceptorHandler {
    @Autowired
    private ClassMethodInterceptorMapping interceptorMapping;
    public PretaHandlerInterceptor preHandle(RequestContext requestContext){
        PretaRequest pretaRequest = requestContext.getPretaRequest();
        String key= pretaRequest.getInfoType()+":"+ pretaRequest.getEventType();
        List<PretaHandlerInterceptor> interceptors = interceptorMapping.getInterceptors(key);
        if(CollectionUtil.isNotEmpty(interceptors)){
            for(PretaHandlerInterceptor interceptor:interceptors){
                boolean preHandle = interceptor.preHandle(requestContext.getClassMethod());
                if(!preHandle){
                    return interceptor;
                }
            }
        }
        return null;
    }
    public void afterCompletion(RequestContext requestContext, Object result){
        PretaRequest pretaRequest = requestContext.getPretaRequest();
        String key= pretaRequest.getInfoType()+":"+ pretaRequest.getEventType();
        List<PretaHandlerInterceptor> interceptors = interceptorMapping.getInterceptors(key);
        if(CollectionUtil.isNotEmpty(interceptors)){
            for(int i=interceptors.size()-1;i>=0;i--){
                interceptors.get(i).afterCompletion(requestContext.getClassMethod(),result);
            }
        }
    }
}
