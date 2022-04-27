package com.foco.status.code.serialize;

import com.foco.context.common.StatusCode;
import com.foco.context.util.HttpContext;
import com.foco.model.ApiResult;
import com.foco.model.constant.ResponseBodyOrderConstants;
import com.foco.status.code.handler.IStatusCodeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * @Description 针对响应不是json格式数据的处理比如
 * List<Integer> test()
 * json格式的处理请见
 * @see StatusCodeConverterSerialize
 * @Author lucoo
 * @Date 2021/6/10 10:34
 **/
@RestControllerAdvice
@Order(ResponseBodyOrderConstants.STATUS_FORMAT_BODY)
@Slf4j
public class ResponseBodyStatusCodeConverterSerialize implements ResponseBodyAdvice {
    private String simpleName;
    StatusCode[] statusCodes;

    IStatusCodeHandler statusCodeHandler;
    public ResponseBodyStatusCodeConverterSerialize(IStatusCodeHandler statusCodeHandler) {
        this.statusCodeHandler = statusCodeHandler;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.hasMethodAnnotation(StatusCodeConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(HttpContext.isFeignRequest()){
            return body;
        }
        boolean handle=false;
        List list=new ArrayList();
        if(body instanceof ApiResult){
            Object data=((ApiResult<?>) body).getData();
            if(data instanceof Collection){
                handle=true;
                list = (List) data;
            }
        }
        if(body instanceof Collection){
            handle=true;
            list= (List)body;
        }
        if(handle){
            StatusCodeConverter statusCodeConverter = returnType.getMethodAnnotation(StatusCodeConverter.class);
            simpleName=statusCodeConverter.handler().getSimpleName();
            try {
                statusCodes = (StatusCode[]) statusCodeConverter.handler().getMethod("values").invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Object> returnList=new ArrayList<>();
            for(Object v:list){
                if(v instanceof StatusCode){
                    StatusCode statusCode=(StatusCode)v;
                    statusCode.setMessage(statusCodeHandler.resolveConvert(simpleName, statusCode.getCode(),statusCodes));
                    returnList.add(statusCode);
                }else {
                    returnList.add(statusCodeHandler.resolveConvert(simpleName,String.valueOf(v),statusCodes));
                }
            }
            return returnList;
        }
        return body;
    }
}
