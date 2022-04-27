package com.foco.crypt.conditional;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @autor 程昭斌
 * @date 2021/4/22 17:30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnDefaultCondition.class)
public @interface ConditionalOnDefault {
    String name();
}
