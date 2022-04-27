package com.foco.auth.condition;

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
@Conditional(OnAuthCondition.class)
public @interface ConditionalOnAuth {
    String value();
}
