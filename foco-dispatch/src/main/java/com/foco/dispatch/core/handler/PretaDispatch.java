package com.foco.dispatch.core.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.foco.context.util.HttpContext;
import com.foco.context.util.ResponseUtils;
import com.foco.dispatch.PretaErrorCode;
import com.foco.dispatch.core.context.ClassMethod;
import com.foco.dispatch.core.context.ClassMethodHandlerMapping;
import com.foco.dispatch.core.context.RequestContext;
import com.foco.dispatch.interceptor.InterceptorHandler;
import com.foco.dispatch.interceptor.PretaHandlerInterceptor;
import com.foco.model.ApiResult;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 将此类注册到spring mvc中
 * @Author lucoo
 * @Date 2021/6/11 9:59
 **/
@Slf4j
@RestController
public class PretaDispatch {
    private ClassMethodHandlerMapping handlerMapping;
    private InterceptorHandler interceptorHandler;

    public PretaDispatch(ClassMethodHandlerMapping handlerMapping, InterceptorHandler interceptorHandler) {
        this.handlerMapping = handlerMapping;
        this.interceptorHandler = interceptorHandler;
    }

    public Object doService(@RequestBody String requestBody) throws Throwable {
        ReadContext ctx = JsonPath.parse(requestBody);
        String infoType = ctx.read("$.infoType");
        String eventType = ctx.read("$.eventType");
        if(StrUtil.isBlank(infoType)||StrUtil.isBlank(eventType)){
            ResponseUtils.write(HttpStatus.OK.value(),JSON.toJSONString(ApiResult.error(PretaErrorCode.URL_ERROR)));
            return null;
        }
        PretaRequest pretaRequest =new PretaRequest();
        pretaRequest.setInfoType(infoType);
        pretaRequest.setEventType(eventType);
        ClassMethod classMethod = handlerMapping.route(pretaRequest.getInfoType(), pretaRequest.getEventType());
        if(classMethod==null){
            ResponseUtils.write(HttpStatus.OK.value(), JSON.toJSONString(ApiResult.error(PretaErrorCode.URL_ERROR)));
            return null;
        }
        //Method method = classMethod.getMethod();
        Object bean = classMethod.getBean();
        RequestContext requestContext = new RequestContext(classMethod, pretaRequest);
        PretaHandlerInterceptor interceptor = interceptorHandler.preHandle(requestContext);
        if(interceptor!=null){
            log.error("{} intercepted",interceptor.getClass().getName());
            return null;
        }
        Object result = null;
        try {
            MethodAccess methodAccess = MethodAccess.get(classMethod.getBeanClass());
            int methodAccessIndex = methodAccess.getIndex(classMethod.getMethodName());
            if(classMethod.getParameterType()!=null){
                result = methodAccess.invoke(bean,methodAccessIndex,JSONObject.parseObject(requestBody, classMethod.getParameterType()));
            }else {
                result=methodAccess.invoke(bean,methodAccessIndex);
            }
            return result;
        }catch (Exception e) {
            throw ExceptionUtil.getRootCause(e);
        }finally {
            interceptorHandler.afterCompletion(requestContext,result);
        }
    }
}
