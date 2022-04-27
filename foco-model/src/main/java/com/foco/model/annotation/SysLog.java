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
 * @Author lucoo
 * @Date 2021/6/27 17:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
	String value() default "";
	boolean ignoreField() default false;
}