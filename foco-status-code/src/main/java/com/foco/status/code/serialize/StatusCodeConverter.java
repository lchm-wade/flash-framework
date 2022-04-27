package com.foco.status.code.serialize;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.foco.context.common.StatusCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 *对状态进行转换
 * 将数字转成中文
 * @Author lucoo
 * @Date 2021/6/23 18:16
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = StatusCodeConverterSerialize.class)
public @interface StatusCodeConverter {
    Class<? extends StatusCode> handler();
}
