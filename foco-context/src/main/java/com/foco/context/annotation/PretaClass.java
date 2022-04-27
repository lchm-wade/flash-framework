package com.foco.context.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description 该注解放在类上面,代表该类是一个业务处理类
 * @Author lucoo
 * @Date 2021/6/11 10:01
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface PretaClass {
    String value();
}
