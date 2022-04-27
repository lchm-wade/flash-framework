package com.foco.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 忽略逻辑删除,mapper接口的方法上加了此注解时,不再做逻辑删除的where is_deleted=0的拼接操作
 * @Author lucoo
 * @Date 2021/6/3 14:19
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreLogicDelete {
}
