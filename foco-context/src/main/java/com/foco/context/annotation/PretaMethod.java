package com.foco.context.annotation;
import java.lang.annotation.*;

/**
 * @Description 该注解放在方法上面,代表该方法是一个业务处理方法
 * @Author lucoo
 * @Date 2021/6/11 10:01
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PretaMethod {
    String value();
}
