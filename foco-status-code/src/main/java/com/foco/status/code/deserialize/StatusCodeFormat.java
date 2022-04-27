package com.foco.status.code.deserialize;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.foco.context.common.StatusCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 *对状态进行转换
 * 将前端传的中文转成后端数字接收
 * @Author lucoo
 * @Date 2021/6/23 18:16
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = StatusCodeFormatterDeserialize.class)
public @interface StatusCodeFormat {
    Class<? extends StatusCode> handler();
}
