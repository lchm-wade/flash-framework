package com.foco.status.code.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.foco.context.common.StatusCode;
import com.foco.context.core.SpringContextHolder;
import com.foco.status.code.handler.IStatusCodeHandler;
import java.io.IOException;

/**
 * @Description todo
 * @Author lucoo
 * @Date 2021/5/28 17:14
 */
public class StatusCodeFormatterDeserialize extends JsonDeserializer<Object> implements ContextualDeserializer {
    StatusCode[] statusCodes;
    private String simpleName;
    private IStatusCodeHandler statusCodeHandler;
    public StatusCodeFormatterDeserialize(Class<? extends StatusCode> handler) {
        try {
            statusCodes = (StatusCode[]) handler.getMethod("values").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        simpleName = handler.getSimpleName();
        statusCodeHandler= SpringContextHolder.getBean(IStatusCodeHandler.class);
    }

    public StatusCodeFormatterDeserialize() {
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getValueAsString();
        return statusCodeHandler.resolveFormat(simpleName,value,statusCodes);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        StatusCodeFormat statusCodeConverter = beanProperty.getAnnotation(StatusCodeFormat.class);
        if (statusCodeConverter != null) {
            return new StatusCodeFormatterDeserialize(statusCodeConverter.handler());
        }
        return deserializationContext.findContextualValueDeserializer(beanProperty.getType(), beanProperty);
    }
}
