package com.foco.context.annotation;

import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;

import java.lang.annotation.*;

/**
 * 没有cloud环境
 *
 * @autor lucoo
 * @date 2021/6/22 17:30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnMissingClass(MainClassConstant.SPRING_CLOUD)
public @interface ConditionalOnMissingCloud {
}
