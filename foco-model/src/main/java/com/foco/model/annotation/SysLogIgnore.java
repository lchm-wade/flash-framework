/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.foco.model.annotation;

import java.lang.annotation.*;

/**
 * 加该注解后不会加该参数写入日志
 * @Author lucoo
 * @Date 2021/6/27 17:55
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogIgnore {
	String value() default "";
}
