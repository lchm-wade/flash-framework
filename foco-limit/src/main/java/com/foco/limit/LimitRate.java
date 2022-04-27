package com.foco.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认当前用户 3秒内访问1次
 * @autor lucoo
 * @date 2021/6/27 11:42
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRate {
    /**单位时间内 允许访问次数 */
    int limitCount() default 1;
    /**限流时间 以秒为单位*/
    long limitTime() default 3;
}
