package com.foco.version.autoconfigure;

import com.foco.version.ApiVersionRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @Author lucoo
 * @Date 2021/6/21 9:31
 **/
public class CustomWebMvcRegistrations implements WebMvcRegistrations {
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new ApiVersionRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        return handlerMapping;
    }
}

