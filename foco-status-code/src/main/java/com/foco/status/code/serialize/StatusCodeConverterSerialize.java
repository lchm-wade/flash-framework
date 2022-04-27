package com.foco.status.code.serialize;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.foco.context.common.StatusCode;
import com.foco.context.core.SpringContextHolder;
import com.foco.context.util.HttpContext;
import com.foco.status.code.handler.IStatusCodeHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * TODO
 * 将数字转成中文
 *
 * @Author lucoo
 * @Date 2021/6/23 18:16
 */
@Slf4j
public class StatusCodeConverterSerialize extends JsonSerializer<Object> implements ContextualSerializer {
    StatusCode[] statusCodes;
    private String simpleName;
    private IStatusCodeHandler statusCodeHandler;
    public StatusCodeConverterSerialize() {
    }

    public StatusCodeConverterSerialize(Class<? extends StatusCode> handler) {
        try {
            statusCodes = (StatusCode[]) handler.getMethod("values").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        simpleName = handler.getSimpleName();
        statusCodeHandler= SpringContextHolder.getBean(IStatusCodeHandler.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String finalVal = String.valueOf(value);
        if (HttpContext.isNotFeignRequest()) {
            try {
                finalVal = statusCodeHandler.resolveConvert(simpleName, String.valueOf(value), statusCodes);
            } catch (Exception e) {
                log.error("状态码转换异常", e);
            }
        }
        jsonGenerator.writeString(finalVal);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            StatusCodeConverter statusCodeConverter = beanProperty.getAnnotation(StatusCodeConverter.class);
            if (statusCodeConverter != null) {
                return new StatusCodeConverterSerialize(statusCodeConverter.handler());
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.getDefaultNullValueSerializer();
    }
}
