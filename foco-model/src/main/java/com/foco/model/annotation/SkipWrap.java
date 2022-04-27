package com.foco.model.annotation;

import java.lang.annotation.*;

/**
 * @Description 跳过响应体包装
 * @Author lucoo
 * @Date 2021/6/10 10:02
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SkipWrap {
}
