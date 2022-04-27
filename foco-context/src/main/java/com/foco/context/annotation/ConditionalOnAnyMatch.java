package com.foco.context.annotation;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * TODO
 * 多组条件,任意一组匹配成功的判断
 *
 * @autor 程昭斌
 * @date 2021/4/22 17:30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnAnyMatchCondition.class)
public @interface ConditionalOnAnyMatch {
    String[] name();

    String[] value();
}
