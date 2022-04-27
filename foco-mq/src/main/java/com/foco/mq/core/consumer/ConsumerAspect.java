package com.foco.mq.core.consumer;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 声明消费类
 * <p>不支持父类传递，在设计中消费者不允许相同元素（例：{@link Consumer#value()}）
 * 出现，父类传递的复用意义暂无，故不予支持
 * @author ChenMing
 * @date 2021/10/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Component
public @interface ConsumerAspect {

}
