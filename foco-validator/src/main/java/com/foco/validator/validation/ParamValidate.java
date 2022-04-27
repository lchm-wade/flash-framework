package com.foco.validator.validation;

import java.lang.annotation.*;

/**
 * description: 自定义分组校验器
 *
 * @Author lucoo
 * @Date 2021/6/28 11:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamValidate {
    Class<?>[] groups() default {};
}
