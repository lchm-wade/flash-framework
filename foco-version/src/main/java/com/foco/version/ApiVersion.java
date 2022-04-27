package com.foco.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description 控制api版本注解
 * @date 2021-06-29 17:09
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    double value();
}
