package com.foco.http.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/10 16:56
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({HttpClientRegistrar.class})
public @interface HttpScan {
    String[] value() default {};
}
