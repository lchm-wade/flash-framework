package com.foco.model.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多个ApiResultBuilder实现类时，加了该注解的优先选择
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResultBuilderPrimary {
}
