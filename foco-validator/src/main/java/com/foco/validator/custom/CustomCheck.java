package com.foco.validator.custom;


import com.foco.context.common.StatusCode;
import java.lang.annotation.*;

/**
 * description: 自定义分组校验器
 *
 * @Author lucoo
 * @Date 2021/6/28 11:16
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomCheck {
    Class<?>[] groups() default {};
    String message() default "";
    Class<? extends CustomChecker> checkClass();
    Class<? extends StatusCode> checkTarget() default StatusCode.class;
}
