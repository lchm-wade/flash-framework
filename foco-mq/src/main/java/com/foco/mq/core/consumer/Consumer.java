package com.foco.mq.core.consumer;

import java.lang.annotation.*;

/**
 * 方法所在的类需要加上{@link ConsumerAspect}
 *
 * <p>只关注public的方法，在{@link ConsumerOwner}注册过程中，能够减少
 * 部分没有意义的方法
 * <p> {@link #value()}含义为consumerId（全局唯一），不区别中间件，
 * 例如RocketMq、Kafka、RabbitMq都是共享的，重复则会在启动过程中抛异常
 *
 * @author ChenMing
 * @date 2021/12/27
 * @see ConsumerOwner
 * @see ConsumerAspect
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface Consumer {

    /**
     * 消费者consumerId （唯一性标识）
     * 支持${}进行注入
     */
    String value();
}
